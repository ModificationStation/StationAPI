package net.modificationstation.sltest.texture;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.block.BlockListener;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.ExpandableTextureAtlas;
import net.modificationstation.stationapi.api.client.texture.TextureFactory;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TextureListener {

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ItemListener.testItem.setTexturePosition(TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/testItem.png"));
        BlockListener.testBlock.texture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("TERRAIN"), "/assets/sltest/textures/blocks/testBlock.png");
        BlockListener.testAnimatedBlock.texture = TextureFactory.INSTANCE.addAnimatedTexture(TextureRegistry.getRegistry("TERRAIN"), "/assets/sltest/textures/blocks/testAnimatedBlock.png", 1);
        ItemListener.testPickaxe.setTexturePosition(TextureFactory.INSTANCE.addAnimatedTexture(TextureRegistry.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/testPickaxe.png", 4));
        ItemListener.testNBTItem.setTexturePosition(TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/nbtItem.png"));
        BlockListener.Freezer.texture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN), "/assets/sltest/textures/blocks/FreezerTop.png");
        BlockListener.Freezer.sideTexture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN), "/assets/sltest/textures/blocks/FreezerSide.png");
        //BlockListener.testBlock.texture = BlockBase.WOOL.texture;
        System.out.println(TexturePack.class.getResourceAsStream("/assets/sltest/textures/items/testItem.png"));

        TEST_ATLAS = new ExpandableTextureAtlas(Identifier.of(SLTest.MODID, "test_atlas"));

        TEST_ATLAS.addTexture("/assets/sltest/textures/items/testItem.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/blocks/testBlock.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/blocks/testAnimatedBlock.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/items/testPickaxe.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/items/nbtItem.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/blocks/FreezerTop.png");
        TEST_ATLAS.addTexture("/assets/sltest/textures/blocks/FreezerSide.png");
    }

    public static ExpandableTextureAtlas TEST_ATLAS;
}
