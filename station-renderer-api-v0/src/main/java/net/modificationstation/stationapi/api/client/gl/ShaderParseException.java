package net.modificationstation.stationapi.api.client.gl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ShaderParseException extends IOException {
	private final List<ShaderParseException.JsonStackTrace> traces = Lists.newArrayList();
	private final String message;

	public ShaderParseException(String message) {
		this.traces.add(new ShaderParseException.JsonStackTrace());
		this.message = message;
	}

	public ShaderParseException(String message, Throwable cause) {
		super(cause);
		this.traces.add(new ShaderParseException.JsonStackTrace());
		this.message = message;
	}

	public void addFaultyElement(String jsonKey) {
		this.traces.get(0).add(jsonKey);
	}

	public void addFaultyFile(String path) {
		this.traces.get(0).fileName = path;
		this.traces.add(0, new ShaderParseException.JsonStackTrace());
	}

	public String getMessage() {
		Object var10000 = this.traces.get(this.traces.size() - 1);
		return "Invalid " + var10000 + ": " + this.message;
	}

	public static ShaderParseException wrap(Exception cause) {
		if (cause instanceof ShaderParseException) {
			return (ShaderParseException)cause;
		} else {
			String string = cause.getMessage();
			if (cause instanceof FileNotFoundException) {
				string = "File not found";
			}

			return new ShaderParseException(string, cause);
		}
	}

	public static class JsonStackTrace {
		@Nullable
		String fileName;
		private final List<String> faultyElements = Lists.newArrayList();

		JsonStackTrace() {
		}

		void add(String element) {
			this.faultyElements.add(0, element);
		}

		@Nullable
		public String getFileName() {
			return this.fileName;
		}

		public String joinStackTrace() {
			return StringUtils.join(this.faultyElements, "->");
		}

		public String toString() {
			if (this.fileName != null) {
				if (this.faultyElements.isEmpty()) {
					return this.fileName;
				} else {
					String var10000 = this.fileName;
					return var10000 + " " + this.joinStackTrace();
				}
			} else {
				return this.faultyElements.isEmpty() ? "(Unknown file)" : "(Unknown file) " + this.joinStackTrace();
			}
		}
	}
}
