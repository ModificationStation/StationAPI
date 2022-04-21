package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.stream.Collectors;

@Environment(value=EnvType.CLIENT)
public class VertexFormat {
   private final ImmutableList<VertexFormatElement> elements;
   private final ImmutableMap<String, VertexFormatElement> elementMap;
   private final IntList offsets = new IntArrayList();
   private final int size;
   private int vertexArray;
   private int vertexBuffer;
   private int elementBuffer;

   public VertexFormat(ImmutableMap<String, VertexFormatElement> elementMap) {
      this.elementMap = elementMap;
      this.elements = elementMap.values().asList();
      int i = 0;
      for (VertexFormatElement vertexFormatElement : elementMap.values()) {
         this.offsets.add(i);
         i += vertexFormatElement.getByteLength();
      }
      this.size = i;
   }

   public String toString() {
      return "format: " + this.elementMap.size() + " elements: " + this.elementMap.entrySet().stream().map(Object::toString).collect(Collectors.joining(" "));
   }

   public int getVertexSizeInteger() {
      return this.getVertexSize() / 4;
   }

   public int getVertexSize() {
      return this.size;
   }

   public ImmutableList<VertexFormatElement> getElements() {
      return this.elements;
   }

   public ImmutableList<String> getShaderAttributes() {
      return this.elementMap.keySet().asList();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || this.getClass() != o.getClass()) {
         return false;
      }
      VertexFormat vertexFormat = (VertexFormat)o;
      if (this.size != vertexFormat.size) {
         return false;
      }
      return this.elementMap.equals(vertexFormat.elementMap);
   }

   public int hashCode() {
      return this.elementMap.hashCode();
   }

   public void startDrawing() {
      this.innerStartDrawing();
   }

   private void innerStartDrawing() {
      int i = this.getVertexSize();
      ImmutableList<VertexFormatElement> list = this.getElements();
      for (int j = 0; j < list.size(); ++j) {
         list.get(j).startDrawing(j, this.offsets.getInt(j), i);
      }
   }

   public void endDrawing() {
      this.innerEndDrawing();
   }

   private void innerEndDrawing() {
      ImmutableList<VertexFormatElement> immutableList = this.getElements();
      for (int i = 0; i < immutableList.size(); ++i) {
         VertexFormatElement vertexFormatElement = immutableList.get(i);
         vertexFormatElement.endDrawing(i);
      }
   }

   public int getVertexArray() {
      if (this.vertexArray == 0) {
         this.vertexArray = GL30.glGenVertexArrays();
      }
      return this.vertexArray;
   }

   public int getVertexBuffer() {
      if (this.vertexBuffer == 0) {
         this.vertexBuffer = GL15.glGenBuffers();
      }
      return this.vertexBuffer;
   }

   public int getElementBuffer() {
      if (this.elementBuffer == 0) {
         this.elementBuffer = GL15.glGenBuffers();
      }
      return this.elementBuffer;
   }

   @Environment(value=EnvType.CLIENT)
   public enum DrawMode {
      LINES(4, 2, 2),
      LINE_STRIP(5, 2, 1),
      DEBUG_LINES(1, 2, 2),
      DEBUG_LINE_STRIP(3, 2, 1),
      TRIANGLES(4, 3, 3),
      TRIANGLE_STRIP(5, 3, 1),
      TRIANGLE_FAN(6, 3, 1),
      QUADS(4, 4, 4);

      public final int mode;
      public final int vertexCount;
      public final int size;

      DrawMode(int mode, int vertexCount, int size) {
         this.mode = mode;
         this.vertexCount = vertexCount;
         this.size = size;
      }

      public int getSize(int vertexCount) {
         return switch (this) {
            case LINE_STRIP, DEBUG_LINES, DEBUG_LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
            case LINES, QUADS -> vertexCount / 4 * 6;
         };
      }
   }

   @Environment(value=EnvType.CLIENT)
   public enum IntType {
      BYTE(5121, 1),
      SHORT(5123, 2),
      INT(5125, 4);

      public final int type;
      public final int size;

      IntType(int type, int size) {
         this.type = type;
         this.size = size;
      }

      public static IntType getSmallestTypeFor(int number) {
         if ((number & 0xFFFF0000) != 0) {
            return INT;
         }
         if ((number & 0xFF00) != 0) {
            return SHORT;
         }
         return BYTE;
      }
   }
}

