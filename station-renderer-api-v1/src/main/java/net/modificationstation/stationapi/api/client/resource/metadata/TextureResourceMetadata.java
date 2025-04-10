package net.modificationstation.stationapi.api.client.resource.metadata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TextureResourceMetadata {
   public static final TextureResourceMetadataReader READER = new TextureResourceMetadataReader();
   private final boolean blur;
   private final boolean clamp;

   public TextureResourceMetadata(boolean blur, boolean bl) {
      this.blur = blur;
      this.clamp = bl;
   }

   public boolean shouldBlur() {
      return this.blur;
   }

   public boolean shouldClamp() {
      return this.clamp;
   }
}
