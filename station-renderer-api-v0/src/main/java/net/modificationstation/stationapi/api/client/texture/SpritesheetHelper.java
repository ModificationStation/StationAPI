package net.modificationstation.stationapi.api.client.texture;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.modificationstation.stationapi.api.util.Identifier;

public interface SpritesheetHelper {

    IntIntPair DEFAULT_RESOLUTION_MULTIPLIER = IntIntPair.of(1, 1);

    Identifier generateIdentifier(int textureIndex);

    IntIntPair getResolutionMultiplier(int textureIndex);
}
