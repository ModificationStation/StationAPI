package net.modificationstation.stationapi.api.client.resource.metadata;

import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.JsonHelper;

@Environment(EnvType.CLIENT)
public class TextureResourceMetadataReader implements ResourceMetadataReader<TextureResourceMetadata> {
   public TextureResourceMetadata fromJson(JsonObject jsonObject) {
      boolean bl = JsonHelper.getBoolean(jsonObject, "blur", false);
      boolean bl2 = JsonHelper.getBoolean(jsonObject, "clamp", false);
      return new TextureResourceMetadata(bl, bl2);
   }

   public String getKey() {
      return "texture";
   }
}
