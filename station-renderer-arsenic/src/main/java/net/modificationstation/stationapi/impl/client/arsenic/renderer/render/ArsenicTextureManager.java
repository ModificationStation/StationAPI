package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.class_214;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureTickListener;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.BufferedImage;

import static net.minecraft.client.texture.TextureManager.field_1245;

public final class ArsenicTextureManager {

    private final TextureManager textureManager;
    private final TextureManagerAccessor textureManagerAccessor;

    public ArsenicTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
        textureManagerAccessor = (TextureManagerAccessor) textureManager;
    }

    public void tick() {
        StationTextureManager.get(textureManager).getTickListeners().forEach(TextureTickListener::tick);
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).bindTexture();
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
        for(int var1 = 0; var1 < textureManagerAccessor.getTextureBinders().size(); ++var1) {
            TextureBinder var2 = textureManagerAccessor.getTextureBinders().get(var1);
            var2.render3d = textureManagerAccessor.getGameOptions().anaglyph3d;
            var2.update();
            textureManagerAccessor.getCurrentImageBuffer().clear();
            if (var2.grid.length > textureManagerAccessor.getCurrentImageBuffer().capacity())
                textureManagerAccessor.setCurrentImageBuffer(class_214.method_744(var2.grid.length));
            textureManagerAccessor.getCurrentImageBuffer().put(var2.grid);
            textureManagerAccessor.getCurrentImageBuffer().position(0).limit(var2.grid.length);

            if (var2 instanceof StaticReferenceProvider provider) {
                final Sprite staticReference = provider.getStaticReference().getSprite();
                final SpriteContents contents = staticReference.getContents();
                int scaledWidth = contents.getWidth() / var2.textureSize;
                int scaledHeight = contents.getHeight() / var2.textureSize;
                for (int var3 = 0; var3 < var2.textureSize; ++var3)
                    for (int var4 = 0; var4 < var2.textureSize; ++var4)
                        GL11.glTexSubImage2D(3553, 0, ((int) (staticReference.getMinU() * contents.getWidth() / (staticReference.getMaxU() - staticReference.getMinU()))) + var3 * scaledWidth, ((int) (staticReference.getMinV() * contents.getHeight() / (staticReference.getMaxV() - staticReference.getMinV()))) + var4 * scaledHeight, scaledWidth, scaledHeight, 6408, 5121, textureManagerAccessor.getCurrentImageBuffer());
            } else {
                for (int var3 = 0; var3 < var2.textureSize; ++var3)
                    for (int var4 = 0; var4 < var2.textureSize; ++var4)
                        GL11.glTexSubImage2D(3553, 0, var2.index % 16 * 16 + var3 * 16, var2.index / 16 * 16 + var4 * 16, 16, 16, 6408, 5121, textureManagerAccessor.getCurrentImageBuffer());
            }
        }
    }

    public void getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {
        switch (par1) {
            case "/terrain.png", "/gui/items.png" -> cir.setReturnValue(StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getGlId());
        }
    }

    public void bindImageToId(BufferedImage imageToBind, int targetId) {
        GL11.glBindTexture(3553, targetId);
        if (field_1245) {
            GL11.glTexParameteri(3553, 10241, 9986);
        } else {
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        GL11.glTexParameteri(3553, 10240, 9728);

        if (textureManagerAccessor.stationapi$isBlurTexture()) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }

        if (textureManagerAccessor.stationapi$isClampTexture()) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }

        int var3 = imageToBind.getWidth();
        int var4 = imageToBind.getHeight();
        int[] var5 = new int[var3 * var4];
        byte[] var6 = new byte[var3 * var4 * 4];
        imageToBind.getRGB(0, 0, var3, var4, var5, 0, var3);

        for(int var7 = 0; var7 < var5.length; ++var7) {
            int var8 = var5[var7] >> 24 & 255;
            int var9 = var5[var7] >> 16 & 255;
            int var10 = var5[var7] >> 8 & 255;
            int var11 = var5[var7] & 255;
            if (textureManagerAccessor.getGameOptions() != null && textureManagerAccessor.getGameOptions().anaglyph3d) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            var6[var7 * 4] = (byte)var9;
            var6[var7 * 4 + 1] = (byte)var10;
            var6[var7 * 4 + 2] = (byte)var11;
            var6[var7 * 4 + 3] = (byte)var8;
        }

        textureManagerAccessor.getCurrentImageBuffer().clear();
        if (var6.length > textureManagerAccessor.getCurrentImageBuffer().capacity())
            textureManagerAccessor.setCurrentImageBuffer(class_214.method_744(var6.length));
        textureManagerAccessor.getCurrentImageBuffer().put(var6);
        textureManagerAccessor.getCurrentImageBuffer().position(0).limit(var6.length);
        GL11.glTexImage2D(3553, 0, 6408, var3, var4, 0, 6408, 5121, textureManagerAccessor.getCurrentImageBuffer());
        if (field_1245) {
            for(int var18 = 1; var18 <= 4; ++var18) {
                int var19 = var3 >> var18 - 1;
                int var20 = var3 >> var18;
                int var21 = var4 >> var18;

                for(int var22 = 0; var22 < var20; ++var22) {
                    for(int var23 = 0; var23 < var21; ++var23) {
                        int var24 = textureManagerAccessor.getCurrentImageBuffer().getInt((var22 * 2 + (var23 * 2) * var19) * 4);
                        int var25 = textureManagerAccessor.getCurrentImageBuffer().getInt((var22 * 2 + 1 + (var23 * 2) * var19) * 4);
                        int var15 = textureManagerAccessor.getCurrentImageBuffer().getInt((var22 * 2 + 1 + (var23 * 2 + 1) * var19) * 4);
                        int var16 = textureManagerAccessor.getCurrentImageBuffer().getInt((var22 * 2 + (var23 * 2 + 1) * var19) * 4);
                        int var17 = textureManagerAccessor.stationapi$method_1098(textureManagerAccessor.stationapi$method_1098(var24, var25), textureManagerAccessor.stationapi$method_1098(var15, var16));
                        textureManagerAccessor.getCurrentImageBuffer().putInt((var22 + var23 * var20) * 4, var17);
                    }
                }

                GL11.glTexImage2D(3553, var18, 6408, var20, var21, 0, 6408, 5121, textureManagerAccessor.getCurrentImageBuffer());
            }
        }
    }

    public void bindImageToId(int[] pixels, int width, int height, int targetId) {
        GL11.glBindTexture(3553, targetId);
        if (field_1245) {
            GL11.glTexParameteri(3553, 10241, 9986);
        } else {
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        GL11.glTexParameteri(3553, 10240, 9728);

        if (textureManagerAccessor.stationapi$isBlurTexture()) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }

        if (textureManagerAccessor.stationapi$isClampTexture()) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }

        byte[] var5 = new byte[width * height * 4];

        for(int var6 = 0; var6 < pixels.length; ++var6) {
            int var7 = pixels[var6] >> 24 & 255;
            int var8 = pixels[var6] >> 16 & 255;
            int var9 = pixels[var6] >> 8 & 255;
            int var10 = pixels[var6] & 255;
            if (textureManagerAccessor.getGameOptions() != null && textureManagerAccessor.getGameOptions().anaglyph3d) {
                int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
                int var12 = (var8 * 30 + var9 * 70) / 100;
                int var13 = (var8 * 30 + var10 * 70) / 100;
                var8 = var11;
                var9 = var12;
                var10 = var13;
            }

            var5[var6 * 4] = (byte)var8;
            var5[var6 * 4 + 1] = (byte)var9;
            var5[var6 * 4 + 2] = (byte)var10;
            var5[var6 * 4 + 3] = (byte)var7;
        }

        textureManagerAccessor.getCurrentImageBuffer().clear();
        if (var5.length > textureManagerAccessor.getCurrentImageBuffer().capacity())
            textureManagerAccessor.setCurrentImageBuffer(class_214.method_744(var5.length));
        textureManagerAccessor.getCurrentImageBuffer().put(var5);
        textureManagerAccessor.getCurrentImageBuffer().position(0).limit(var5.length);
        GL11.glTexSubImage2D(3553, 0, 0, 0, width, height, 6408, 5121, textureManagerAccessor.getCurrentImageBuffer());
    }
}
