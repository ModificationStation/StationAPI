package net.modificationstation.stationloader.api.client.model;

import net.minecraft.client.render.QuadPoint;
import net.modificationstation.stationloader.api.common.util.BlockFaces;

public interface CustomTexturedQuad {

    QuadPoint[] getQuadPoints();

    BlockFaces getSide();

    String getTexture();
}
