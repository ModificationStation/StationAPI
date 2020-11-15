package net.modificationstation.stationloader.mixin.common;

import net.minecraft.item.tool.Shovel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Shovel.class)
public abstract class MixinShovel implements net.modificationstation.stationloader.api.common.item.tool.Shovel {

}
