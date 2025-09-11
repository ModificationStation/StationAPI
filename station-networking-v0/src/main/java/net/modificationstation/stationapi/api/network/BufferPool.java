package net.modificationstation.stationapi.api.network;

import org.jetbrains.annotations.ApiStatus;

import java.lang.ref.Cleaner;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

@ApiStatus.Experimental
public class BufferPool {
    public static final int BUFFER_SIZE = 16_383;
    private static final Cleaner CLEANER = Cleaner.create();
    private final Queue<SoftReference<ByteBuffer>> buffers = new LinkedList<>();

    public ByteBuffer get() {
        ByteBuffer ret;
        SoftReference<ByteBuffer> ref;
        // Check if the pool has any buffer's available
        while ((ref = buffers.poll()) != null) {
            if ((ret = ref.get()) != null) {
                return ret;
            }
        }
        // If not buffers are available create one
        return ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    public void register(Object ref, ByteBuffer buffer) {
        CLEANER.register(ref, () -> buffers.add(new SoftReference<>(buffer)));
    }

    public void clear() {
        buffers.clear();
    }

    public int size() {
        return buffers.size();
    }

    public boolean offer(ByteBuffer buffer) {
        return buffers.offer(new SoftReference<>(buffer.clear()));
    }
}
