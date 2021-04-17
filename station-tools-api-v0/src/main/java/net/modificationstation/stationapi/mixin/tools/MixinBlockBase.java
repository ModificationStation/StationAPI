package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockMiningLevel;
import net.modificationstation.stationapi.api.item.tool.Hatchet;
import net.modificationstation.stationapi.api.item.tool.Pickaxe;
import net.modificationstation.stationapi.api.item.tool.Shear;
import net.modificationstation.stationapi.api.item.tool.Shovel;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockMiningLevel {

    @Shadow
    @Final
    public Material material;

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getBlockLevel(int meta, ItemInstance itemInstance) {
        int level = -1;
        if (itemInstance.getType() instanceof Pickaxe) {
            if ((Object) this == BlockBase.OBSIDIAN) {
                level = 3;
            } else if ((Object) this != BlockBase.DIAMOND_BLOCK && (Object) this != BlockBase.DIAMOND_ORE) {
                if ((Object) this != BlockBase.GOLD_BLOCK && (Object) this != BlockBase.GOLD_ORE) {
                    if ((Object) this != BlockBase.IRON_BLOCK && (Object) this != BlockBase.IRON_ORE) {
                        if ((Object) this != BlockBase.LAPIS_LAZULI_BLOCK && (Object) this != BlockBase.LAPIS_LAZULI_ORE) {
                            if ((Object) this != BlockBase.REDSTONE_ORE && (Object) this != BlockBase.REDSTONE_ORE_LIT) {
                                if (this.material == Material.STONE) {
                                    level = 0;
                                } else {
                                    level = this.material == Material.METAL ? 0 : -1;
                                }
                            } else {
                                level = 2;
                            }
                        } else {
                            level = 1;
                        }
                    } else {
                        level = 1;
                    }
                } else {
                    level = 2;
                }
            } else {
                level = 2;
            }
        }
        if (itemInstance.getType() instanceof Hatchet && level == -1) {
            level = this.material == Material.WOOD ? 0 : -1;
        }
        if (itemInstance.getType() instanceof Shear && level == -1) {
            level = this.material == Material.WOOL ? 0 : -1;
        }
        if (itemInstance.getType() instanceof Shovel && level == -1) {
            level = this.material == Material.DIRT || this.material == Material.CLAY || this.material == Material.SAND || this.material == Material.SNOW || this.material == Material.SNOW_BLOCK || this.material == Material.ORGANIC ? 0 : -1;
        }
        return level;
    }

    @Override
    public List<Class<? extends ToolLevel>> getToolTypes(int meta, ItemInstance itemInstance) {
        if (material == Material.STONE || material == Material.METAL) {
            return Collections.singletonList(Pickaxe.class);
        }
        else if (material == Material.WOOD) {
            return Collections.singletonList(Hatchet.class);
        }
        else if (material == Material.WOOL) {
            return Collections.singletonList(Shear.class);
        }
        else if (material == Material.DIRT || material == Material.CLAY || material == Material.SAND || material == Material.SNOW || material == Material.SNOW_BLOCK || material == Material.ORGANIC) {
            return Collections.singletonList(Shovel.class);
        }
        else return null;
    }
}
