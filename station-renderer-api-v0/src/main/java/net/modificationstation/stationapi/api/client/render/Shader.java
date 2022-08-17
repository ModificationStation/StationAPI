package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlConst;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.*;
import net.modificationstation.stationapi.api.client.texture.AbstractTexture;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceFactory;
import net.modificationstation.stationapi.api.util.FileNameUtil;
import net.modificationstation.stationapi.api.util.JsonHelper;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Environment(EnvType.CLIENT)
public class Shader implements GlShader, AutoCloseable {
	private static final String CORE_DIRECTORY = MODID + "/shaders/core/";
	private static final String INCLUDE_DIRECTORY = MODID + "/shaders/include/";
	private static final Uniform DEFAULT_UNIFORM = new Uniform();
	private static final boolean field_32780 = true;
	private static Shader activeShader;
	private static int activeShaderId;
	private final Map<String, Object> samplers = Maps.newHashMap();
	private final List<String> samplerNames = Lists.newArrayList();
	private final List<Integer> loadedSamplerIds = Lists.newArrayList();
	private final List<GlUniform> uniforms = Lists.newArrayList();
	private final List<Integer> loadedUniformIds = Lists.newArrayList();
	private final Map<String, GlUniform> loadedUniforms = Maps.newHashMap();
	private final int programId;
	private final String name;
	private boolean dirty;
	private final GlBlendState blendState;
	private final List<Integer> loadedAttributeIds;
	private final List<String> attributeNames;
	private final Program vertexShader;
	private final Program fragmentShader;
	private final VertexFormat format;
	@Nullable
	public final GlUniform modelViewMat;
	@Nullable
	public final GlUniform projectionMat;
	@Nullable
	public final GlUniform viewRotationMat;
	@Nullable
	public final GlUniform textureMat;
	@Nullable
	public final GlUniform screenSize;
	@Nullable
	public final GlUniform colorModulator;
	@Nullable
	public final GlUniform light0Direction;
	@Nullable
	public final GlUniform light1Direction;
	@Nullable
	public final GlUniform fogMode;
	@Nullable
	public final GlUniform fogDensity;
	@Nullable
	public final GlUniform fogStart;
	@Nullable
	public final GlUniform fogEnd;
	@Nullable
	public final GlUniform fogColor;
	@Nullable
	public final GlUniform fogShape;
	@Nullable
	public final GlUniform lineWidth;
	@Nullable
	public final GlUniform gameTime;
	@Nullable
	public final GlUniform chunkOffset;

