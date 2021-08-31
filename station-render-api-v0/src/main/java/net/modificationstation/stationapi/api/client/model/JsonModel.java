package net.modificationstation.stationapi.api.client.model;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.QuadPoint;
import net.modificationstation.stationapi.api.client.registry.JsonModelRegistry;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.impl.client.model.JsonCuboidData;
import net.modificationstation.stationapi.impl.client.model.JsonFaceData;
import net.modificationstation.stationapi.impl.client.model.JsonFacesData;
import net.modificationstation.stationapi.impl.client.model.JsonModelData;
import net.modificationstation.stationapi.impl.client.texture.TextureInit;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class JsonModel {

    public final Identifier id;
    public final String modelPath;
    private final Map<String, Atlas.Texture> textures = new HashMap<>();
    private final List<JsonCuboidData> cuboids = new ArrayList<>();
    public final List<QuadPoint> quads = new ArrayList<>();

    public JsonModel(final Identifier id) {
        JsonModelRegistry.INSTANCE.register(id, this);
        this.id = id;
        modelPath = ResourceManager.parsePath(id, "/" + MODID + "/models", "json");
        reload();
    }

    public void reload() {
        //noinspection deprecation
        final JsonModelData data = new Gson().fromJson(new BufferedReader(new InputStreamReader(((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack.getResourceAsStream(modelPath), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")), JsonModelData.class);
        final Map<String, Atlas.Texture> textures = new HashMap<>();
        textures.put("#missing", TextureInit.JSON_MISSING);
        data.textures.forEach((textureId, texturePath) -> textures.put("#" + textureId, Atlases.getStationJsonModels().addTexture(ResourceManager.parsePath(of(texturePath), "/" + MODID + "/textures", "png"))));
        this.textures.clear();
        this.textures.putAll(textures);
        cuboids.clear();
        cuboids.addAll(data.elements);
        cuboids.forEach(JsonCuboidData::postprocess);
        updateUVs();
    }

    public void updateUVs() {
        quads.clear();
//        cuboids.forEach(jsonCuboidData -> {
//            JsonFacesData jsonFacesData = jsonCuboidData.faces;
//            JsonFaceData jsonFaceData;
//            jsonFaceData = jsonFacesData.down;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//            jsonFaceData = jsonFacesData.up;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//            jsonFaceData = jsonFacesData.east;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//            jsonFaceData = jsonFacesData.west;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//            jsonFaceData = jsonFacesData.north;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//            jsonFaceData = jsonFacesData.south;
//            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
//        });
        cuboids.forEach(cuboid -> {
            double[]
                    from = cuboid.from,
                    to = cuboid.to;
            float
                    xFrom = (float) from[0],
                    yFrom = (float) from[1],
                    zFrom = (float) from[2],
                    xTo = (float) to[0],
                    yTo = (float) to[1],
                    zTo = (float) to[2];
            JsonFacesData jsonFacesData = cuboid.faces;
            JsonFaceData jsonFaceData;
            float[] uv;
            jsonFaceData = jsonFacesData.down;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xFrom, yFrom, zTo, uv[0], uv[3]));
            quads.add(new QuadPoint(xFrom, yFrom, zFrom, uv[0], uv[1]));
            quads.add(new QuadPoint(xTo, yFrom, zFrom, uv[2], uv[1]));
            quads.add(new QuadPoint(xTo, yFrom, zTo, uv[2], uv[3]));
            jsonFaceData = jsonFacesData.up;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xTo, yTo, zTo, uv[2], uv[3]));
            quads.add(new QuadPoint(xTo, yTo, zFrom, uv[2], uv[1]));
            quads.add(new QuadPoint(xFrom, yTo, zFrom, uv[0], uv[1]));
            quads.add(new QuadPoint(xFrom, yTo, zTo, uv[0], uv[3]));
            jsonFaceData = jsonFacesData.east;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xFrom, yTo, zFrom, uv[2], uv[1]));
            quads.add(new QuadPoint(xTo, yTo, zFrom, uv[0], uv[1]));
            quads.add(new QuadPoint(xTo, yFrom, zFrom, uv[0], uv[3]));
            quads.add(new QuadPoint(xFrom, yFrom, zFrom, uv[2], uv[3]));
            jsonFaceData = jsonFacesData.west;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xFrom, yTo, zTo, uv[0], uv[1]));
            quads.add(new QuadPoint(xFrom, yFrom, zTo, uv[0], uv[3]));
            quads.add(new QuadPoint(xTo, yFrom, zTo, uv[2], uv[3]));
            quads.add(new QuadPoint(xTo, yTo, zTo, uv[2], uv[1]));
            jsonFaceData = jsonFacesData.north;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xFrom, yTo, zTo, uv[2], uv[1]));
            quads.add(new QuadPoint(xFrom, yTo, zFrom, uv[0], uv[1]));
            quads.add(new QuadPoint(xFrom, yFrom, zFrom, uv[0], uv[3]));
            quads.add(new QuadPoint(xFrom, yFrom, zTo, uv[2], uv[3]));
            jsonFaceData = jsonFacesData.south;
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            uv = Floats.toArray(Doubles.asList(jsonFaceData.getUv()));
            quads.add(new QuadPoint(xTo, yFrom, zTo, uv[0], uv[3]));
            quads.add(new QuadPoint(xTo, yFrom, zFrom, uv[2], uv[3]));
            quads.add(new QuadPoint(xTo, yTo, zFrom, uv[2], uv[1]));
            quads.add(new QuadPoint(xTo, yTo, zTo, uv[0], uv[1]));
        });
    }

    public Map<String, Atlas.Texture> getTextures() {
        return Collections.unmodifiableMap(textures);
    }

    public List<JsonCuboidData> getCuboids() {
        return Collections.unmodifiableList(cuboids);
    }
}
