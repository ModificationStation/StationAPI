package net.modificationstation.stationapi.impl.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.util.Null;

import java.util.*;

@RequiredArgsConstructor
public class JsonModelData {

    public String parent = null;
    @SuppressWarnings("FieldMayBeFinal") // had to make it non-final with a getter because javac is retarded
    @Getter
    private boolean ambientocclusion = true;
    public Map<String, String> textures = Null.get();
    public List<JsonCuboidData> elements = Null.get();
    public GuiLightType gui_light = GuiLightType.SIDE;
}
