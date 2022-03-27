package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class VertexFormat {
   private final ImmutableList<VertexFormatElement> elements;
   private final IntList offsets = new IntArrayList();
   private final int size;

   public VertexFormat(ImmutableList<VertexFormatElement> immutableList) {
      this.elements = immutableList;
      int i = 0;

      for (VertexFormatElement formatElement : immutableList) {
         this.offsets.add(i);
         i += formatElement.getSize();
      }

      this.size = i;
   }

   public String toString() {
      return "format: " + this.elements.size() + " elements: " + this.elements.stream().map(Object::toString).collect(Collectors.joining(" "));
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

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         VertexFormat vertexFormat = (VertexFormat)o;
         return this.size == vertexFormat.size && this.elements.equals(vertexFormat.elements);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.elements.hashCode();
   }

   public void startDrawing(long pointer) {
      int i = this.getVertexSize();
      List<VertexFormatElement> list = this.getElements();

      for (int j = 0; j < list.size(); ++j) {
         list.get(j).startDrawing(pointer + (long) this.offsets.getInt(j), i);
      }
   }

   public void endDrawing() {
      for (VertexFormatElement vertexFormatElement : this.getElements()) {
         vertexFormatElement.endDrawing();
      }
   }
}
