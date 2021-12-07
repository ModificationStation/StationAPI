package net.modificationstation.stationapi.impl.client.texture;

import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;

public interface BlockRendererCustomAccessor {

    StationBlockRenderer getStationBlockRenderer();

    BakedModelRenderer getBakedModelRenderer();
}
