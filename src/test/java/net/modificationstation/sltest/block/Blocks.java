package net.modificationstation.sltest.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.modificationstation.sltest.mixin.BlockBaseAccessor;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Arrays;
import java.util.function.Function;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public enum Blocks {

    TEST_BLOCK("test_block", "testBlock", id -> new TemplateBlock(id, Material.CLAY).setHardness(1)),
    TEST_ANIMATED_BLOCK("test_animated_block", "testAnimatedBlock", id -> new ModdedMetaBlock(id, Material.NETHER_PORTAL)),
    CUSTOM_MODEL_BLOCK("farlands_block", "farlands_block", id -> new ModdedModelBlock(id, Material.SOIL).setHardness(1)),
    FREEZER("freezer", "freezer", id -> new BlockFreezer(id).setHardness(2.5F).setSoundGroup(TemplateBlock.DEFAULT_SOUND_GROUP)),
    ALTAR("altar", "altar", id -> new BlockAltar(id, Material.STONE).setHardness(3)),
    VARIATION_BLOCK("variation_block", "variationBlock", id -> new VariationBlock(id, Material.STONE).setHardness(.5F).setSoundGroup(Block.DEFAULT_SOUND_GROUP).disableAutoItemRegistration()),
    EMISSION_CHECKER("emission_checker", "emissionChecker", LampBlock::new),
    INDISPENSABLE_BLOCK("indispensable_block", "indispensableBlock", IndispensableBlock::new),
    EFFECT_BLOCK("effect_block", "effectBlock", EffectBlock::new);

    private final Runnable register;
    private Block block;

    Blocks(String id, String translationKey, Function<Identifier, Block> factory) {
        this.register = () -> block = factory.apply(of(NAMESPACE, id)).setTranslationKey(NAMESPACE, translationKey);
    }

    public Block get() {
        return block;
    }

    public static class Init {

        @EventListener
        private static void registerBlocks(BlockRegistryEvent event) {
//            BlockBase.ALLOWS_GRASS_UNDER[BlockBase.STILL_WATER.id] = BlockBase.ALLOWS_GRASS_UNDER[BlockBase.FLOWING_WATER.id] = true;
            ((BlockBaseAccessor) Block.BEDROCK).invokeSetHardness(2);
//            int blocksAmount = 100000;
//            BLOCKS = new TemplateBlockBase[blocksAmount];
//            Random random = new Random(42);
//            for (int i = 0; i < blocksAmount; i++) {
//                BlockBase block = new ColouredBlock(Material.DIRT, random.nextInt()).setHardness(0.8F).setSounds(BlockBase.GLASS_SOUNDS).setTranslationKey(MODID, "testBlock" + i);
//                Registry.register(event.registry, of(MODID, "test_block_" + i), block);
//            }
            Arrays.stream(values()).forEach(blocks -> blocks.register.run());
        }

//        public static BlockBase[] BLOCKS;
    }
}