package net.modificationstation.stationapi.api.network.codec;

/**
 * Similar to a {@link com.mojang.serialization.Codec}, but all values are encoded into a "stream" with no assigning identifier attached to them.
 * A stream codec represents a stream of memory.
 * @param <B>
 * @param <V>
 */
public interface StreamCodec<B, V> extends StreamDecoder<B, V>, StreamEncoder<B, V> {
    static <B, V> StreamCodec<B, V> ofMember(StreamMemberEncoder<B, V> encoder, StreamDecoder<B, V> decoder) {
        return new StreamCodec<>() {
            @Override
            public V decode(B buf) {
                return decoder.decode(buf);
            }

            @Override
            public void encode(B buf, V obj) {
                encoder.encode(obj, buf);
            }
        };
    }
}
