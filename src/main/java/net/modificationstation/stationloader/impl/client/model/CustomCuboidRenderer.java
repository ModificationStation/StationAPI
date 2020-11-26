package net.modificationstation.stationloader.impl.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class CustomCuboidRenderer implements net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer {

    private QuadPoint[] quadPoints;
    private CustomTexturedQuad[] cubeQuads;
    private JsonFaces uvs;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float pitch;
    public float yaw;
    public float roll;
    private boolean compiled = false;
    private int list = 0;
    public boolean mirror = false;
    public boolean visible = true;
    public boolean skipRendering = false;
    public String modid;

    public CustomCuboidRenderer(JsonFaces uvs, String modid) {
        this.uvs = uvs;
        this.modid = modid;
    }

    @Environment(EnvType.CLIENT)
    public void setupCuboid(float x, float y, float z, int width, int height, int length, float extend, HashMap<String, String> textures) {
        this.quadPoints = new QuadPoint[8];
        this.cubeQuads = new CustomTexturedQuad[6];
        float x2 = x + (float)width;
        float y2 = y + (float)height;
        float z2 = z + (float)length;
        x -= extend;
        y -= extend;
        z -= extend;
        x2 += extend;
        y2 += extend;
        z2 += extend;
        if (this.mirror) {
            float var11 = x2;
            x2 = x;
            x = var11;
        }

        QuadPoint point0 = new QuadPoint(x, y, z, 0.0F, 0.0F);
        QuadPoint point1 = new QuadPoint(x2, y, z, 0.0F, 8.0F);
        QuadPoint point2 = new QuadPoint(x2, y2, z, 8.0F, 8.0F);
        QuadPoint point3 = new QuadPoint(x, y2, z, 8.0F, 0.0F);
        QuadPoint point4 = new QuadPoint(x, y, z2, 0.0F, 0.0F);
        QuadPoint point5 = new QuadPoint(x2, y, z2, 0.0F, 8.0F);
        QuadPoint point6 = new QuadPoint(x2, y2, z2, 8.0F, 8.0F);
        QuadPoint point7 = new QuadPoint(x, y2, z2, 8.0F, 0.0F);
        this.quadPoints[0] = point0;
        this.quadPoints[1] = point1;
        this.quadPoints[2] = point2;
        this.quadPoints[3] = point3;
        this.quadPoints[4] = point4;
        this.quadPoints[5] = point5;
        this.quadPoints[6] = point6;
        this.quadPoints[7] = point7;

        // East
        this.cubeQuads[0] = new CustomTexturedQuad(new QuadPoint[]{point5, point1, point2, point6}, (int) (uvs.getEast().getUv()[2]), (int) (uvs.getEast().getUv()[3]), (int) (uvs.getEast().getUv()[0]), (int) (uvs.getEast().getUv()[1]) , 16, 16, BlockFaces.EAST, getTexture(textures, uvs.getEast().getTexture()));
        // West
        this.cubeQuads[1] = new CustomTexturedQuad(new QuadPoint[]{point0, point4, point7, point3}, (int) (uvs.getWest().getUv()[2]), (int) (uvs.getWest().getUv()[3]), (int) (uvs.getWest().getUv()[0]), (int) (uvs.getWest().getUv()[1]), 16, 16, BlockFaces.WEST, getTexture(textures, uvs.getWest().getTexture()));
        // Up
        this.cubeQuads[2] = new CustomTexturedQuad(new QuadPoint[]{point5, point4, point0, point1}, (int) (uvs.getDown().getUv()[0]), (int) (uvs.getDown().getUv()[1]), (int) (uvs.getDown().getUv()[2]), (int) (uvs.getDown().getUv()[3]), 16, 16, BlockFaces.DOWN, getTexture(textures, uvs.getDown().getTexture()));
        // Down
        this.cubeQuads[3] = new CustomTexturedQuad(new QuadPoint[]{point2, point3, point7, point6}, (int) (uvs.getUp().getUv()[0]), (int) (uvs.getUp().getUv()[1]), (int) (uvs.getUp().getUv()[2]), (int) (uvs.getUp().getUv()[3]), 16, 16, BlockFaces.UP, getTexture(textures, uvs.getUp().getTexture()));
        // North
        this.cubeQuads[4] = new CustomTexturedQuad(new QuadPoint[]{point1, point0, point3, point2}, (int) (uvs.getNorth().getUv()[2]), (int) (uvs.getNorth().getUv()[3]), (int) (uvs.getNorth().getUv()[0]), (int) (uvs.getNorth().getUv()[1]), 16, 16, BlockFaces.NORTH, getTexture(textures, uvs.getNorth().getTexture()));
        // South
        this.cubeQuads[5] = new CustomTexturedQuad(new QuadPoint[]{point4, point5, point6, point7}, (int) (uvs.getSouth().getUv()[2]), (int) (uvs.getSouth().getUv()[3]), (int) (uvs.getSouth().getUv()[0]), (int) (uvs.getSouth().getUv()[1]), 16, 16, BlockFaces.SOUTH, getTexture(textures, uvs.getSouth().getTexture()));

        if (this.mirror) {
            for (CustomTexturedQuad texturedQuad : this.cubeQuads) {
                texturedQuad.rotateQuads();
            }
        }

    }

    public static String getTexture(HashMap<String, String> textures, String textureName) {
        try {
            if (textureName.startsWith("#missing")) {
                return null;
            }
            else {
                String[] tex = textures.get(textureName.substring(1)).split("/");
                return tex[tex.length-1];
            }
        } catch (Exception e) {
            StationLoader.INSTANCE.getLogger().error("Unable to get valid texture for side!");
            e.printStackTrace();
            return null;
        }
    }

    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    @Environment(EnvType.CLIENT)
    public void renderEntityModelCube(float f) {
        if (!this.skipRendering) {
            if (this.visible) {
                if (!this.compiled) {
                    this.compileEntityModel(f);
                }

                if (this.pitch == 0.0F && this.yaw == 0.0F && this.roll == 0.0F) {
                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                        GL11.glCallList(this.list);
                    } else {
                        GL11.glTranslatef(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                        GL11.glCallList(this.list);
                        GL11.glTranslatef(-this.rotationPointX * f, -this.rotationPointY * f, -this.rotationPointZ * f);
                    }
                } else {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                    if (this.roll != 0.0F) {
                        GL11.glRotatef(this.roll * 57.295776F, 0.0F, 0.0F, 1.0F);
                    }

                    if (this.yaw != 0.0F) {
                        GL11.glRotatef(this.yaw * 57.295776F, 0.0F, 1.0F, 0.0F);
                    }

                    if (this.pitch != 0.0F) {
                        GL11.glRotatef(this.pitch * 57.295776F, 1.0F, 0.0F, 0.0F);
                    }

                    GL11.glCallList(this.list);
                    GL11.glPopMatrix();
                }

            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void compileEntityModel(float f) {
        this.list = class_214.method_741(1);
        GL11.glNewList(this.list, 4864);
        Tessellator var2 = Tessellator.INSTANCE;

        for (CustomTexturedQuad texturedQuad : this.cubeQuads) {
            texturedQuad.renderQuads(var2, f, modid);
        }

        GL11.glEndList();
        this.compiled = true;
    }

    @Override
    public CustomTexturedQuad[] getCubeQuads() {
        return cubeQuads;
    }

    public String getModID() {
        return modid;
    }
}
