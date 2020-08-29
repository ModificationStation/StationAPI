package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.mixin.client.accessor.TessellatorAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TileRenderer.class)
public class MixinTileRenderer {

    @SuppressWarnings("InvalidMemberReference")
    @ModifyVariable(method = {
            "method_46(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_55(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_61(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_65(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_67(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_69(Lnet/minecraft/block/BlockBase;DDDI)V"
    }, index = 8, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_45(Lnet/minecraft/block/BlockBase;DDDDD)V", index = 13, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getTorchTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_47(Lnet/minecraft/block/BlockBase;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getCrossedTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_56(Lnet/minecraft/block/BlockBase;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0, shift = At.Shift.BEFORE))
    private int getCropsTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_70(Lnet/minecraft/block/BlockBase;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;method_1604(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getFireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_71(Lnet/minecraft/block/BlockBase;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;method_1604(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getRedstoneWireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_72(Lnet/minecraft/block/BlockBase;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;method_1604(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getLadderTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_44(Lnet/minecraft/block/Rail;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Rail;method_1108()Z", shift = At.Shift.BEFORE))
    private int getRailTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_75(Lnet/minecraft/block/BlockBase;III)Z", index = 28, at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 2, shift = At.Shift.BEFORE))
    private int getFluidTextureID(int texID) {
        return overrideTexture(texID);
    }

    @Redirect(method = "method_75(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I"))
    private int getFluidTextureID(BlockBase blockBase, int side, int meta) {
        return overrideTexture(blockBase.getTextureForSide(side, meta));
    }

    @Redirect(method = "method_81(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;method_1626(Lnet/minecraft/level/TileView;IIII)I", ordinal = 0))
    private int getBedTextureID1(BlockBase blockBase, TileView arg, int i, int j, int k, int i1) {
        return overrideTexture(blockBase.method_1626(arg, i, j, k, i1));
    }

    @Redirect(method = "method_81(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;method_1626(Lnet/minecraft/level/TileView;IIII)I", ordinal = 1))
    private int getBedTextureID2(BlockBase blockBase, TileView arg, int i, int j, int k, int i1) {
        return overrideTexture(blockBase.method_1626(arg, i, j, k, i1));
    }

    private int overrideTexture(int texID) {
        if (TextureRegistry.currentRegistry() != null) {
            int atlasID = texID / TextureRegistry.currentRegistry().texturesPerFile();
            if (TextureRegistry.currentRegistry().currentTexture() != atlasID) {
                Tessellator tessellator = Tessellator.INSTANCE;
                boolean hasColor = false;
                if (!inventory) {
                    hasColor = ((TessellatorAccessor) tessellator).getField_2065();
                    tessellator.draw();
                }
                TextureRegistry.currentRegistry().bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, atlasID);
                if (!inventory) {
                    tessellator.start();
                    ((TessellatorAccessor) tessellator).setField_2065(hasColor);
                }
            }
            return texID % TextureRegistry.currentRegistry().texturesPerFile();
        } else
            return texID;
    }

    @Inject(method = "method_48(Lnet/minecraft/block/BlockBase;IF)V", at = @At("HEAD"))
    private void onRenderBlockInInventory(CallbackInfo ci) {
        inventory = true;
    }

    @Inject(method = "method_48(Lnet/minecraft/block/BlockBase;IF)V", at = @At("RETURN"))
    private void afterRenderBlockInInventory(CallbackInfo ci) {
        inventory = false;
    }

    private boolean inventory;
}
