package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;

@Environment(EnvType.CLIENT)
public class BakedQuad {
   protected final int[] vertexData;
   protected final int colorIndex;
   protected final Direction face;
   protected final Sprite sprite;
   private final boolean shade;

   public BakedQuad(int[] vertexData, int colorIndex, Direction face, Sprite sprite, boolean shade) {
      this.vertexData = vertexData;
      this.colorIndex = colorIndex;
      this.face = face;
      this.sprite = sprite;
      this.shade = shade;
   }

   public int[] getVertexData() {
      return this.vertexData;
   }

   public boolean hasColour() {
      return this.colorIndex != -1;
   }

   public int getColorIndex() {
      return this.colorIndex;
   }

   public Direction getFace() {
      return this.face;
   }

   public boolean hasShade() {
      return this.shade;
   }
}
