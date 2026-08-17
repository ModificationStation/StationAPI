package net.modificationstation.stationapi.api.vanillafix.util;

import net.minecraft.nbt.NbtCompound;

public record MetaIntervalToTag(int start, int end, NbtCompound tag, int outputMeta) {
}
