package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.modificationstation.stationloader.impl.client.texture.TextureRegistry.GUI_ITEMS;
import static net.modificationstation.stationloader.impl.client.texture.TextureRegistry.TERRAIN;

@Mixin(class_556.class)
@Environment(EnvType.CLIENT)
public class Mixinclass_556 {

    @Shadow private Minecraft field_2401;

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0))
    private void bindBlockTexture1(int target, int texture) {
        TERRAIN.bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1))
    private void bindBlockTexture2(int target, int texture) {
        TERRAIN.bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2))
    private void bindItemTexture(int target, int texture) {
        GUI_ITEMS.bindAtlas(field_2401.textureManager, 0);
    }

    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_917(Lnet/minecraft/item/ItemInstance;)I"))
    private int rebindItemTexture(Living living, ItemInstance arg) {
        int texID = living.method_917(arg);
        TextureRegistry.currentRegistry().bindAtlas(field_2401.textureManager, texID / TextureRegistry.currentRegistry().texturesPerFile());
        return texID % TextureRegistry.currentRegistry().texturesPerFile();
    }
}
