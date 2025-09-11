package net.modificationstation.stationapi.api.network.codec;

@FunctionalInterface
public interface StreamMemberEncoder<O, T> {
    void encode(T obj, O encoder);
}
