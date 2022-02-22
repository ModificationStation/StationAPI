package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.function.*;

@Environment(EnvType.CLIENT)
public class VertexFormatElement {
   private static final Logger LOGGER = LogManager.getLogger();
   private final VertexFormatElement.Format format;
   private final VertexFormatElement.Type type;
   private final int index;
   private final int count;
   private final int size;

   public VertexFormatElement(int index, VertexFormatElement.Format format, VertexFormatElement.Type type, int count) {
      if (this.isValidType(index, type)) {
         this.type = type;
      } else {
         LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
         this.type = VertexFormatElement.Type.UV;
      }

      this.format = format;
      this.index = index;
      this.count = count;
      this.size = format.getSize() * this.count;
   }

   private boolean isValidType(int index, VertexFormatElement.Type type) {
      return index == 0 || type == VertexFormatElement.Type.UV;
   }

   public final VertexFormatElement.Format getFormat() {
      return this.format;
   }

   public final VertexFormatElement.Type getType() {
      return this.type;
   }

   public final int getIndex() {
      return this.index;
   }

   public String toString() {
      return this.count + "," + this.type.getName() + "," + this.format.getName();
   }

   public final int getSize() {
      return this.size;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         VertexFormatElement vertexFormatElement = (VertexFormatElement)o;
         if (this.count != vertexFormatElement.count) {
            return false;
         } else if (this.index != vertexFormatElement.index) {
            return false;
         } else if (this.format != vertexFormatElement.format) {
            return false;
         } else {
            return this.type == vertexFormatElement.type;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int i = this.format.hashCode();
      i = 31 * i + this.type.hashCode();
      i = 31 * i + this.index;
      i = 31 * i + this.count;
      return i;
   }

   public void startDrawing(long pointer, int stride) {
      this.type.startDrawing(this.count, this.format.getGlId(), stride, pointer, this.index);
   }

   public void endDrawing() {
      this.type.endDrawing(this.index);
   }

   @Environment(EnvType.CLIENT)
   public enum Format {
      FLOAT(4, "Float", 5126),
      UBYTE(1, "Unsigned Byte", 5121),
      BYTE(1, "Byte", 5120),
      USHORT(2, "Unsigned Short", 5123),
      SHORT(2, "Short", 5122),
      UINT(4, "Unsigned Int", 5125),
      INT(4, "Int", 5124);

      private final int size;
      private final String name;
      private final int glId;

      Format(int size, String name, int glId) {
         this.size = size;
         this.name = name;
         this.glId = glId;
      }

      public int getSize() {
         return this.size;
      }

      public String getName() {
         return this.name;
      }

      public int getGlId() {
         return this.glId;
      }
   }

   @Environment(EnvType.CLIENT)
   public enum Type {
      POSITION("Position", (i, j, k, l, m) -> {
         GL11.glVertexPointer(i, j, k, l);
         GL11.glEnableClientState(32884);
      }, (i) -> {
         GL11.glDisableClientState(32884);
      }),
      NORMAL("Normal", (i, j, k, l, m) -> {
         GL11.glNormalPointer(j, k, l);
         GL11.glEnableClientState(32885);
      }, (i) -> {
         GL11.glDisableClientState(32885);
      }),
      COLOR("Vertex Color", (i, j, k, l, m) -> {
         GL11.glColorPointer(i, j, k, l);
         GL11.glEnableClientState(32886);
      }, (i) -> {
         GL11.glDisableClientState(32886);
      }),
      UV("UV", (i, j, k, l, m) -> {
         GL13.glClientActiveTexture('蓀' + m);
         GL11.glTexCoordPointer(i, j, k, l);
         GL11.glEnableClientState(32888);
         GL13.glClientActiveTexture(33984);
      }, (i) -> {
         GL13.glClientActiveTexture('蓀' + i);
         GL11.glDisableClientState(32888);
         GL13.glClientActiveTexture(33984);
      }),
      PADDING("Padding", (i, j, k, l, m) -> {
      }, (i) -> {
      }),
      GENERIC("Generic", (i, j, k, l, m) -> {
         GL20.glEnableVertexAttribArray(m);
         GL20.glVertexAttribPointer(m, i, j, false, k, l);
      }, GL20::glEnableVertexAttribArray);

      private final String name;
      private final VertexFormatElement.Type.Starter starter;
      private final IntConsumer finisher;

      Type(String name, VertexFormatElement.Type.Starter starter, IntConsumer intConsumer) {
         this.name = name;
         this.starter = starter;
         this.finisher = intConsumer;
      }

      private void startDrawing(int count, int glId, int stride, long pointer, int elementIndex) {
         this.starter.setupBufferState(count, glId, stride, pointer, elementIndex);
      }

      public void endDrawing(int elementIndex) {
         this.finisher.accept(elementIndex);
      }

      public String getName() {
         return this.name;
      }

      @Environment(EnvType.CLIENT)
      interface Starter {
         void setupBufferState(int count, int glId, int stride, long pointer, int elementIndex);
      }
   }
}
