package net.modificationstation.sltest.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.model.json.JsonModel;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.sltest.block.BlockListener.Freezer;
import static net.modificationstation.sltest.block.BlockListener.testAnimatedBlock;
import static net.modificationstation.sltest.block.BlockListener.testBlock;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class TextureListener {

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
//        BlockListener.testBlock.texture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("TERRAIN"), "/assets/sltest/textures/blocks/testBlock.png");
//        BlockListener.testAnimatedBlock.texture = TextureFactory.INSTANCE.addAnimatedTexture(TextureRegistry.getRegistry("TERRAIN"), "/assets/sltest/textures/blocks/testAnimatedBlock.png", 1);
//        BlockListener.Freezer.texture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN), "/assets/sltest/textures/blocks/FreezerTop.png");
//        BlockListener.Freezer.sideTexture = TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN), "/assets/sltest/textures/blocks/FreezerSide.png");
        //BlockListener.testBlock.texture = BlockBase.WOOL.texture;

//        ItemListener.testItem.setTexturePosition(TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/testItem.png"));
//        ItemListener.testPickaxe.setTexturePosition(TextureFactoryOld.INSTANCE.addAnimatedTexture(TextureRegistryOld.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/testPickaxe.png", 4));
//        ItemListener.testNBTItem.setTexturePosition(TextureFactory.INSTANCE.addTexture(TextureRegistry.getRegistry("GUI_ITEMS"), "/assets/sltest/textures/items/nbtItem.png"));

        ExpandableAtlas terrain = Atlases.getStationTerrain();

        testBlock.texture = terrain.addTexture(of(MODID, "blocks/testBlock")).index;
        testAnimatedBlock.texture = terrain.addTexture(of(MODID, "blocks/testAnimatedBlock")).index;
        Freezer.texture = terrain.addTexture(of(MODID, "blocks/FreezerTop")).index;
        Freezer.sideTexture = terrain.addTexture(of(MODID, "blocks/FreezerSide")).index;

        altarTextures[Direction.DOWN.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_bottom")).index;
        altarTextures[Direction.UP.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_top")).index;
        altarTextures[Direction.EAST.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_east")).index;
        altarTextures[Direction.WEST.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_west")).index;
        altarTextures[Direction.NORTH.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_north")).index;
        altarTextures[Direction.SOUTH.ordinal()] = terrain.addTexture(of(MODID, "blocks/altar_south")).index;

        ItemListener.testNBTItem.setTexture(of(MODID, "items/nbtItem"));
        ItemListener.testItem.setTexture(of(MODID, "items/highres"));
//        ItemListener.testPickaxe.setAnimationBinder("/assets/sltest/stationapi/textures/items/testPickaxe.png", 1, of(MODID, "items/testItem"));
        ItemListener.testPickaxe.setTexture(of(MODID, "items/testPickaxe"));

//        SquareAtlas.GUI_ITEMS.addAnimationBinder("/assets/sltest/textures/items/testPickaxe.png", 1, 0);

        TEST_ATLAS = new ExpandableAtlas(of(SLTest.MODID, "test_atlas"));

        TEST_ATLAS.addTexture(of(MODID, "items/testItem"));
        TEST_ATLAS.addTexture(of(MODID, "blocks/testBlock"));
        TEST_ATLAS.addTexture(of(MODID, "blocks/testAnimatedBlock"));
        TEST_ATLAS.addTexture(of(MODID, "items/testPickaxe"));
        TEST_ATLAS.addTexture(of(MODID, "items/nbtItem"));
        TEST_ATLAS.addTexture(of(MODID, "blocks/FreezerTop"));
        TEST_ATLAS.addTexture(of(MODID, "blocks/FreezerSide"));

        farlandsBlockModel = JsonModel.get(of(MODID, "farlandsBlock"));
    }

    public static final int[] altarTextures = new int[6];

    public static ExpandableAtlas TEST_ATLAS;

    public static JsonModel farlandsBlockModel;
}
