package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Sprite {
    private final Identifier id;
    private final Resource resource;
    private final AtomicReference<NativeImage> image = new AtomicReference<>();
    private final AtomicInteger regionCount;

    public Sprite(Identifier id, Resource resource, int regionCount) {
        this.id = id;
        this.resource = resource;
        this.regionCount = new AtomicInteger(regionCount);
    }

    public NativeImage read() throws IOException {
        NativeImage nativeImage = this.image.get();
        if (nativeImage == null) synchronized (this) {
            nativeImage = this.image.get();
            if (nativeImage == null) try (InputStream inputStream = this.resource.getInputStream()) {
                nativeImage = NativeImage.read(inputStream);
                this.image.set(nativeImage);
            } catch (IOException iOException) {
                throw new IOException("Failed to load image " + this.id, iOException);
            }
        }
        return nativeImage;
    }

    public void close() {
        NativeImage nativeImage;
        int i = this.regionCount.decrementAndGet();
        if (i <= 0 && (nativeImage = this.image.getAndSet(null)) != null) nativeImage.close();
    }
}

