package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.common.block.BlockManager;
import net.modificationstation.stationloader.api.common.block.BlockMiningLevel;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.block.BlockStrengthPerMeta;
import net.modificationstation.stationloader.api.common.entity.player.StrengthOnMeta;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
import net.modificationstation.stationloader.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationloader.api.common.item.tool.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockStrengthPerMeta, BlockMiningLevel {

    @Shadow
    @Final
    public static BlockBase[] BY_ID;
    @Shadow
    @Final
    public Material material;
    @Shadow
    protected float hardness;

    public MixinBlockBase(int i, Material material) {
    }

    public MixinBlockBase(int i, int j, Material material) {
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(II)Lnet/minecraft/block/Stone;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeBlockRegister(CallbackInfo ci) {
        ModelRegister.EVENT.getInvoker().registerModels(ModelRegister.Type.BLOCKS);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void afterBlockRegister(CallbackInfo ci) {
        GeneralFactory.INSTANCE.addFactory(BlockBase.class, (args) ->
                args[0] instanceof Integer ?
                        args[1] instanceof Material ?
                                BlockBase.class.cast(new MixinBlockBase((int) args[0], (Material) args[1])) :
                                args[1] instanceof Integer && args[2] instanceof Material ?
                                        BlockBase.class.cast(new MixinBlockBase((int) args[0], (int) args[1], (Material) args[2])) :
                                        null :
                        null
        );
        BlockRegister.EVENT.getInvoker().registerBlocks(BlockRegistry.INSTANCE, BlockRegister.EVENT.getInvokerModID());
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/item/PlaceableTileEntity;"))
    private static PlaceableTileEntity getBlockItem(int blockID) {
        return BlockManager.INSTANCE.getBlockItem(BY_ID[blockID + BY_ID.length]);
    }

    @Shadow
    public String getName() {
        return null;
    }

    @Shadow
    public float getHardness() {
        return 0;
    }

    @Shadow public double maxX;

    @ModifyVariable(method = "setName(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;", at = @At("HEAD"))
    private String getName(String name) {
        return BlockNameSet.EVENT.getInvoker().getName((BlockBase) (Object) this, name);
    }

    @Override
    public float getBlockStrength(PlayerBase player, int meta) {
        float ret = getHardness(meta);
        if (ret < 0.0F)
            return 0.0F;
        return material.doesRequireTool() || player.getHeldItem() != null && ((EffectiveOnMeta) player.getHeldItem().getType()).isEffectiveOn((BlockBase) (Object) this, meta, player.getHeldItem()) ? ((StrengthOnMeta) player).getStrengh((BlockBase) (Object) this, meta) / ret / 30F : 1F / ret / 100F;
    }

    @Override
    public float getHardness(int meta) {
        return getHardness();
    }

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
