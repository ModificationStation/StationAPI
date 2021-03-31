package net.modificationstation.stationapi.mixin.common;

import net.minecraft.item.tool.Pickaxe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Pickaxe.class)
public abstract class MixinPickaxe implements net.modificationstation.stationapi.api.common.item.tool.Pickaxe {

}
