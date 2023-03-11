package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElement;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementFace;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementTexture;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3f;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ItemModelGenerator {
    public static final List<String> LAYERS = Lists.newArrayList("layer0", "layer1", "layer2", "layer3", "layer4");

    public JsonUnbakedModel create(Function<SpriteIdentifier, Sprite> textureGetter, JsonUnbakedModel blockModel) {
        Map<String, Either<SpriteIdentifier, String>> map = new HashMap<>();
        List<ModelElement> list = new ArrayList<>();

        for(int i = 0; i < LAYERS.size(); ++i) {
            String string = LAYERS.get(i);
            if (!blockModel.textureExists(string)) {
                break;
            }

            SpriteIdentifier spriteIdentifier = blockModel.resolveSprite(string);
            map.put(string, Either.left(spriteIdentifier));
            Sprite sprite = textureGetter.apply(spriteIdentifier);
            list.addAll(this.addLayerElements(i, string, sprite));
        }

        map.put("particle", blockModel.textureExists("particle") ? Either.left(blockModel.resolveSprite("particle")) : map.get("layer0"));
        JsonUnbakedModel jsonUnbakedModel = new JsonUnbakedModel(null, list, map, false, blockModel.getGuiLight(), blockModel.getTransformations(), blockModel.getOverrides());
        jsonUnbakedModel.id = blockModel.id;
        return jsonUnbakedModel;
    }

    private List<ModelElement> addLayerElements(int layer, String key, Sprite sprite) {
        List<ModelElement> elements = new ArrayList<>();

        int width = sprite.getWidth();
        int height = sprite.getHeight();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        float animationFrameDelta = sprite.getAnimationFrameDelta();
        int[] frames = sprite.getDistinctFrameCount().toArray();

        Map<Direction, ModelElementFace> map = new EnumMap<>(Direction.class);
        map.put(Direction.WEST, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0, animationFrameDelta)));
        map.put(Direction.EAST, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0, animationFrameDelta)));
        elements.add(new ModelElement(new Vec3f(0.0F, 0.0F, 7.5F), new Vec3f(16.0F, 16.0F, 8.5F), map, null, true));

        int first1 = -1;
        int first2 = -1;
        int last1 = -1;
        int last2 = -1;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    if (doesPixelHaveEdge(sprite, frames, x, y, Side.DOWN)) {
                        if (first1 == -1) {
                            first1 = x;
                        }
                        last1 = x;
                    }
                    if (doesPixelHaveEdge(sprite, frames, x, y, Side.UP)) {
                        if (first2 == -1) {
                            first2 = x;
                        }
                        last2 = x;
                    }
                } else {
                    if (first1 != -1) {
                        elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
                        first1 = -1;
                    }
                    if (first2 != -1) {
                        elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
                        first2 = -1;
                    }
                }
            }

            if (first1 != -1) {
                elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
                first1 = -1;
            }
            if (first2 != -1) {
                elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
                first2 = -1;
            }
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    if (doesPixelHaveEdge(sprite, frames, x, y, Side.RIGHT)) {
                        if (first1 == -1) {
                            first1 = y;
                        }
                        last1 = y;
                    }
                    if (doesPixelHaveEdge(sprite, frames, x, y, Side.LEFT)) {
                        if (first2 == -1) {
                            first2 = y;
                        }
                        last2 = y;
                    }
                } else {
                    if (first1 != -1) {
                        elements.add(createVerticalOutlineElement(Direction.SOUTH, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
                        first1 = -1;
                    }
                    if (first2 != -1) {
                        elements.add(createVerticalOutlineElement(Direction.NORTH, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
                        first2 = -1;
                    }
                }
            }

            if (first1 != -1) {
                elements.add(createVerticalOutlineElement(Direction.SOUTH, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
                first1 = -1;
            }
            if (first2 != -1) {
                elements.add(createVerticalOutlineElement(Direction.NORTH, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
                first2 = -1;
            }
        }

        return elements;
    }

    private static ModelElement createHorizontalOutlineElement(Direction direction, int layer, String key, int start, int end, int y, int height, float animationFrameDelta, float xFactor, float yFactor) {
        Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor }, 0, animationFrameDelta)));
        return new ModelElement(new Vec3f(start / xFactor, (height - (y + 1)) / yFactor, 7.5F), new Vec3f((end + 1) / xFactor, (height - y) / yFactor, 8.5F), faces, null, true);
    }

    private static ModelElement createVerticalOutlineElement(Direction direction, int layer, String key, int start, int end, int x, int height, float animationFrameDelta, float xFactor, float yFactor) {
        Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { (x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor }, 0, animationFrameDelta)));
        return new ModelElement(new Vec3f(x / xFactor, (height - (end + 1)) / yFactor, 7.5F), new Vec3f((x + 1) / xFactor, (height - start) / yFactor, 8.5F), faces, null, true);
    }

    private static ModelElementTexture createUnlerpedTexture(float[] uvs, @SuppressWarnings("SameParameterValue") int rotation, float delta) {
        return new ModelElementTexture(unlerpUVs(uvs, delta), rotation);
    }

    private static float unlerp(float delta, float start, float end) {
        return (start - delta * end) / (1 - delta);
    }

    private static float[] unlerpUVs(float[] uvs, float delta) {
        float centerU = (uvs[0] + uvs[2]) / 2.0F;
        float centerV = (uvs[1] + uvs[3]) / 2.0F;
        uvs[0] = unlerp(delta, uvs[0], centerU);
        uvs[2] = unlerp(delta, uvs[2], centerU);
        uvs[1] = unlerp(delta, uvs[1], centerV);
        uvs[3] = unlerp(delta, uvs[3], centerV);
        return uvs;
    }

    private static boolean isPixelOutsideSprite(Sprite sprite, int x, int y) {
        return x < 0 || y < 0 || x >= sprite.getWidth() || y >= sprite.getHeight();
    }

    private static boolean isPixelTransparent(Sprite sprite, int frame, int x, int y) {
        return isPixelOutsideSprite(sprite, x, y) || sprite.isPixelTransparent(frame, x, y);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isPixelAlwaysTransparent(Sprite sprite, int[] frames, int x, int y) {
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y)) {
                return false;
            }
        }
        return true;
    }

    public static boolean doesPixelHaveEdge(Sprite sprite, int[] frames, int x, int y, Side direction) {
        int x1 = x + direction.getOffsetX();
        int y1 = y + direction.getOffsetY();
        if (isPixelOutsideSprite(sprite, x1, y1)) {
            return true;
        }
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y) && isPixelTransparent(sprite, frame, x1, y1)) {
                return true;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    enum Side {
        UP(Direction.UP, 0, -1),
        DOWN(Direction.DOWN, 0, 1),
        LEFT(Direction.SOUTH, -1, 0),
        RIGHT(Direction.NORTH, 1, 0);

        private final Direction direction;
        private final int offsetX;
        private final int offsetY;

        Side(Direction direction, int offsetX, int offsetY) {
            this.direction = direction;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public int getOffsetX() {
            return this.offsetX;
        }

        public int getOffsetY() {
            return this.offsetY;
        }
    }
}
