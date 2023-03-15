package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;

public record AtlasSourceType(Codec<? extends AtlasSource> codec) {}

