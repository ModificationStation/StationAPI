package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.model.BasicBakedModel;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.model.Quad;
import net.modificationstation.stationapi.api.client.model.Vertex;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Either;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.model.GuiLightType;
import net.modificationstation.stationapi.impl.client.model.JsonCuboidData;
import net.modificationstation.stationapi.impl.client.model.JsonFaceData;
import net.modificationstation.stationapi.impl.client.model.JsonModelData;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas.MISSING;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.util.math.Direction.values;

public final class JsonModel extends Model {

    public static final Identifier BUILTIN_GENERATED = Identifier.of("builtin/generated");

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(EnumMap.class, (InstanceCreator<EnumMap>) type -> new EnumMap((Class) ((ParameterizedType) type).getActualTypeArguments()[0])).create();

    private JsonModelData data;
    private ImmutableMap<String, Atlas.Sprite> textures;

    public static JsonModel get(final Identifier identifier) {
        return get(identifier, JsonModel::new);
    }

    private JsonModel(final Identifier identifier) {
        super(identifier, "json");
    }

    @Override
    public void reloadFromTexturePack(final TexturePack newTexturePack) {
        invalidated = true;
        InputStream stream = newTexturePack.getResourceAsStream(modelPath);
        data = GSON.fromJson(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")), JsonModelData.class);
        List<JsonModelData> inheritance = new ArrayList<>();
        boolean generated = false;
        {
            JsonModelData parentData = data;
            inheritance.add(parentData);
            while (parentData.parent != null) {
                if (Identifier.of(parentData.parent) == BUILTIN_GENERATED) {
                    generated = true;
                    break;
                } else
                    inheritance.add(
                            parentData = GSON.fromJson(
                                    new BufferedReader(new InputStreamReader(
                                            newTexturePack.getResourceAsStream(ResourceManager.ASSETS.toPath(
                                                    Identifier.of(parentData.parent),
                                                    MODID + "/models", "json"
                                            )),
                                            StandardCharsets.UTF_8
                                    )).lines().collect(Collectors.joining("\n")), JsonModelData.class)
                    );
            }
            Collections.reverse(inheritance);
        }
        Map<String, String> textures = new HashMap<>();
        List<JsonCuboidData> elements = new ArrayList<>();
        AtomicReference<GuiLightType> guiLight = new AtomicReference<>(GuiLightType.SIDE);
        inheritance.forEach(parentData -> {
            if (parentData.textures != null)
                textures.putAll(parentData.textures);
            if (parentData.elements != null) {
                elements.clear();
                elements.addAll(parentData.elements);
            }
            if (parentData.gui_light != null)
                guiLight.set(parentData.gui_light);
        });
        data.textures = textures;
        data.elements = elements;
        data.gui_light = guiLight.get();
        ImmutableMap.Builder<String, Atlas.Sprite> texturesBuilder = ImmutableMap.builder();
        data.textures.forEach((textureId, texturePath) -> {
            while (texturePath.startsWith("#")) texturePath = data.textures.get(texturePath.substring(1));
            texturesBuilder.put("#" + textureId, Atlases.getTerrain().addTexture(of(texturePath)));
        });
        this.textures = texturesBuilder.build();
        data.elements.forEach(cuboid -> {
            cuboid.postprocess();
            cuboid.faces.values().forEach(face -> face.postprocess(this.textures.getOrDefault(face.textureId, Atlases.getTerrain().addTexture(MISSING))));
        });
    }

    private void updateUVs() {
        if (data != null)
            data.elements.forEach(cuboid -> cuboid.faces.values().forEach(JsonFaceData::updateUVs));
    }

    @Override
    protected BasicBakedModel bake() {
        updateUVs();
        Map<Direction, ImmutableList.Builder<Quad>> faceQuadsBuilders = new EnumMap<>(Direction.class);
        Arrays.stream(values()).forEach(direction -> faceQuadsBuilders.put(direction, ImmutableList.builder()));
        ImmutableList.Builder<Quad> quads = ImmutableList.builder();
        data.elements.forEach(cuboid -> {
            double[]
                    from = cuboid.from,
                    to = cuboid.to;
            double
                    xFrom = from[0],
                    yFrom = from[1],
                    zFrom = from[2],
                    xTo = to[0],
                    yTo = to[1],
                    zTo = to[2];
            Map<Direction, JsonFaceData> faces = cuboid.faces;
            boolean shade = cuboid.isShade();
            faces.forEach((direction, face) -> {
                ImmutableList.Builder<Quad> q = face.cullface == null ? quads : faceQuadsBuilders.get(face.cullface);
                face.updateUVs();
                double[] uv = face.getUv();
                switch (direction) {
                    case DOWN:
                        q.add(Quad.get(
                                Vertex.get(xFrom, yFrom, zTo, uv[4], uv[7], direction, shade),
                                Vertex.get(xFrom, yFrom, zFrom, uv[0], uv[1], direction, shade),
                                Vertex.get(xTo, yFrom, zFrom, uv[6], uv[5], direction, shade),
                                Vertex.get(xTo, yFrom, zTo, uv[2], uv[3], direction, shade)
                        ));
                        break;
                    case UP:
                        q.add(Quad.get(
                                Vertex.get(xTo, yTo, zTo, uv[2], uv[3], direction, shade),
                                Vertex.get(xTo, yTo, zFrom, uv[6], uv[5], direction, shade),
                                Vertex.get(xFrom, yTo, zFrom, uv[0], uv[1], direction, shade),
                                Vertex.get(xFrom, yTo, zTo, uv[4], uv[7], direction, shade)
                        ));
                        break;
                    case EAST:
                        q.add(Quad.get(
                                Vertex.get(xFrom, yTo, zFrom, uv[2], uv[1], direction, shade),
                                Vertex.get(xTo, yTo, zFrom, uv[0], uv[1], direction, shade),
                                Vertex.get(xTo, yFrom, zFrom, uv[0], uv[3], direction, shade),
                                Vertex.get(xFrom, yFrom, zFrom, uv[2], uv[3], direction, shade)
                        ));
                        break;
                    case WEST:
                        q.add(Quad.get(
                                Vertex.get(xFrom, yTo, zTo, uv[0], uv[1], direction, shade),
                                Vertex.get(xFrom, yFrom, zTo, uv[0], uv[3], direction, shade),
                                Vertex.get(xTo, yFrom, zTo, uv[2], uv[3], direction, shade),
                                Vertex.get(xTo, yTo, zTo, uv[2], uv[1], direction, shade)
                        ));
                        break;
                    case NORTH:
                        q.add(Quad.get(
                                Vertex.get(xFrom, yTo, zTo, uv[2], uv[1], direction, shade),
                                Vertex.get(xFrom, yTo, zFrom, uv[0], uv[1], direction, shade),
                                Vertex.get(xFrom, yFrom, zFrom, uv[0], uv[3], direction, shade),
                                Vertex.get(xFrom, yFrom, zTo, uv[2], uv[3], direction, shade)
                        ));
                        break;
                    case SOUTH:
                        q.add(Quad.get(
                                Vertex.get(xTo, yFrom, zTo, uv[0], uv[3], direction, shade),
                                Vertex.get(xTo, yFrom, zFrom, uv[2], uv[3], direction, shade),
                                Vertex.get(xTo, yTo, zFrom, uv[2], uv[1], direction, shade),
                                Vertex.get(xTo, yTo, zTo, uv[0], uv[1], direction, shade)
                        ));
                        break;
                }
            });
        });
        ImmutableMap.Builder<Direction, ImmutableList<Quad>> faceQuads = ImmutableMap.builder();
        faceQuadsBuilders.forEach((direction, faceQuadPointBuilder) -> faceQuads.put(direction, faceQuadPointBuilder.build()));
        return new BasicBakedModel.Builder()
                .faceQuads(Maps.immutableEnumMap(faceQuads.build()))
                .quads(quads.build())
                .useAO(data.isAmbientocclusion())
                .isSideLit(data.gui_light == GuiLightType.SIDE)
                .sprite(textures.get("#particle"))
                .build();
    }

    private static boolean isTextureReference(String reference) {
        return reference.charAt(0) == '#';
    }

    @Environment(EnvType.CLIENT)
    public enum GuiLight {

        /**
         * The model will be shaded from the front, like a basic item
         */
        FRONT("front"),

        /**
         * The model will be shaded from the side, like a block.
         */
        SIDE("side");

        private final String name;

        GuiLight(String name) {
            this.name = name;
        }

        public static JsonModel.GuiLight deserialize(String value) {
            JsonModel.GuiLight[] var1 = values();

            for (JsonModel.GuiLight guiLight : var1) {
                if (guiLight.name.equals(value)) {
                    return guiLight;
                }
            }

            throw new IllegalArgumentException("Invalid gui light: " + value);
        }

        public boolean isSide() {
            return this == SIDE;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<JsonModel> {
        public JsonModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            List<ModelElement> list = this.deserializeElements(jsonDeserializationContext, jsonObject);
            String string = this.deserializeParent(jsonObject);
            Map<String, Either<SpriteIdentifier, String>> map = this.deserializeTextures(jsonObject);
            boolean bl = this.deserializeAmbientOcclusion(jsonObject);
            ModelTransformation modelTransformation = ModelTransformation.NONE;
            if (jsonObject.has("display")) {
                JsonObject jsonObject2 = JsonHelper.getObject(jsonObject, "display");
                modelTransformation = jsonDeserializationContext.deserialize(jsonObject2, ModelTransformation.class);
            }

            List<ModelOverride> list2 = this.deserializeOverrides(jsonDeserializationContext, jsonObject);
            JsonModel.GuiLight guiLight = null;
            if (jsonObject.has("gui_light")) {
                guiLight = JsonModel.GuiLight.deserialize(JsonHelper.getString(jsonObject, "gui_light"));
            }

            Identifier identifier = string.isEmpty() ? null : Identifier.of(string);
//            return new JsonModel(identifier, list, map, bl, guiLight, modelTransformation, list2);
            return null;
        }

        protected List<ModelOverride> deserializeOverrides(JsonDeserializationContext context, JsonObject object) {
            List<ModelOverride> list = Lists.newArrayList();
            if (object.has("overrides")) {
                JsonArray jsonArray = JsonHelper.getArray(object, "overrides");

                for (JsonElement jsonElement : jsonArray) {
                    list.add(context.deserialize(jsonElement, ModelOverride.class));
                }
            }

            return list;
        }

        private Map<String, Either<SpriteIdentifier, String>> deserializeTextures(JsonObject object) {
            Identifier identifier = Atlases.getTerrain().id;
            Map<String, Either<SpriteIdentifier, String>> map = Maps.newHashMap();
            if (object.has("textures")) {
                JsonObject jsonObject = JsonHelper.getObject(object, "textures");

                for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                    map.put(stringJsonElementEntry.getKey(), resolveReference(identifier, stringJsonElementEntry.getValue().getAsString()));
                }
            }

            return map;
        }

        private static Either<SpriteIdentifier, String> resolveReference(Identifier id, String name) {
            if (JsonModel.isTextureReference(name)) {
                return Either.right(name.substring(1));
            } else {
                Identifier identifier = Identifier.tryParse(name);
                if (identifier == null) {
                    throw new JsonParseException(name + " is not valid resource location");
                } else {
                    return Either.left(SpriteIdentifier.of(id, identifier));
                }
            }
        }

        private String deserializeParent(JsonObject json) {
            return JsonHelper.getString(json, "parent", "");
        }

        protected boolean deserializeAmbientOcclusion(JsonObject json) {
            return JsonHelper.getBoolean(json, "ambientocclusion", true);
        }

        protected List<ModelElement> deserializeElements(JsonDeserializationContext context, JsonObject json) {
            List<ModelElement> list = Lists.newArrayList();
            if (json.has("elements")) {

                for (JsonElement jsonElement : JsonHelper.getArray(json, "elements")) {
                    list.add(context.deserialize(jsonElement, ModelElement.class));
                }
            }

            return list;
        }
    }
}
