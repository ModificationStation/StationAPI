package net.modificationstation.stationapi.api.client.texture;

import java.util.ArrayList;
import java.util.List;

public class TextureSet {

    private final List<Atlas> atlases = new ArrayList<>();

    public TextureSet() {
    }

    public Atlas get(int id) {
        return atlases.get(id);
    }

    public int add(Atlas atlas) {
        atlases.add(atlas);
        return atlases.size() - 1;
    }
}
