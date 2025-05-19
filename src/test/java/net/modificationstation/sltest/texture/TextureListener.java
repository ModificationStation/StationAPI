package net.modificationstation.sltest.texture;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.block.BlockFreezer;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.template.block.TemplateDoorBlock;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.sltest.block.Blocks.*;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class TextureListener {

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {

        ExpandableAtlas terrain = Atlases.getTerrain();

        TEST_BLOCK.get().textureId = terrain.addTexture(of(NAMESPACE, "blocks/testBlock")).index;
        INDISPENSABLE_BLOCK.get().textureId = terrain.addTexture(of(NAMESPACE, "blocks/indispensableBlock")).index;
        TEST_ANIMATED_BLOCK.get().textureId = terrain.addTexture(of(NAMESPACE, "blocks/testAnimatedBlock")).index;
        FREEZER.get().textureId = terrain.addTexture(of(NAMESPACE, "blocks/FreezerTop")).index;
        ((BlockFreezer) FREEZER.get()).sideTexture = terrain.addTexture(of(NAMESPACE, "blocks/FreezerSide")).index;

        altarTextures[Direction.DOWN.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_bottom")).index;
        altarTextures[Direction.UP.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_top")).index;
        altarTextures[Direction.EAST.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_east")).index;
        altarTextures[Direction.WEST.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_west")).index;
        altarTextures[Direction.NORTH.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_north")).index;
        altarTextures[Direction.SOUTH.ordinal()] = terrain.addTexture(of(NAMESPACE, "blocks/altar_south")).index;

        ItemListener.testNBTItem.setTexture(of(NAMESPACE, "items/nbtItem"));
        ItemListener.testItem.setTexture(of(NAMESPACE, "items/highres"));
//        ItemListener.testPickaxe.setAnimationBinder("/assets/sltest/stationapi/textures/items/testPickaxe.png", 1, of(MODID, "items/testItem"));
        ItemListener.testPickaxe.setTexture(of(NAMESPACE, "items/testPickaxe"));

//        SquareAtlas.GUI_ITEMS.addAnimationBinder("/assets/sltest/textures/items/testPickaxe.png", 1, 0);

        TEST_ATLAS = new ExpandableAtlas(of(SLTest.NAMESPACE, "test_atlas"));

        TEST_ATLAS.addTexture(of(NAMESPACE, "items/testItem"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "blocks/testBlock"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "blocks/testAnimatedBlock"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "items/testPickaxe"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "items/nbtItem"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "blocks/FreezerTop"));
        TEST_ATLAS.addTexture(of(NAMESPACE, "blocks/FreezerSide"));

//        farlandsBlockModel = JsonUnbakedModel.get(of(MODID, "farlandsBlock"));
//        testItemModel = JsonUnbakedModel.get(of(MODID, "item/testItem"));

        // I'm using hardcoded IDs because something's fucked up with the order of the texture registry event so I can't get textures.
        ((TemplateDoorBlock) FANCY_WOOD_DOOR.get()).topTextureId = 81;
        ((TemplateDoorBlock) FANCY_WOOD_DOOR.get()).bottomTextureId = 97;
//        ((TemplateDoorBlock) FANCY_WOOD_DOOR.get()).topTextureId = terrain.getTexture(Namespace.MINECRAFT.id("block/oak_door_top")).index;
//        ((TemplateDoorBlock) FANCY_WOOD_DOOR.get()).bottomTextureId = terrain.getTexture(Namespace.MINECRAFT.id("block/oak_door_bottom")).index;
    }

    public static final int[] altarTextures = new int[6];

    public static ExpandableAtlas TEST_ATLAS;

    public static JsonUnbakedModel farlandsBlockModel;
    public static JsonUnbakedModel testItemModel;
}
