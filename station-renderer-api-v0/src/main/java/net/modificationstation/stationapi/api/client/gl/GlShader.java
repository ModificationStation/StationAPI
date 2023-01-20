package net.modificationstation.stationapi.api.client.gl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GlShader {
	int getProgramRef();

	void markUniformsDirty();

	Program getVertexShader();

	Program getFragmentShader();

	void attachReferencedShaders();
}
