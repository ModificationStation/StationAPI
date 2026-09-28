package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.MapCodec;

public record AtlasSourceType(MapCodec<? extends AtlasSource> codec) {}

