package net.modificationstation.stationloader.mixin.common;

import net.minecraft.item.tool.Hatchet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hatchet.class)
public abstract class MixinHatchet implements net.modificationstation.stationloader.api.common.item.tool.Hatchet {

}
