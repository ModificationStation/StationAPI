package net.modificationstation.stationapi.api.network.codec;

/**
 * Represents a generic encoder.
 * @param <S> The serializer use to serialize the object.
 * @param <T> The object to serialize.
 */
public interface StreamEncoder<S, T> {
    void encode(S encoder, T obj);
}
