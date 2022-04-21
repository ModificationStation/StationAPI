package net.modificationstation.stationapi.impl.client.resource;

import lombok.Getter;
import net.modificationstation.stationapi.api.client.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ResourceImpl extends InputStream implements Resource {

    private final InputStream resource;
    private final InputStream meta;
    @Getter
    private final String resourcePackName;

    public ResourceImpl(InputStream resource, @Nullable InputStream meta, String resourcePackName) {
        this.resource = resource;
        this.meta = meta;
        this.resourcePackName = resourcePackName;
    }

    @Override
    public InputStream getInputStream() {
        return resource;
    }

    @Override
    public Optional<InputStream> getMeta() {
        return Optional.ofNullable(meta);
    }

    @Override
    public int read() throws IOException {
        return resource.read();
    }

    @Override
    public int read(@SuppressWarnings("NullableProblems") @NotNull byte[] b) throws IOException {
        return resource.read(b);
    }

    @Override
    public int read(@SuppressWarnings("NullableProblems") @NotNull byte[] b, int off, int len) throws IOException {
        return resource.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return resource.skip(n);
    }

    @Override
    public int available() throws IOException {
        return resource.available();
    }

    @Override
    public void close() throws IOException {
        resource.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        resource.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        resource.reset();
    }

    @Override
    public boolean markSupported() {
        return resource.markSupported();
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ResourceImpl ? equals(obj) : resource.equals(obj);
    }

    @Override
    public String toString() {
        return resource.toString();
    }
}
