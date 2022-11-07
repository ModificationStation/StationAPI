package net.modificationstation.stationapi.api.util.dynamic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.util.Util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public final class DynamicSerializableUuid {
    public static final Codec<UUID> CODEC = Codec.INT_STREAM.comapFlatMap(uuidStream -> Util.toArray(uuidStream, 4).map(DynamicSerializableUuid::toUuid), uuid -> Arrays.stream(DynamicSerializableUuid.toIntArray(uuid)));
    public static final int BYTE_ARRAY_SIZE = 16;
    private static final String OFFLINE_PLAYER_UUID_PREFIX = "OfflinePlayer:";

    private DynamicSerializableUuid() {
    }

    public static UUID toUuid(int[] array) {
        return new UUID((long)array[0] << 32 | (long)array[1] & 0xFFFFFFFFL, (long)array[2] << 32 | (long)array[3] & 0xFFFFFFFFL);
    }

    public static int[] toIntArray(UUID uuid) {
        long l = uuid.getMostSignificantBits();
        long m = uuid.getLeastSignificantBits();
        return DynamicSerializableUuid.toIntArray(l, m);
    }

    private static int[] toIntArray(long uuidMost, long uuidLeast) {
        return new int[]{(int)(uuidMost >> 32), (int)uuidMost, (int)(uuidLeast >> 32), (int)uuidLeast};
    }

    public static byte[] toByteArray(UUID uuid) {
        byte[] bs = new byte[16];
        ByteBuffer.wrap(bs).order(ByteOrder.BIG_ENDIAN).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
        return bs;
    }

    public static UUID toUuid(Dynamic<?> dynamic) {
        int[] is = dynamic.asIntStream().toArray();
        if (is.length != 4) {
            throw new IllegalArgumentException("Could not read UUID. Expected int-array of length 4, got " + is.length + ".");
        }
        return DynamicSerializableUuid.toUuid(is);
    }

    public static UUID getOfflinePlayerUuid(String nickname) {
        return UUID.nameUUIDFromBytes((OFFLINE_PLAYER_UUID_PREFIX + nickname).getBytes(StandardCharsets.UTF_8));
    }
}

