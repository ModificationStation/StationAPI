package net.modificationstation.stationapi.mixin.flattening.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;

@Mixin(InGameHud.class)
abstract class InGameHudMixin extends DrawContext {
    @Shadow private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Runtime;maxMemory()J",
                    shift = Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void stationapi_renderHud(float bl, boolean i, int j, int par4, CallbackInfo ci, class_564 scaler, int var6, int var7, TextRenderer var8) {
        HitResult hit = minecraft.field_2823;
        int offset = 22;
        if (hit != null && hit.type == HitResultType.BLOCK) {
            BlockState state = minecraft.world.getBlockState(hit.blockX, hit.blockY, hit.blockZ);

            String text = "Block: " + state.getBlock().getTranslatedName();
            drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 16777215);

            text = "Meta: " + minecraft.world.getBlockMeta(hit.blockX, hit.blockY, hit.blockZ);
            drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 16777215);

            Collection<Property<?>> properties = state.getProperties();
            if (!properties.isEmpty()) {
                text = "Properties:";
                drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 16777215);

                for (Property<?> property : properties) {
                    text = property.getName() + ": " + state.get(property);
                    drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 14737632);
                }
            }

            Collection<TagKey<Block>> tags = state.streamTags().toList();
            if (!tags.isEmpty()) {
                text = "Tags:";
                drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 16777215);

                for (TagKey<Block> property : tags) {
                    text = "#" + property.id();
                    drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset += 10, 14737632);
                }
            }

            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                BlockEntity entity = minecraft.world.method_1777(hit.blockX, hit.blockY, hit.blockZ);
                if (entity != null) {
                    String className = entity.getClass().getName();
                    text = "Tile Entity: " + className.substring(className.lastIndexOf('.') + 1);
                    drawTextWithShadow(var8, text, var6 - var8.getWidth(text) - 2, offset + 20, 16777215);
                }
            }
        }
    }
}
