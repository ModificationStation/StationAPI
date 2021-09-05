package net.modificationstation.stationapi.api.client.model;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.block.BlockFaces;
import net.modificationstation.stationapi.api.client.registry.JsonModelRegistry;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.impl.client.model.FaceQuadPoint;
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
    public final List<FaceQuadPoint> quadPoints = new ArrayList<>();

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
        cuboids.forEach(jsonCuboidData -> {
            jsonCuboidData.postprocess();
            jsonCuboidData.faces.down.postprocess(this.textures.get(jsonCuboidData.faces.down.textureId));
            jsonCuboidData.faces.up.postprocess(this.textures.get(jsonCuboidData.faces.up.textureId));
            jsonCuboidData.faces.east.postprocess(this.textures.get(jsonCuboidData.faces.east.textureId));
            jsonCuboidData.faces.west.postprocess(this.textures.get(jsonCuboidData.faces.west.textureId));
            jsonCuboidData.faces.north.postprocess(this.textures.get(jsonCuboidData.faces.north.textureId));
            jsonCuboidData.faces.south.postprocess(this.textures.get(jsonCuboidData.faces.south.textureId));
        });
        updateUVs();
    }

    public void updateUVs() {
        quadPoints.clear();
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
            double[] uv;
            jsonFaceData = jsonFacesData.down;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zTo, uv[4], uv[7], BlockFaces.DOWN));
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zFrom, uv[0], uv[1], BlockFaces.DOWN));
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zFrom, uv[6], uv[5], BlockFaces.DOWN));
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zTo, uv[2], uv[3], BlockFaces.DOWN));
            jsonFaceData = jsonFacesData.up;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zTo, uv[2], uv[3], BlockFaces.UP));
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zFrom, uv[6], uv[5], BlockFaces.UP));
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zFrom, uv[0], uv[1], BlockFaces.UP));
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zTo, uv[4], uv[7], BlockFaces.UP));
            jsonFaceData = jsonFacesData.east;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zFrom, uv[2], uv[1], BlockFaces.EAST));
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zFrom, uv[0], uv[1], BlockFaces.EAST));
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zFrom, uv[0], uv[3], BlockFaces.EAST));
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zFrom, uv[2], uv[3], BlockFaces.EAST));
            jsonFaceData = jsonFacesData.west;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zTo, uv[0], uv[1], BlockFaces.WEST));
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zTo, uv[0], uv[3], BlockFaces.WEST));
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zTo, uv[2], uv[3], BlockFaces.WEST));
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zTo, uv[2], uv[1], BlockFaces.WEST));
            jsonFaceData = jsonFacesData.north;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zTo, uv[2], uv[1], BlockFaces.NORTH));
            quadPoints.add(FaceQuadPoint.get(xFrom, yTo, zFrom, uv[0], uv[1], BlockFaces.NORTH));
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zFrom, uv[0], uv[3], BlockFaces.NORTH));
            quadPoints.add(FaceQuadPoint.get(xFrom, yFrom, zTo, uv[2], uv[3], BlockFaces.NORTH));
            jsonFaceData = jsonFacesData.south;
            jsonFaceData.updateUVs();
            uv = jsonFaceData.getUv();
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zTo, uv[0], uv[3], BlockFaces.SOUTH));
            quadPoints.add(FaceQuadPoint.get(xTo, yFrom, zFrom, uv[2], uv[3], BlockFaces.SOUTH));
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zFrom, uv[2], uv[1], BlockFaces.SOUTH));
            quadPoints.add(FaceQuadPoint.get(xTo, yTo, zTo, uv[0], uv[1], BlockFaces.SOUTH));
        });
    }

    public Map<String, Atlas.Texture> getTextures() {
        return Collections.unmodifiableMap(textures);
    }

    public List<JsonCuboidData> getCuboids() {
        return Collections.unmodifiableList(cuboids);
    }
}