	public Shader(ResourceFactory factory, String name, VertexFormat format) throws IOException {
		this.name = name;
		this.format = format;
		Identifier identifier = Identifier.of(CORE_DIRECTORY + name + ".json");
		try (BufferedReader reader = factory.openAsReader(identifier);){
			JsonArray jsonArray3;
			JsonArray jsonArray2;
			JsonObject jsonObject = JsonHelper.deserialize(reader);
			String string = JsonHelper.getString(jsonObject, "vertex");
			String string2 = JsonHelper.getString(jsonObject, "fragment");
			JsonArray jsonArray = JsonHelper.getArray(jsonObject, "samplers", null);
			if (jsonArray != null) {
				int i = 0;
				for (JsonElement jsonElement : jsonArray) {
					try {
						this.readSampler(jsonElement);
					}
					catch (Exception exception) {
						ShaderParseException shaderParseException = ShaderParseException.wrap(exception);
						shaderParseException.addFaultyElement("samplers[" + i + "]");
						throw shaderParseException;
					}
					++i;
				}
			}
			if ((jsonArray2 = JsonHelper.getArray(jsonObject, "attributes", null)) != null) {
				int j = 0;
				this.loadedAttributeIds = Lists.newArrayListWithCapacity(jsonArray2.size());
				this.attributeNames = Lists.newArrayListWithCapacity(jsonArray2.size());
				for (JsonElement jsonElement2 : jsonArray2) {
					try {
						this.attributeNames.add(JsonHelper.asString(jsonElement2, "attribute"));
					}
					catch (Exception exception2) {
						ShaderParseException shaderParseException2 = ShaderParseException.wrap(exception2);
						shaderParseException2.addFaultyElement("attributes[" + j + "]");
						throw shaderParseException2;
					}
					++j;
				}
			} else {
				this.loadedAttributeIds = null;
				this.attributeNames = null;
			}
			if ((jsonArray3 = JsonHelper.getArray(jsonObject, "uniforms", null)) != null) {
				int k = 0;
				for (JsonElement jsonElement3 : jsonArray3) {
					try {
						this.addUniform(jsonElement3);
					}
					catch (Exception exception3) {
						ShaderParseException shaderParseException3 = ShaderParseException.wrap(exception3);
						shaderParseException3.addFaultyElement("uniforms[" + k + "]");
						throw shaderParseException3;
					}
					++k;
				}
			}
			this.blendState = Shader.readBlendState(JsonHelper.getObject(jsonObject, "blend", null));
			this.vertexShader = Shader.loadProgram(factory, Program.Type.VERTEX, string);
			this.fragmentShader = Shader.loadProgram(factory, Program.Type.FRAGMENT, string2);
			this.programId = GlProgramManager.createProgram();
			if (this.attributeNames != null) {
				int k = 0;
				for (String string3 : format.getAttributeNames()) {
					GlUniform.bindAttribLocation(this.programId, k, string3);
					this.loadedAttributeIds.add(k);
					++k;
				}
			}
			GlProgramManager.linkProgram(this);
			this.loadReferences();
		}
		catch (Exception exception4) {
			ShaderParseException shaderParseException4 = ShaderParseException.wrap(exception4);
			shaderParseException4.addFaultyFile(identifier.id);
			throw shaderParseException4;
		}
		this.markUniformsDirty();
		this.modelViewMat = this.getUniform("ModelViewMat");
		this.projectionMat = this.getUniform("ProjMat");
		this.viewRotationMat = this.getUniform("IViewRotMat");
		this.textureMat = this.getUniform("TextureMat");
		this.screenSize = this.getUniform("ScreenSize");
		this.colorModulator = this.getUniform("ColorModulator");
		this.light0Direction = this.getUniform("Light0_Direction");
		this.light1Direction = this.getUniform("Light1_Direction");
		this.fogMode = this.getUniform("FogMode");
		this.fogDensity = this.getUniform("FogDensity");
		this.fogStart = this.getUniform("FogStart");
		this.fogEnd = this.getUniform("FogEnd");
		this.fogColor = this.getUniform("FogColor");
		this.fogShape = this.getUniform("FogShape");
		this.lineWidth = this.getUniform("LineWidth");
		this.gameTime = this.getUniform("GameTime");
		this.chunkOffset = this.getUniform("ChunkOffset");
	}

