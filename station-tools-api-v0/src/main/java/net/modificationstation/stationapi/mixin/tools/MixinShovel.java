package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.tool.Shovel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Shovel.class)
public abstract class MixinShovel implements net.modificationstation.stationapi.api.item.tool.Shovel {

}
