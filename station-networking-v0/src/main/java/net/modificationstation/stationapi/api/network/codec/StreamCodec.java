package net.modificationstation.stationapi.api.network.codec;

/**
 * Similar to a {@link com.mojang.serialization.Codec}, but all values are encoded into a "stream" with no assigning identifier attached to them.
 * A stream codec represents a stream of memory.
 * @param <B>
 * @param <V>
 */
public interface StreamCodec<B, V> extends StreamDecoder<B, V>, StreamEncoder<B, V> {
}
