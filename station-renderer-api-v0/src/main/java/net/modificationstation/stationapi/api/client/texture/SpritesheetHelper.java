package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.util.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

public interface SpritesheetHelper {

    BiTuple<Integer, Integer> DEFAULT_RESOLUTION_MULTIPLIER = Tuple.tuple(1, 1);

    Identifier generateIdentifier(int textureIndex);

    BiTuple<Integer, Integer> getResolutionMultiplier(int textureIndex);
}
