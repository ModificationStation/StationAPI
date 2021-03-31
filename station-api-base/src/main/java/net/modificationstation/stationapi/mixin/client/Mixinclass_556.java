package net.modificationstation.stationapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_556.class)
@Environment(EnvType.CLIENT)
public class Mixinclass_556 {

    @Shadow
    private Minecraft field_2401;

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0, remap = false))
    private void bindBlockTexture1(int target, int texture) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1, remap = false))
    private void bindBlockTexture2(int target, int texture) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2, remap = false))
    private void bindItemTexture(int target, int texture) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.GUI_ITEMS).bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_917(Lnet/minecraft/item/ItemInstance;)I"))
    private int rebindItemTexture(Living living, ItemInstance arg) {
        int texID = living.method_917(arg);
        TextureRegistry.currentRegistry().bindAtlas(field_2401.textureManager, texID / TextureRegistry.currentRegistry().texturesPerFile());
        return texID % TextureRegistry.currentRegistry().texturesPerFile();
    }
}
