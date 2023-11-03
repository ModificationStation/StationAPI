package net.modificationstation.sltest.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.block.BlockFreezer;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.sltest.block.Blocks.*;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class TextureListener {

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {

        ExpandableAtlas terrain = Atlases.getTerrain();

        TEST_BLOCK.get().textureId = terrain.addTexture(of(MODID, "blocks/testBlock")).index;
        TEST_ANIMATED_BLOCK.get().textureId = terrain.addTexture(of(MODID, "blocks/testAnimatedBlock")).index;
        FREEZER.get().textureId = terrain.addTexture(of(MODID, "blocks/FreezerTop")).index;
        ((BlockFreezer) FREEZER.get()).sideTexture = terrain.addTexture(of(MODID, "blocks/FreezerSide")).index;

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

//        farlandsBlockModel = JsonUnbakedModel.get(of(MODID, "farlandsBlock"));
//        testItemModel = JsonUnbakedModel.get(of(MODID, "item/testItem"));
    }

    public static final int[] altarTextures = new int[6];

    public static ExpandableAtlas TEST_ATLAS;

    public static JsonUnbakedModel farlandsBlockModel;
    public static JsonUnbakedModel testItemModel;
}
