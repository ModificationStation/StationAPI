package net.modificationstation.stationapi.mixin.sortme.common;

import net.minecraft.item.tool.Hatchet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hatchet.class)
public abstract class MixinHatchet implements net.modificationstation.stationapi.api.common.item.tool.Hatchet {

}