	private static Program loadProgram(ResourceFactory factory, Program.Type type, String name) throws IOException {
		Program program2;
		Program program = type.getProgramCache().get(name);
		if (program == null) {
			String string = CORE_DIRECTORY + name + type.getFileExtension();
			Resource resource = factory.getResourceOrThrow(Identifier.of(string));
			try (InputStream inputStream = resource.getInputStream()) {
				final String string2 = FileNameUtil.getPosixFullPath(string);
				program2 = Program.createFromResource(type, name, inputStream, resource.getResourcePackName(), new GLImportProcessor() {
					private final Set<String> visitedImports = Sets.newHashSet();

					@Override
					public String loadImport(boolean inline, String name) {
						String string;
						name = FileNameUtil.normalizeToPosix((inline ? string2 : Shader.INCLUDE_DIRECTORY) + name);
						if (!this.visitedImports.add(name)) {
							return null;
						}
						Identifier identifier = Identifier.of(name);
						BufferedReader reader;
						try {
							reader = factory.openAsReader(identifier);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							string = IOUtils.toString(reader);
						} catch (Throwable throwable) {
							try {
								if (reader != null) {
									try {
										((Reader) reader).close();
									} catch (Throwable throwable2) {
										throwable.addSuppressed(throwable2);
									}
								}
								throw throwable;
							} catch (IOException iOException) {
								LOGGER.error("Could not open GLSL import {}: {}", name, iOException.getMessage());
								return "#error " + iOException.getMessage();
							}
						}
						try {
							((Reader) reader).close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						return string;
					}
				});
			}
		} else {
			program2 = program;
		}
		return program2;
	}

	public static GlBlendState readBlendState(JsonObject json) {
		if (json == null) {
			return new GlBlendState();
		} else {
			int i = 32774;
			int j = 1;
			int k = 0;
			int l = 1;
			int m = 0;
			boolean bl = true;
			boolean bl2 = false;
			if (JsonHelper.hasString(json, "func")) {
				i = GlBlendState.getFuncFromString(json.get("func").getAsString());
				if (i != 32774) {
					bl = false;
				}
			}

			if (JsonHelper.hasString(json, "srcrgb")) {
				j = GlBlendState.getComponentFromString(json.get("srcrgb").getAsString());
				if (j != 1) {
					bl = false;
				}
			}

			if (JsonHelper.hasString(json, "dstrgb")) {
				k = GlBlendState.getComponentFromString(json.get("dstrgb").getAsString());
				if (k != 0) {
					bl = false;
				}
			}

			if (JsonHelper.hasString(json, "srcalpha")) {
				l = GlBlendState.getComponentFromString(json.get("srcalpha").getAsString());
				if (l != 1) {
					bl = false;
				}

				bl2 = true;
			}

			if (JsonHelper.hasString(json, "dstalpha")) {
				m = GlBlendState.getComponentFromString(json.get("dstalpha").getAsString());
				if (m != 0) {
					bl = false;
				}

				bl2 = true;
			}

			if (bl) {
				return new GlBlendState();
			} else {
				return bl2 ? new GlBlendState(j, k, l, m, i) : new GlBlendState(j, k, i);
			}
		}
	}

	public void close() {

		for (GlUniform glUniform : this.uniforms) {
			glUniform.close();
		}

		GlProgramManager.deleteProgram(this);
	}

	public void unbind() {
		RenderSystem.assertOnRenderThread();
		GlProgramManager.useProgram(0);
		activeShaderId = -1;
		activeShader = null;
		int i = GlStateManager._getActiveTexture();

		for(int j = 0; j < this.loadedSamplerIds.size(); ++j) {
			if (this.samplers.get(this.samplerNames.get(j)) != null) {
				GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + j);
				GlStateManager._bindTexture(0);
			}
		}

		GlStateManager._activeTexture(i);
	}

	public void bind() {
		RenderSystem.assertOnRenderThread();
		this.dirty = false;
		activeShader = this;
		this.blendState.enable();
		if (this.programId != activeShaderId) {
			GlProgramManager.useProgram(this.programId);
			activeShaderId = this.programId;
		}

		int i = GlStateManager._getActiveTexture();

		for(int j = 0; j < this.loadedSamplerIds.size(); ++j) {
			String string = this.samplerNames.get(j);
			if (this.samplers.get(string) != null) {
				int k = GlUniform.getUniformLocation(this.programId, string);
				GlUniform.uniform1(k, j);
				RenderSystem.activeTexture(GlConst.GL_TEXTURE0 + j);
				RenderSystem.enableTexture();
				Object object = this.samplers.get(string);
				int l = -1;
				/*if (object instanceof Framebuffer) {
					l = ((Framebuffer)object).getColorAttachment();
				} else*/ if (object instanceof AbstractTexture) {
					l = ((AbstractTexture)object).getGlId();
				} else if (object instanceof Integer) {
					l = (Integer)object;
				}

				if (l != -1) {
					RenderSystem.bindTexture(l);
				}
			}
		}

		GlStateManager._activeTexture(i);

		for (GlUniform glUniform : this.uniforms) {
			glUniform.upload();
		}

	}

	public void markUniformsDirty() {
		this.dirty = true;
	}

	@Nullable
	public GlUniform getUniform(String name) {
		RenderSystem.assertOnRenderThread();
		return this.loadedUniforms.get(name);
	}

