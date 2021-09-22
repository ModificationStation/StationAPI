package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.awt.image.*;
import java.util.*;

public class StationBlockRenderer {

    public final Set<Atlas> activeAtlases = new HashSet<>();
    public final BlockRendererAccessor blockRendererAccessor;

    public StationBlockRenderer(BlockRenderer tileRenderer) {
        blockRendererAccessor = (BlockRendererAccessor) tileRenderer;
    }

    public void renderActiveAtlases() {
        if (!activeAtlases.isEmpty()) {
            activeAtlases.forEach(atlas -> {
                atlas.bindAtlas();
                Tessellator tessellator = atlas.getTessellator();
                tessellator.draw();
                tessellator.setOffset(0, 0, 0);
            });
            activeAtlases.clear();
            Atlases.getTerrain().bindAtlas();
        }
    }

    public void renderBottomFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getBottomFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                adjustedRenderY = renderY + block.minY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
            tessellator.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            tessellator.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
        }
        tessellator.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
    }

    public void renderTopFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getTopFaceRotation()) {
            case 1:
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                adjustedRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
            tessellator.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            tessellator.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
        }
        tessellator.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
    }

    public void renderEastFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getEastFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                adjustedRenderZ = renderZ + block.minZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(startRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(endRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(endRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(startRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
            tessellator.vertex(endRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            tessellator.vertex(endRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
        }
        tessellator.vertex(startRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
    }

    public void renderWestFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getWestFaceRotation()) {
            case 1:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                adjustedRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(startRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(startRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(endRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(startRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            tessellator.vertex(startRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            tessellator.vertex(endRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
        }
        tessellator.vertex(endRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
    }

    public void renderNorthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getNorthFaceRotation()) {
            case 1:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                adjustedRenderX = renderX + block.minX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(adjustedRenderX, endRenderY, endRenderZ, endU2, startV2);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(adjustedRenderX, endRenderY, startRenderZ, startU1, startV1);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(adjustedRenderX, startRenderY, startRenderZ, startU2, endV2);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(adjustedRenderX, endRenderY, endRenderZ, endU2, startV2);
            tessellator.vertex(adjustedRenderX, endRenderY, startRenderZ, startU1, startV1);
            tessellator.vertex(adjustedRenderX, startRenderY, startRenderZ, startU2, endV2);
        }
        tessellator.vertex(adjustedRenderX, startRenderY, endRenderZ, endU1, endV1);
    }

    public void renderSouthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, boolean renderingInInventory) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator tessellator;
        if (renderingInInventory) {
            tessellator = Tessellator.INSTANCE;
            atlas.bindAtlas();
        } else {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
        }
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getSouthFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.minZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                adjustedRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getField_92()) {
            tessellator.colour(
                    blockRendererAccessor.getField_56(), blockRendererAccessor.getField_60(), blockRendererAccessor.getField_64()
            );
            tessellator.vertex(adjustedRenderX, startRenderY, endRenderZ, startU2, endV2);
            tessellator.colour(
                    blockRendererAccessor.getField_57(), blockRendererAccessor.getField_61(), blockRendererAccessor.getField_65()
            );
            tessellator.vertex(adjustedRenderX, startRenderY, startRenderZ, endU1, endV1);
            tessellator.colour(
                    blockRendererAccessor.getField_58(), blockRendererAccessor.getField_62(), blockRendererAccessor.getField_66()
            );
            tessellator.vertex(adjustedRenderX, endRenderY, startRenderZ, endU2, startV2);
            tessellator.colour(
                    blockRendererAccessor.getField_59(), blockRendererAccessor.getField_63(), blockRendererAccessor.getField_68()
            );
        } else {
            tessellator.vertex(adjustedRenderX, startRenderY, endRenderZ, startU2, endV2);
            tessellator.vertex(adjustedRenderX, startRenderY, startRenderZ, endU1, endV1);
            tessellator.vertex(adjustedRenderX, endRenderY, startRenderZ, endU2, startV2);
        }
        tessellator.vertex(adjustedRenderX, endRenderY, endRenderZ, startU1, startV1);
    }
}
