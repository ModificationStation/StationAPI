package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.QuadPoint;
import net.modificationstation.stationapi.api.common.util.BlockFaces;

public interface CustomTexturedQuad {

    QuadPoint[] getQuadPoints();

    BlockFaces getSide();

    String getTexture();
}
