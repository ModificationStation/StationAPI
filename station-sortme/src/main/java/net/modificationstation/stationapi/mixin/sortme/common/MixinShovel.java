package net.modificationstation.stationapi.mixin.sortme.common;

import net.minecraft.item.tool.Shovel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Shovel.class)
public abstract class MixinShovel implements net.modificationstation.stationapi.api.common.item.tool.Shovel {

}
