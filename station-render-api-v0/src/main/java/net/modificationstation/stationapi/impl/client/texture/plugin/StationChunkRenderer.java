package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.block.BlockBase;
import net.minecraft.class_66;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.TileEntityRenderDispatcher;
import net.minecraft.level.WorldPopulationRegion;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPluginProvider;
import net.modificationstation.stationapi.api.client.texture.plugin.MeshRendererPlugin;
import net.modificationstation.stationapi.mixin.render.client.class_66Accessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

final class StationChunkRenderer extends MeshRendererPlugin {

    private final class_66Accessor meshRendererAccessor;

    StationChunkRenderer(class_66 meshRenderer) {
        super(meshRenderer);
        meshRendererAccessor = (class_66Accessor) meshRenderer;
    }

    @Override
    public void buildMesh(CallbackInfo ci) {
        if (meshRenderer.field_249) {
            ++class_66.field_230;
            int var1 = meshRenderer.field_231;
            int var2 = meshRenderer.field_232;
            int var3 = meshRenderer.field_233;
            int var4 = meshRenderer.field_231 + meshRenderer.field_234;
            int var5 = meshRenderer.field_232 + meshRenderer.field_235;
            int var6 = meshRenderer.field_233 + meshRenderer.field_236;

            for(int var7 = 0; var7 < 2; ++var7) {
                meshRenderer.field_244[var7] = true;
            }

            Chunk.field_953 = false;
            //noinspection unchecked,rawtypes
            Set<TileEntityBase> var21 = new HashSet<>(meshRenderer.field_224);
            meshRenderer.field_224.clear();
            byte var8 = 1;
            WorldPopulationRegion var9 = new WorldPopulationRegion(meshRenderer.level, var1 - var8, var2 - var8, var3 - var8, var4 + var8, var5 + var8, var6 + var8);
            BlockRenderer var10 = new BlockRenderer(var9);
            StationBlockRenderer plugin = (StationBlockRenderer) ((BlockRendererPluginProvider) var10).getPlugin();

            //noinspection ConstantConditions notch code broke intellij
            for(int var11 = 0; var11 < 2; ++var11) {
                boolean var12 = false;
                boolean var13 = false;
                boolean var14 = false;

                for(int var15 = var2; var15 < var5; ++var15) {
                    for(int var16 = var3; var16 < var6; ++var16) {
                        for(int var17 = var1; var17 < var4; ++var17) {
                            int var18 = var9.getTileId(var17, var15, var16);
                            if (var18 > 0) {
                                //noinspection ConstantConditions notch code broke intellij
                                if (!var14) {
                                    var14 = true;
                                    GL11.glNewList(meshRendererAccessor.getField_225() + var11, 4864);
                                    GL11.glPushMatrix();
                                    meshRendererAccessor.invokeMethod_306();
                                    float var19 = 1.000001F;
                                    GL11.glTranslatef((float)(-meshRenderer.field_236) / 2.0F, (float)(-meshRenderer.field_235) / 2.0F, (float)(-meshRenderer.field_236) / 2.0F);
                                    GL11.glScalef(var19, var19, var19);
                                    GL11.glTranslatef((float)meshRenderer.field_236 / 2.0F, (float)meshRenderer.field_235 / 2.0F, (float)meshRenderer.field_236 / 2.0F);
                                    plugin.startMeshRender();
                                    class_66Accessor.getTesselator().start();
                                    class_66Accessor.getTesselator().setOffset(-meshRenderer.field_231, -meshRenderer.field_232, -meshRenderer.field_233);
                                }

                                if (var11 == 0 && BlockBase.HAS_TILE_ENTITY[var18]) {
                                    TileEntityBase var23 = var9.getTileEntity(var17, var15, var16);
                                    if (TileEntityRenderDispatcher.INSTANCE.hasCustomRenderer(var23)) {
                                        //noinspection unchecked
                                        meshRenderer.field_224.add(var23);
                                    }
                                }

                                BlockBase var24 = BlockBase.BY_ID[var18];
                                int var20 = var24.getRenderPass();
                                if (var20 != var11) {
                                    var12 = true;
                                } else if (var20 == var11) {
                                    var13 |= var10.render(var24, var17, var15, var16);
                                }
                            }
                        }
                    }
                }

                //noinspection ConstantConditions notch code broke intellij
                if (var14) {
                    plugin.endMeshRender();
                    class_66Accessor.getTesselator().draw();
                    GL11.glPopMatrix();
                    GL11.glEndList();
                    class_66Accessor.getTesselator().setOffset(0.0D, 0.0D, 0.0D);
                } else {
                    //noinspection ConstantConditions notch code broke intellij
                    var13 = false;
                }

                //noinspection ConstantConditions notch code broke intellij
                if (var13) {
                    meshRenderer.field_244[var11] = false;
                }

                //noinspection ConstantConditions notch code broke intellij
                if (!var12) {
                    break;
                }
            }

            //noinspection unchecked,rawtypes
            HashSet<TileEntityBase> var22 = new HashSet<>(meshRenderer.field_224);
            var22.removeAll(var21);
            meshRendererAccessor.getField_228().addAll(var22);
            // noinspection unchecked,SuspiciousMethodCalls
            meshRenderer.field_224.forEach(var21::remove);
            meshRendererAccessor.getField_228().removeAll(var21);
            meshRenderer.field_223 = Chunk.field_953;
            meshRendererAccessor.setField_227(true);
        }
        ci.cancel();
    }
}
