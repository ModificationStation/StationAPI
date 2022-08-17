package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_214;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.util.collection.LinkedList;
import net.modificationstation.stationapi.mixin.render.client.MinecraftAccessor;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class VboPool implements AutoCloseable {

    @SuppressWarnings("deprecation")
    private static final MinecraftAccessor mc = ((MinecraftAccessor) FabricLoader.getInstance().getGameInstance());

    private final VertexFormat format;
    private int vertexArrayId = GlStateManager._glGenVertexArrays();
    private int vertexBufferId = GlStateManager._glGenBuffers();
    private int capacity = 4096;
    private int nextPos = 0;
    private int size;
    private final LinkedList<Pos> posList = new LinkedList<>();
    private Pos compactPosLast = null;
    private int curBaseInstance;

    private IntBuffer bufferIndirect = class_214.method_745(this.capacity * 5);
    private final int vertexBytes;
    private VertexFormat.DrawMode drawMode = VertexFormat.DrawMode.QUADS;

    public VboPool(VertexFormat format) {
        this.format = format;
        vertexBytes = format.getVertexSizeByte();
        this.bindBuffer();
        long i = this.toBytes(this.capacity);
        GlStateManager._glBufferData(GL15.GL_ARRAY_BUFFER, i, GL15.GL_STATIC_DRAW);
        this.unbindBuffer();
    }

    @Override
    public void close() throws Exception {
        if (this.vertexBufferId > 0) {
            RenderSystem.glDeleteBuffers(this.vertexBufferId);
            this.vertexBufferId = 0;
        }
    }

    public void bufferData(ByteBuffer data, Pos poolPos) {
        if (this.vertexBufferId >= 0) {
            int position = poolPos.getPosition();
            int size = poolPos.getSize();
            int bufferSize = this.toVertex(data.limit());

            if (bufferSize <= 0) {
                if (position >= 0) {
                    poolPos.setPosition(-1);
                    poolPos.setSize(0);
                    this.posList.remove(poolPos.getNode());
                    this.size -= size;
                }
            } else {
                if (bufferSize > size) {
                    poolPos.setPosition(this.nextPos);
                    poolPos.setSize(bufferSize);
                    this.nextPos += bufferSize;

                    if (position >= 0) {
                        this.posList.remove(poolPos.getNode());
                    }

                    this.posList.addLast(poolPos.getNode());
                }

                poolPos.setSize(bufferSize);
                this.size += bufferSize - size;
                this.checkVboSize(poolPos.getPositionNext());
                long l = this.toBytes(poolPos.getPosition());
                this.bindVertexArray();
                this.bindBuffer();
                GlStateManager._glBufferSubData(GL15.GL_ARRAY_BUFFER, l, data);
                this.unbindBuffer();
                unbindVertexArray();

                if (this.nextPos > this.size * 11 / 10) {
                    this.compactRanges(1);
                }
            }
        }
    }

    private void compactRanges(int countMax) {
        if (!this.posList.isEmpty()) {
            Pos vborange = this.compactPosLast;

            if (vborange == null || !this.posList.contains(vborange.getNode())) {
                vborange = this.posList.getFirst().getItem();
            }

            int i;
            Pos vborange1 = vborange.getPrev();

            if (vborange1 == null) {
                i = 0;
            } else {
                i = vborange1.getPositionNext();
            }

            int j = 0;

            while (vborange != null && j < countMax) {
                ++j;

                if (vborange.getPosition() == i) {
                    i += vborange.getSize();
                    vborange = vborange.getNext();
                } else {
                    int k = vborange.getPosition() - i;

                    if (vborange.getSize() <= k) {
                        this.copyVboData(vborange.getPosition(), i, vborange.getSize());
                        vborange.setPosition(i);
                        i += vborange.getSize();
                        vborange = vborange.getNext();
                    } else {
                        this.checkVboSize(this.nextPos + vborange.getSize());
                        this.copyVboData(vborange.getPosition(), this.nextPos, vborange.getSize());
                        vborange.setPosition(this.nextPos);
                        this.nextPos += vborange.getSize();
                        Pos vborange2 = vborange.getNext();
                        this.posList.remove(vborange.getNode());
                        this.posList.addLast(vborange.getNode());
                        vborange = vborange2;
                    }
                }
            }

            if (vborange == null) {
                this.nextPos = this.posList.getLast().getItem().getPositionNext();
            }

            this.compactPosLast = vborange;
        }
    }

    private long toBytes(int vertex)
    {
        return (long)vertex * (long)this.vertexBytes;
    }

    private int toVertex(long bytes) {
        return (int)(bytes / (long)this.vertexBytes);
    }

    private void checkVboSize(int sizeMin) {
        if (this.capacity < sizeMin) {
            this.expandVbo(sizeMin);
        }
    }

    private void copyVboData(int posFrom, int posTo, int size) {
        long i = this.toBytes(posFrom);
        long j = this.toBytes(posTo);
        long k = this.toBytes(size);
        GlStateManager._glBindBuffer(GL31.GL_COPY_READ_BUFFER, this.vertexBufferId);
        GlStateManager._glBindBuffer(GL31.GL_COPY_WRITE_BUFFER, this.vertexBufferId);
        GlStateManager._glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL31.GL_COPY_WRITE_BUFFER, i, j, k);
        mc.stationapi$printOpenGLError("Copy VBO range");
        GlStateManager._glBindBuffer(GL31.GL_COPY_READ_BUFFER, 0);
        GlStateManager._glBindBuffer(GL31.GL_COPY_WRITE_BUFFER, 0);
    }

    private void expandVbo(int sizeMin) {
        int i;

        i = this.capacity * 6 / 4;
        while (i < sizeMin) {
            i = i * 6 / 4;
        }

        long j = this.toBytes(this.capacity);
        long k = this.toBytes(i);
        int l = GlStateManager._glGenBuffers();
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, l);
        GlStateManager._glBufferData(GL15.GL_ARRAY_BUFFER, k, GL15.GL_STATIC_DRAW);
        mc.stationapi$printOpenGLError("Expand VBO");
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GlStateManager._glBindBuffer(GL31.GL_COPY_READ_BUFFER, this.vertexBufferId);
        GlStateManager._glBindBuffer(GL31.GL_COPY_WRITE_BUFFER, l);
        GlStateManager._glCopyBufferSubData(GL31.GL_COPY_READ_BUFFER, GL31.GL_COPY_WRITE_BUFFER, 0L, 0L, j);
        mc.stationapi$printOpenGLError("Copy VBO: " + k);
        GlStateManager._glBindBuffer(GL31.GL_COPY_READ_BUFFER, 0);
        GlStateManager._glBindBuffer(GL31.GL_COPY_WRITE_BUFFER, 0);
        GlStateManager._glDeleteBuffers(this.vertexBufferId);
        this.bufferIndirect = class_214.method_745(i * 5);
        this.vertexBufferId = l;
        this.capacity = i;
    }

    public void bindVertexArray() {
        GlStateManager._glBindVertexArray(this.vertexArrayId);
    }

    public void bindBuffer() {
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBufferId);
    }

    public void upload(VertexFormat.DrawMode drawMode, Pos range) {
        if (this.drawMode != drawMode) {
            if (this.bufferIndirect.position() > 0) {
                throw new IllegalArgumentException("Mixed region draw modes: " + this.drawMode + " != " + drawMode);
            }

            this.drawMode = drawMode;
        }

        this.bufferIndirect.put(drawMode.getIndexCount(range.getSize()));
        bufferIndirect.put(1);
        this.bufferIndirect.put(0);
        bufferIndirect.put(range.getPosition());
        bufferIndirect.put(curBaseInstance++);
    }

    public void drawAll() {
        this.bindVertexArray();
        this.bindBuffer();
        format.setupState();

        int i = this.drawMode.getIndexCount(this.nextPos);
        RenderSystem.IndexBuffer autostorageindexbuffer = RenderSystem.getSequentialBuffer(this.drawMode);
        VertexFormat.IndexType indextype = autostorageindexbuffer.getIndexType();
        autostorageindexbuffer.bindAndGrow(i);
//        GlStateManager._glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, autostorageindexbuffer.getId());
        this.bufferIndirect.flip();
        GlStateManager._multiDrawElementsIndirect(this.drawMode.glMode, indextype.glType, this.bufferIndirect, bufferIndirect.limit() / 5, 0);
        this.bufferIndirect.limit(this.bufferIndirect.capacity());

        if (this.nextPos > this.size * 11 / 10) {
            this.compactRanges(1);
        }
        curBaseInstance = 0;
    }

    public void unbindBuffer() {
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static void unbindVertexArray() {
        GlStateManager._glBindVertexArray(0);
    }

    public void deleteGlBuffers() {
        if (this.vertexArrayId >= 0) {
            RenderSystem.glDeleteVertexArrays(this.vertexArrayId);
            this.vertexArrayId = -1;
        }
        if (this.vertexBufferId >= 0) {
            RenderSystem.glDeleteBuffers(this.vertexBufferId);
            this.vertexBufferId = -1;
        }
    }

    public static class Pos {

        private int position = -1;
        private int size = 0;
        private final LinkedList.Node<Pos> node = new LinkedList.Node<>(this);

        public int getPosition() {
            return this.position;
        }

        public int getSize() {
            return this.size;
        }

        public int getPositionNext() {
            return this.position + this.size;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public LinkedList.Node<Pos> getNode() {
            return this.node;
        }

        public Pos getPrev() {
            LinkedList.Node<Pos> node = this.node.getPrev();
            return node == null ? null : node.getItem();
        }

        public Pos getNext() {
            LinkedList.Node<Pos> node = this.node.getNext();
            return node == null ? null : node.getItem();
        }

        public String toString() {
            return this.position + "/" + this.size + "/" + (this.position + this.size);
        }
    }
}
