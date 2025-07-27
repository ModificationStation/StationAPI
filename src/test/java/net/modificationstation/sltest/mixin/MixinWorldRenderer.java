package net.modificationstation.sltest.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Shadow private TextureManager textureManager;

    @Shadow private World world;

    @Shadow private Minecraft client;

    //@Inject(method = "renderSky(F)V", at = @At("HEAD"), cancellable = true)
    private void endSky(float f, CallbackInfo ci) {
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //RenderHelper.disableLighting();
        GL11.glDepthMask(false);
        this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky6.png"));
        Tessellator var21 = Tessellator.INSTANCE;

        for (int var22 = 0; var22 < 6; ++var22) {
            GL11.glPushMatrix();

            if (var22 == 1) {
                this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky1.png"));
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var22 == 2) {
                this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky4.png"));
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var22 == 3) {
//                this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky2.png"));
//                this.textureManager.bindTexture(this.textureManager.getTextureId(ExpandableTextureAtlas.STATION_GUI_ITEMS.spritesheet));
//                ((CustomAtlasProvider) ItemListener.testItem).getAtlas().of(ItemListener.testItem.getTexturePosition(0)).bindAtlas();
//                Atlases.getStationJsonModels().bindAtlas();
                this.textureManager.bindTexture(StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getGlId());
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var22 == 4) {
                this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky3.png"));
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (var22 == 5) {
                this.textureManager.bindTexture(this.textureManager.getTextureId("/assets/sltest/textures/skybox/sky5.png"));
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            var21.startQuads();
            //var21.colour(2631720);
            //var21.colour(0xffffff);

            /*Vec3f light = level.method_279(client.viewEntity, f);
            float l = (float) ((light.x + light.y + light.z) / 3);
            var21.colour(l, l, l);*/

            float light = world.dimension.lightLevelToLuminance[15 - world.ambientDarkness];
            var21.color(light, light, light);

            if (var22 == 2) {
                var21.vertex(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
                var21.vertex(-100.0D, -100.0D, 100.0D, 0.0D, -1.0D);
                var21.vertex(100.0D, -100.0D, 100.0D, -1.0D, -1.0D);
                var21.vertex(100.0D, -100.0D, -100.0D, -1.0D, 0.0D);
            } else if (var22 == 3 || var22 == 5) {
                var21.vertex(-100.0D, -100.0D, -100.0D, 1.0D, 0.0D);
                var21.vertex(-100.0D, -100.0D, 100.0D, 0.0D, 0.0D);
                var21.vertex(100.0D, -100.0D, 100.0D, 0.0D, 1.0D);
                var21.vertex(100.0D, -100.0D, -100.0D, 1.0D, 1.0D);
            } else if (var22 == 4) {
                var21.vertex(-100.0D, -100.0D, -100.0D, 0.0D, 1.0D);
                var21.vertex(-100.0D, -100.0D, 100.0D, 1.0D, 1.0D);
                var21.vertex(100.0D, -100.0D, 100.0D, 1.0D, 0.0D);
                var21.vertex(100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
            } else {
                var21.vertex(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
                var21.vertex(-100.0D, -100.0D, 100.0D, 0.0D, 1.0D);
                var21.vertex(100.0D, -100.0D, 100.0D, 1.0D, 1.0D);
                var21.vertex(100.0D, -100.0D, -100.0D, 1.0D, 0.0D);
            }
//            var21.pos(-100.0D, -100.0D, -100.0D);
//            var21.pos(-100.0D, -100.0D, 100.0D);
//            var21.pos(100.0D, -100.0D, 100.0D);
//            var21.pos(100.0D, -100.0D, -100.0D);
            var21.draw();
            GL11.glPopMatrix();
        }
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        ci.cancel();
    }
}
