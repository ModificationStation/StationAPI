package net.modificationstation.stationapi.api.network.codec;

/**
 * Represents a generic decoder.
 * @param <I> The deserializer that decodes the object.
 * @param <T> The object that will get deserialized.
 */
public interface StreamDecoder<I, T> {
    T decode(I decoder);
}
