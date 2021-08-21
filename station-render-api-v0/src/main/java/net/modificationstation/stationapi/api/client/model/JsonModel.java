package net.modificationstation.stationapi.api.client.model;

import com.google.gson.Gson;
import net.modificationstation.stationapi.api.client.registry.JsonModelRegistry;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas;
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

    public JsonModel(final Identifier id) {
        JsonModelRegistry.INSTANCE.register(id, this);
        this.id = id;
        modelPath = ResourceManager.parsePath(id, "/" + MODID + "/models", "json");
        reload();
    }

    public void reload() {
        final JsonModelData data = new Gson().fromJson(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(modelPath), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")), JsonModelData.class);
        final Map<String, Atlas.Texture> textures = new HashMap<>();
        textures.put("#missing", TextureInit.JSON_MISSING);
        data.getTextures().forEach((textureId, texturePath) -> textures.put("#" + textureId, JsonModelAtlas.STATION_JSON_MODELS.addTexture(ResourceManager.parsePath(of(texturePath), "/" + MODID + "/textures", "png"))));
        this.textures.clear();
        this.textures.putAll(textures);
        cuboids.clear();
        cuboids.addAll(data.getElements());
        updateUVs();
    }

    public void updateUVs() {
        cuboids.forEach(jsonCuboidData -> {
            JsonFacesData jsonFacesData = jsonCuboidData.getFaces();
            JsonFaceData jsonFaceData;
            jsonFaceData = jsonFacesData.getDown();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            jsonFaceData = jsonFacesData.getUp();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            jsonFaceData = jsonFacesData.getEast();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            jsonFaceData = jsonFacesData.getWest();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            jsonFaceData = jsonFacesData.getNorth();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
            jsonFaceData = jsonFacesData.getSouth();
            jsonFaceData.updateUVs(textures.get(jsonFaceData.texture));
        });
    }

    public Map<String, Atlas.Texture> getTextures() {
        return Collections.unmodifiableMap(textures);
    }

    public List<JsonCuboidData> getCuboids() {
        return Collections.unmodifiableList(cuboids);
    }
}
