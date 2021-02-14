package net.modificationstation.stationapi.impl.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.util.BlockFaces;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class CustomCuboidRenderer implements net.modificationstation.stationapi.api.client.model.CustomCuboidRenderer {

    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float pitch;
    public float yaw;
    public float roll;
    public boolean mirror = false;
    public boolean visible = true;
    public boolean skipRendering = false;
    public String modid;
    private QuadPoint[] quadPoints;
    private CustomTexturedQuad[] cubeQuads;
    private final JsonFaces uvs;
    private boolean compiled = false;
    private int list = 0;

    public CustomCuboidRenderer(JsonFaces uvs, String modid) {
        this.uvs = uvs;
        this.modid = modid;
    }

    public static String getTexture(HashMap<String, String> textures, String textureName) {
        try {
            if (textureName.startsWith("#missing")) {
                return null;
            } else {
                String[] tex = textures.get(textureName.substring(1)).split("/");
                return tex[tex.length - 1];
            }
        } catch (Exception e) {
            StationAPI.LOGGER.error("Unable to get valid texture for side!");
            e.printStackTrace();
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public void setupCuboid(float f, float f1, float f2, int i, int j, int k, float f3, HashMap<String, String> textures) {
        this.quadPoints = new QuadPoint[8];
        this.cubeQuads = new CustomTexturedQuad[6];
        float var8 = f + (float) i;
        float var9 = f1 + (float) j;
        float var10 = f2 + (float) k;
        f -= f3;
        f1 -= f3;
        f2 -= f3;
        var8 += f3;
        var9 += f3;
        var10 += f3;
        if (this.mirror) {
            float var11 = var8;
            var8 = f;
            f = var11;
        }

        QuadPoint var20 = new QuadPoint(f, f1, f2, 0.0F, 0.0F);
        QuadPoint var12 = new QuadPoint(var8, f1, f2, 0.0F, 8.0F);
        QuadPoint var13 = new QuadPoint(var8, var9, f2, 8.0F, 8.0F);
        QuadPoint var14 = new QuadPoint(f, var9, f2, 8.0F, 0.0F);
        QuadPoint var15 = new QuadPoint(f, f1, var10, 0.0F, 0.0F);
        QuadPoint var16 = new QuadPoint(var8, f1, var10, 0.0F, 8.0F);
        QuadPoint var17 = new QuadPoint(var8, var9, var10, 8.0F, 8.0F);
        QuadPoint var18 = new QuadPoint(f, var9, var10, 8.0F, 0.0F);
        this.quadPoints[0] = var20;
        this.quadPoints[1] = var12;
        this.quadPoints[2] = var13;
        this.quadPoints[3] = var14;
        this.quadPoints[4] = var15;
        this.quadPoints[5] = var16;
        this.quadPoints[6] = var17;
        this.quadPoints[7] = var18;

        // East
        this.cubeQuads[0] = new CustomTexturedQuad(new QuadPoint[]{var16, var12, var13, var17}, (int) (uvs.getEast().getUv()[2]), (int) (uvs.getEast().getUv()[3]), (int) (uvs.getEast().getUv()[0]), (int) (uvs.getEast().getUv()[1]), 16, 16, BlockFaces.EAST, getTexture(textures, uvs.getEast().getTexture()));
        // West
        this.cubeQuads[1] = new CustomTexturedQuad(new QuadPoint[]{var20, var15, var18, var14}, (int) (uvs.getWest().getUv()[2]), (int) (uvs.getWest().getUv()[3]), (int) (uvs.getWest().getUv()[0]), (int) (uvs.getWest().getUv()[1]), 16, 16, BlockFaces.WEST, getTexture(textures, uvs.getWest().getTexture()));
        // Up
        this.cubeQuads[2] = new CustomTexturedQuad(new QuadPoint[]{var16, var15, var20, var12}, (int) (uvs.getDown().getUv()[0]), (int) (uvs.getDown().getUv()[1]), (int) (uvs.getDown().getUv()[2]), (int) (uvs.getDown().getUv()[3]), 16, 16, BlockFaces.DOWN, getTexture(textures, uvs.getDown().getTexture()));
        // Down
        this.cubeQuads[3] = new CustomTexturedQuad(new QuadPoint[]{var13, var14, var18, var17}, (int) (uvs.getUp().getUv()[0]), (int) (uvs.getUp().getUv()[1]), (int) (uvs.getUp().getUv()[2]), (int) (uvs.getUp().getUv()[3]), 16, 16, BlockFaces.UP, getTexture(textures, uvs.getUp().getTexture()));
        // North
        this.cubeQuads[4] = new CustomTexturedQuad(new QuadPoint[]{var12, var20, var14, var13}, (int) (uvs.getNorth().getUv()[2]), (int) (uvs.getNorth().getUv()[3]), (int) (uvs.getNorth().getUv()[0]), (int) (uvs.getNorth().getUv()[1]), 16, 16, BlockFaces.NORTH, getTexture(textures, uvs.getNorth().getTexture()));
        // South
        this.cubeQuads[5] = new CustomTexturedQuad(new QuadPoint[]{var15, var16, var17, var18}, (int) (uvs.getSouth().getUv()[2]), (int) (uvs.getSouth().getUv()[3]), (int) (uvs.getSouth().getUv()[0]), (int) (uvs.getSouth().getUv()[1]), 16, 16, BlockFaces.SOUTH, getTexture(textures, uvs.getSouth().getTexture()));

        if (this.mirror) {
            for (CustomTexturedQuad texturedQuad : this.cubeQuads) {
                texturedQuad.rotateQuads();
            }
        }

    }

    public void setRotationPoint(float f, float f1, float f2) {
        this.rotationPointX = f;
        this.rotationPointY = f1;
        this.rotationPointZ = f2;
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

    @Override
    public String getModID() {
        return modid;
    }
}
