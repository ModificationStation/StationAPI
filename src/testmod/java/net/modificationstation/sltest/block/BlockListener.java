package net.modificationstation.sltest.block;

import com.google.gson.Gson;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class BlockListener {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        BlockRegistry registry = event.registry;
        testAnimatedBlock = new ModdedMetaBlock(of(MODID, "test_animated_block"), Material.PORTAL).setTranslationKey(MODID, "testAnimatedBlock"); //151
        testBlock = new TemplateBlockBase(of(MODID, "test_block"), Material.CLAY).setHardness(1).setTranslationKey(MODID, "testBlock"); //150
        customModelBlock = new ModdedModelBlock(of(MODID, "farlands_block"), Material.DIRT).setTranslationKey(MODID, "farlands_block"); //152
        Freezer = (BlockFreezer) new BlockFreezer(of(MODID, "freezer")).setHardness(2.5F).setSounds(TemplateBlockBase.STONE_SOUNDS).setTranslationKey(MODID, "freezer"); //153
        altar = new BlockAltar(of(MODID, "altar"), Material.STONE).setHardness(3).setTranslationKey(MODID, "altar");
        System.out.println(new Gson().toJson(customModelBlock));

        System.out.println(registry.getIdentifier(TemplateBlockBase.BEDROCK));
    }

    public static TemplateBlockBase testBlock;
    public static TemplateBlockBase testAnimatedBlock;
    public static TemplateBlockBase customModelBlock;
    public static BlockFreezer Freezer;
    public static TemplateBlockBase altar;
}