package net.modificationstation.stationapi.impl.client.texture;

import net.modificationstation.stationapi.api.util.UnsafeProvider;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import java.util.*;

public final class NativeABGRImage implements AutoCloseable {

    private final int width, height;
    private final long sizeBytes;
    private long imagePointer;

    public NativeABGRImage(int width, int height, long imagePointer) {
        this.width = width;
        this.height = height;
        sizeBytes = (long) width * height * 4;
        this.imagePointer = imagePointer;
    }

    public NativeABGRImage(int width, int height) {
        this.width = width;
        this.height = height;
        sizeBytes = (long) width * height * 4;
        imagePointer = UnsafeProvider.theUnsafe.allocateMemory(sizeBytes);
    }

    public static void read(InputStream inputStream) {
        try (ImageInputStream imageStream = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageStream);
            while(iter.hasNext()) {
                ImageReader reader = iter.next();
                try {
                    reader.setInput(imageStream);
                    int width = reader.getWidth(reader.getMinIndex());
                    int height = reader.getHeight(reader.getMinIndex());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (imagePointer != 0) {
            UnsafeProvider.theUnsafe.freeMemory(imagePointer);
            imagePointer = 0;
        }
    }
}