	public Uniform getUniformOrDefault(String name) {
		RenderSystem.assertOnGameThread();
		GlUniform glUniform = this.getUniform(name);
		return glUniform == null ? DEFAULT_UNIFORM : glUniform;
	}

	private void loadReferences() {
		RenderSystem.assertOnRenderThread();
		IntList intList = new IntArrayList();

		int i;
		for(i = 0; i < this.samplerNames.size(); ++i) {
			String string = this.samplerNames.get(i);
			int j = GlUniform.getUniformLocation(this.programId, string);
			if (j == -1) {
				LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", this.name, string);
				this.samplers.remove(string);
				intList.add(i);
			} else {
				this.loadedSamplerIds.add(j);
			}
		}

		for(i = intList.size() - 1; i >= 0; --i) {
			int k = intList.getInt(i);
			this.samplerNames.remove(k);
		}

		for (GlUniform glUniform : this.uniforms) {
			String string2 = glUniform.getName();
			int l = GlUniform.getUniformLocation(this.programId, string2);
			if (l == -1) {
				LOGGER.warn("Shader {} could not find uniform named {} in the specified shader program.", this.name, string2);
			} else {
				this.loadedUniformIds.add(l);
				glUniform.setLocation(l);
				this.loadedUniforms.put(string2, glUniform);
			}
		}

	}

	private void readSampler(JsonElement json) {
		JsonObject jsonObject = JsonHelper.asObject(json, "sampler");
		String string = JsonHelper.getString(jsonObject, "name");
		if (!JsonHelper.hasString(jsonObject, "file")) {
			this.samplers.put(string, null);
			this.samplerNames.add(string);
		} else {
			this.samplerNames.add(string);
		}
	}

	public void addSampler(String name, Object sampler) {
		this.samplers.put(name, sampler);
		this.markUniformsDirty();
	}

	private void addUniform(JsonElement json) throws ShaderParseException {
		JsonObject jsonObject = JsonHelper.asObject(json, "uniform");
		String string = JsonHelper.getString(jsonObject, "name");
		int i = GlUniform.getTypeIndex(JsonHelper.getString(jsonObject, "type"));
		int j = JsonHelper.getInt(jsonObject, "count");
		float[] fs = new float[Math.max(j, 16)];
		JsonArray jsonArray = JsonHelper.getArray(jsonObject, "values");
		if (jsonArray.size() != j && jsonArray.size() > 1) {
			throw new ShaderParseException("Invalid amount of values specified (expected " + j + ", found " + jsonArray.size() + ")");
		} else {
			int k = 0;

			for(Iterator<JsonElement> var9 = jsonArray.iterator(); var9.hasNext(); ++k) {
				JsonElement jsonElement = var9.next();

				try {
					fs[k] = JsonHelper.asFloat(jsonElement, "value");
				} catch (Exception var13) {
					ShaderParseException shaderParseException = ShaderParseException.wrap(var13);
					shaderParseException.addFaultyElement("values[" + k + "]");
					throw shaderParseException;
				}
			}

			if (j > 1 && jsonArray.size() == 1) {
				while(k < j) {
					fs[k] = fs[0];
					++k;
				}
			}

			int l = j > 1 && j <= 4 && i < 8 ? j - 1 : 0;
			GlUniform glUniform = new GlUniform(string, i + l, j, this);
			if (i <= 3) {
				glUniform.setForDataType((int)fs[0], (int)fs[1], (int)fs[2], (int)fs[3]);
			} else if (i <= 7) {
				glUniform.setForDataType(fs[0], fs[1], fs[2], fs[3]);
			} else {
				glUniform.set(Arrays.copyOfRange(fs, 0, j));
			}

			this.uniforms.add(glUniform);
		}
	}

	public Program getVertexShader() {
		return this.vertexShader;
	}

	public Program getFragmentShader() {
		return this.fragmentShader;
	}

	public void attachReferencedShaders() {
		this.fragmentShader.attachTo(this);
		this.vertexShader.attachTo(this);
	}

	public VertexFormat getFormat() {
		return this.format;
	}

	public String getName() {
		return this.name;
	}

	public int getProgramRef() {
		return this.programId;
	}
}
