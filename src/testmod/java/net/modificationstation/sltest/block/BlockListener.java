package net.modificationstation.sltest.block;

import com.google.gson.Gson;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.Null;
import net.modificationstation.stationapi.template.common.block.TemplateBlockBase;

public class BlockListener {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener
    public void registerBlocks(RegistryEvent.Blocks event) {
        BlockRegistry registry = event.registry;
        testAnimatedBlock = new ModdedMetaBlock(Identifier.of(MODID, "test_animated_block"), Material.PORTAL).setTranslationKey(MODID, "testAnimatedBlock"); //151
        testBlock = new TemplateBlockBase(Identifier.of(MODID, "test_block"), Material.CLAY).setTranslationKey(MODID, "testBlock"); //150
        customModelBlock = new ModdedModelBlock(Identifier.of(MODID, "farlands_block"), Material.DIRT).setTranslationKey(MODID, "farlands_block"); //152
        Freezer = (BlockFreezer) new BlockFreezer(Identifier.of(MODID, "freezer")).setHardness(2.5F).setSounds(TemplateBlockBase.STONE_SOUNDS).setTranslationKey(MODID, "freezer"); //153
        System.out.println(new Gson().toJson(customModelBlock));

        System.out.println(registry.getIdentifier(TemplateBlockBase.BEDROCK));
    }

    public static TemplateBlockBase testBlock;
    public static TemplateBlockBase testAnimatedBlock;
    public static TemplateBlockBase customModelBlock;
    public static BlockFreezer Freezer;
}