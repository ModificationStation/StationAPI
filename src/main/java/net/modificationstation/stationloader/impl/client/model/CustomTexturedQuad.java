package net.modificationstation.stationloader.impl.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import org.lwjgl.opengl.GL11;

public class CustomTexturedQuad {
    public QuadPoint[] quadPoints;
    private boolean mirror;
    private BlockFaces side;
    private String textureName;

    public CustomTexturedQuad(QuadPoint[] args) {
        this.mirror = false;
        this.quadPoints = args;
    }

    @Environment(EnvType.CLIENT)
    public CustomTexturedQuad(QuadPoint[] args, int textureU1, int textureV1, int textureU2, int textureV2, int textureWidth, int textureHeight, BlockFaces side, String textureName) {
        this(args);
        float var6 = 0.0015625F;
        float var7 = 0.003125F;
        args[0] = args[0].method_983((float)textureU2 / textureWidth - var6, (float)textureV1 / textureHeight + var7);
        args[1] = args[1].method_983((float)textureU1 / textureWidth + var6, (float)textureV1 / textureHeight + var7);
        args[2] = args[2].method_983((float)textureU1 / textureWidth + var6, (float)textureV2 / textureHeight - var7);
        args[3] = args[3].method_983((float)textureU2 / textureWidth - var6, (float)textureV2 / textureHeight - var7);
        this.side = side;
        this.textureName = textureName;
    }

    // I dont really know what the purpose of this method is, but it makes the models work properly.
    @Environment(EnvType.CLIENT)
    public void rotateQuads() {
        QuadPoint[] var1 = new QuadPoint[this.quadPoints.length];

        for(int var2 = 0; var2 < this.quadPoints.length; ++var2) {
            var1[var2] = this.quadPoints[this.quadPoints.length - var2 - 1];
        }

        this.quadPoints = var1;
    }

    @Environment(EnvType.CLIENT)
    public void renderQuads(Tessellator arg, float f, String modid) {
        Vec3f var3 = this.quadPoints[1].pointVector.method_1307(this.quadPoints[0].pointVector);
        Vec3f var4 = this.quadPoints[1].pointVector.method_1307(this.quadPoints[2].pointVector);
        Vec3f var5 = var4.method_1309(var3).method_1296();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.getTextureId("/assets/" + modid + "/models/textures/" + textureName));
        arg.start();
        if (this.mirror) {
            arg.method_1697(-((float)var5.x), -((float)var5.y), -((float)var5.z));
        } else {
            arg.method_1697((float)var5.x, (float)var5.y, (float)var5.z);
        }

        for(int var6 = 0; var6 < 4; ++var6) {
            QuadPoint var7 = this.quadPoints[var6];
            arg.vertex((float)var7.pointVector.x * f, (float)var7.pointVector.y * f, (float)var7.pointVector.z * f, var7.field_1147, var7.field_1148);
        }

        arg.draw();
    }

    public QuadPoint[] getQuadPoints() {
        return quadPoints;
    }

    public BlockFaces getSide() {
        return side;
    }
}
