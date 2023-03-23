package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZippedTexturePack.class)
public class MixinZippedTexturePack extends TexturePack {

}
