package net.modificationstation.stationapi.api.client.gl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.FileNameUtil;
import net.modificationstation.stationapi.api.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the flattening of "moj_" import strings in the loaded GLSL shader file.
 * Instances of an import are replaced by the contents of the referenced file
 * prefixed by a comment describing the line position and original file location
 * of the import.
 */
@Environment(EnvType.CLIENT)
public abstract class GLImportProcessor {
	private static final String MULTI_LINE_COMMENT_PATTERN = "/\\*(?:[^*]|\\*+[^*/])*\\*+/";
	private static final String SINGLE_LINE_COMMENT_PATTERN = "//[^\\v]*";
	private static final Pattern MOJ_IMPORT_PATTERN = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*moj_import(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(?:\"(.*)\"|<(.*)>))");
	private static final Pattern IMPORT_VERSION_PATTERN = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*version(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(\\d+))\\b");
	private static final Pattern TRAILING_WHITESPACE_PATTERN = Pattern.compile("(?:^|\\v)(?:\\s|/\\*(?:[^*]|\\*+[^*/])*\\*+/|(//[^\\v]*))*\\z");

	/**
	 * Reads the source code supplied into a list of lines suitable for uploading to
	 * the GL Shader cache.
	 * 
	 * <p>Imports are processed as per the description of this class.
	 */
	public List<String> readSource(String source) {
		GLImportProcessor.Context context = new GLImportProcessor.Context();
		List<String> list = this.parseImports(source, context, "");
		list.set(0, this.readImport(list.get(0), context.column));
		return list;
	}

	private List<String> parseImports(String source, GLImportProcessor.Context context, String path) {
		int i = context.line;
		int j = 0;
		String string = "";
		List<String> list = Lists.newArrayList();
		Matcher matcher = MOJ_IMPORT_PATTERN.matcher(source);

		String string2;
		while(matcher.find()) {
			if (!method_36424(source, matcher, j)) {
				string2 = matcher.group(2);
				boolean bl = string2 != null;
				if (!bl) {
					string2 = matcher.group(3);
				}

				if (string2 != null) {
					String string3 = source.substring(j, matcher.start(1));
					String string4 = path + string2;
					String string5 = this.loadImport(bl, string4);
					int k;
					if (!Strings.isNullOrEmpty(string5)) {
						if (!StringHelper.endsWithLineBreak(string5)) {
							string5 = string5 + System.lineSeparator();
						}

						++context.line;
						k = context.line;
						List<String> list2 = this.parseImports(string5, context, bl ? FileNameUtil.getPosixFullPath(string4) : "");
						list2.set(0, String.format(Locale.ROOT, "#line %d %d\n%s", 0, k, this.extractVersion(list2.get(0), context)));
						if (!StringUtils.isBlank(string3)) {
							list.add(string3);
						}

						list.addAll(list2);
					} else {
						String string6 = bl ? String.format("/*#moj_import \"%s\"*/", string2) : String.format("/*#moj_import <%s>*/", string2);
						list.add(string + string3 + string6);
					}

					k = StringHelper.countLines(source.substring(0, matcher.end(1)));
					string = String.format(Locale.ROOT, "#line %d %d", k, i);
					j = matcher.end(1);
				}
			}
		}

		string2 = source.substring(j);
		if (!StringUtils.isBlank(string2)) {
			list.add(string + string2);
		}

		return list;
	}

	/**
	 * Converts a line known to contain an import into a fully-qualified
	 * version of itself for insertion as a comment.
	 */
	private String extractVersion(String line, GLImportProcessor.Context context) {
		Matcher matcher = IMPORT_VERSION_PATTERN.matcher(line);
		if (matcher.find() && method_36423(line, matcher)) {
			context.column = Math.max(context.column, Integer.parseInt(matcher.group(2)));
			String var10000 = line.substring(0, matcher.start(1));
			return var10000 + "/*" + line.substring(matcher.start(1), matcher.end(1)) + "*/" + line.substring(matcher.end(1));
		} else {
			return line;
		}
	}

	private String readImport(String line, int start) {
		Matcher matcher = IMPORT_VERSION_PATTERN.matcher(line);
		if (matcher.find() && method_36423(line, matcher)) {
			String var10000 = line.substring(0, matcher.start(2));
			return var10000 + Math.max(start, Integer.parseInt(matcher.group(2))) + line.substring(matcher.end(2));
		} else {
			return line;
		}
	}

	private static boolean method_36423(String string, Matcher matcher) {
		return !method_36424(string, matcher, 0);
	}

	private static boolean method_36424(String string, Matcher matcher, int i) {
		int j = matcher.start() - i;
		if (j == 0) {
			return false;
		} else {
			Matcher matcher2 = TRAILING_WHITESPACE_PATTERN.matcher(string.substring(i, matcher.start()));
			if (!matcher2.find()) {
				return true;
			} else {
				int k = matcher2.end(1);
				return k == matcher.start();
			}
		}
	}

	/**
	 * Called to load an import reference's source code.
	 */
	@Nullable
	public abstract String loadImport(boolean inline, String name);

	/**
	 * A context for the parser to keep track of its current line and caret position in the file.
	 */
	@Environment(EnvType.CLIENT)
	static final class Context {
		int column;
		int line;
	}
}
