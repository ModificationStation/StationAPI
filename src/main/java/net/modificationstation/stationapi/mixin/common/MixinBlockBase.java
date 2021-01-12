package net.modificationstation.stationapi.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.client.event.model.ModelRegister;
import net.modificationstation.stationapi.api.common.block.BlockManager;
import net.modificationstation.stationapi.api.common.block.BlockMiningLevel;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.block.BlockStrengthPerMeta;
import net.modificationstation.stationapi.api.common.entity.player.StrengthOnMeta;
import net.modificationstation.stationapi.api.common.event.block.BlockNameSet;
import net.modificationstation.stationapi.api.common.event.block.BlockRegister;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationapi.api.common.item.tool.*;
import net.modificationstation.stationapi.api.common.registry.Identifier;
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
        BlockRegistry blocks = BlockRegistry.INSTANCE;
        blocks.registerValue(Identifier.of("stone"), BlockBase.STONE);
        blocks.registerValue(Identifier.of("grass_block"), BlockBase.GRASS);
        blocks.registerValue(Identifier.of("dirt"), BlockBase.DIRT);
        blocks.registerValue(Identifier.of("cobblestone"), BlockBase.COBBLESTONE);
        blocks.registerValue(Identifier.of("planks"), BlockBase.WOOD);
        blocks.registerValue(Identifier.of("sapling"), BlockBase.SAPLING);
        blocks.registerValue(Identifier.of("bedrock"), BlockBase.BEDROCK);
        blocks.registerValue(Identifier.of("flowing_water"), BlockBase.FLOWING_WATER);
        blocks.registerValue(Identifier.of("water"), BlockBase.STILL_WATER);
        blocks.registerValue(Identifier.of("flowing_lava"), BlockBase.FLOWING_LAVA);
        blocks.registerValue(Identifier.of("lava"), BlockBase.STILL_LAVA);
        blocks.registerValue(Identifier.of("sand"), BlockBase.SAND);
        blocks.registerValue(Identifier.of("gravel"), BlockBase.GRAVEL);
        blocks.registerValue(Identifier.of("gold_ore"), BlockBase.GOLD_ORE);
        blocks.registerValue(Identifier.of("iron_ore"), BlockBase.IRON_ORE);
        blocks.registerValue(Identifier.of("coal_ore"), BlockBase.COAL_ORE);
        blocks.registerValue(Identifier.of("log"), BlockBase.LOG);
        blocks.registerValue(Identifier.of("leaves"), BlockBase.LEAVES);
        blocks.registerValue(Identifier.of("sponge"), BlockBase.SPONGE);
        blocks.registerValue(Identifier.of("glass"), BlockBase.GLASS);
        blocks.registerValue(Identifier.of("lapis_ore"), BlockBase.LAPIS_LAZULI_ORE);
        blocks.registerValue(Identifier.of("lapis_block"), BlockBase.LAPIS_LAZULI_BLOCK);
        blocks.registerValue(Identifier.of("dispenser"), BlockBase.DISPENSER);
        blocks.registerValue(Identifier.of("sandstone"), BlockBase.SANDSTONE);
        blocks.registerValue(Identifier.of("note_block"), BlockBase.NOTEBLOCK);
        blocks.registerValue(Identifier.of("bed"), BlockBase.BED);
        blocks.registerValue(Identifier.of("powered_rail"), BlockBase.GOLDEN_RAIL);
        blocks.registerValue(Identifier.of("detector_rail"), BlockBase.DETECTOR_RAIL);
        blocks.registerValue(Identifier.of("sticky_piston"), BlockBase.STICKY_PISTON);
        blocks.registerValue(Identifier.of("cobweb"), BlockBase.COBWEB);
        blocks.registerValue(Identifier.of("grass"), BlockBase.TALLGRASS);
        blocks.registerValue(Identifier.of("dead_bush"), BlockBase.DEADBUSH);
        blocks.registerValue(Identifier.of("piston"), BlockBase.PISTON);
        blocks.registerValue(Identifier.of("piston_head"), BlockBase.PISTON_HEAD);
        blocks.registerValue(Identifier.of("wool"), BlockBase.WOOL);
        blocks.registerValue(Identifier.of("moving_piston"), BlockBase.MOVING_PISTON);
        blocks.registerValue(Identifier.of("dandelion"), BlockBase.DANDELION);
        blocks.registerValue(Identifier.of("rose"), BlockBase.ROSE);
        blocks.registerValue(Identifier.of("brown_mushroom"), BlockBase.BROWN_MUSHROOM);
        blocks.registerValue(Identifier.of("red_mushroom"), BlockBase.RED_MUSHROOM);
        blocks.registerValue(Identifier.of("gold_block"), BlockBase.GOLD_BLOCK);
        blocks.registerValue(Identifier.of("iron_block"), BlockBase.IRON_BLOCK);
        blocks.registerValue(Identifier.of("double_slab"), BlockBase.DOUBLE_STONE_SLAB);
        blocks.registerValue(Identifier.of("slab"), BlockBase.STONE_SLAB);
        blocks.registerValue(Identifier.of("bricks"), BlockBase.BRICK);
        blocks.registerValue(Identifier.of("tnt"), BlockBase.TNT);
        blocks.registerValue(Identifier.of("bookshelf"), BlockBase.BOOKSHELF);
        blocks.registerValue(Identifier.of("mossy_cobblestone"), BlockBase.MOSSY_COBBLESTONE);
        blocks.registerValue(Identifier.of("obsidian"), BlockBase.OBSIDIAN);
        blocks.registerValue(Identifier.of("torch"), BlockBase.TORCH);
        blocks.registerValue(Identifier.of("fire"), BlockBase.FIRE);
        blocks.registerValue(Identifier.of("spawner"), BlockBase.MOB_SPAWNER);
        blocks.registerValue(Identifier.of("plank_stairs"), BlockBase.STAIRS_WOOD);
        blocks.registerValue(Identifier.of("chest"), BlockBase.CHEST);
        blocks.registerValue(Identifier.of("redstone_wire"), BlockBase.REDSTONE_DUST);
        blocks.registerValue(Identifier.of("diamond_ore"), BlockBase.DIAMOND_ORE);
        blocks.registerValue(Identifier.of("diamond_block"), BlockBase.DIAMOND_BLOCK);
        blocks.registerValue(Identifier.of("crafting_table"), BlockBase.WORKBENCH);
        blocks.registerValue(Identifier.of("wheat"), BlockBase.CROPS);
        blocks.registerValue(Identifier.of("farmland"), BlockBase.FARMLAND);
        blocks.registerValue(Identifier.of("furnace"), BlockBase.FURNACE);
        blocks.registerValue(Identifier.of("furnace_lit"), BlockBase.FURNACE_LIT);
        blocks.registerValue(Identifier.of("sign"), BlockBase.STANDING_SIGN);
        blocks.registerValue(Identifier.of("plank_door"), BlockBase.DOOR_WOOD);
        blocks.registerValue(Identifier.of("ladder"), BlockBase.LADDER);
        blocks.registerValue(Identifier.of("rail"), BlockBase.RAIL);
        blocks.registerValue(Identifier.of("cobblestone_stairs"), BlockBase.STAIRS_STONE);
        blocks.registerValue(Identifier.of("wall_sign"), BlockBase.WALL_SIGN);
        blocks.registerValue(Identifier.of("lever"), BlockBase.LEVER);
        blocks.registerValue(Identifier.of("plank_pressure_plate"), BlockBase.WOODEN_PRESSURE_PLATE);
        blocks.registerValue(Identifier.of("iron_door"), BlockBase.DOOR_IRON);
        blocks.registerValue(Identifier.of("stone_pressure_plate"), BlockBase.STONE_PRESSURE_PLATE);
        blocks.registerValue(Identifier.of("redstone_ore"), BlockBase.REDSTONE_ORE);
        blocks.registerValue(Identifier.of("redstone_ore_lit"), BlockBase.REDSTONE_ORE_LIT);
        blocks.registerValue(Identifier.of("redstone_torch"), BlockBase.REDSTONE_TORCH);
        blocks.registerValue(Identifier.of("redstone_torch_lit"), BlockBase.REDSTONE_TORCH_LIT);
        blocks.registerValue(Identifier.of("button"), BlockBase.BUTTON);
        blocks.registerValue(Identifier.of("snow"), BlockBase.SNOW);
        blocks.registerValue(Identifier.of("ice"), BlockBase.ICE);
        blocks.registerValue(Identifier.of("snow_block"), BlockBase.SNOW_BLOCK);
        blocks.registerValue(Identifier.of("cactus"), BlockBase.CACTUS);
        blocks.registerValue(Identifier.of("clay"), BlockBase.CLAY);
        blocks.registerValue(Identifier.of("sugar_cane"), BlockBase.SUGAR_CANES);
        blocks.registerValue(Identifier.of("jukebox"), BlockBase.JUKEBOX);
        blocks.registerValue(Identifier.of("fence"), BlockBase.FENCE);
        blocks.registerValue(Identifier.of("pumpkin"), BlockBase.PUMPKIN);
        blocks.registerValue(Identifier.of("netherrack"), BlockBase.NETHERRACK);
        blocks.registerValue(Identifier.of("soul_sand"), BlockBase.SOUL_SAND);
        blocks.registerValue(Identifier.of("glowstone"), BlockBase.GLOWSTONE);
        blocks.registerValue(Identifier.of("portal"), BlockBase.PORTAL);
        blocks.registerValue(Identifier.of("jack_o_lantern"), BlockBase.JACK_O_LANTERN);
        blocks.registerValue(Identifier.of("cake"), BlockBase.CAKE);
        blocks.registerValue(Identifier.of("repeater"), BlockBase.REDSTONE_REPEATER);
        blocks.registerValue(Identifier.of("repeater_lit"), BlockBase.REDSTONE_REPEATER_LIT);
        blocks.registerValue(Identifier.of("locked_chest"), BlockBase.LOCKED_CHEST);
        blocks.registerValue(Identifier.of("trapdoor"), BlockBase.TRAPDOOR);
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
