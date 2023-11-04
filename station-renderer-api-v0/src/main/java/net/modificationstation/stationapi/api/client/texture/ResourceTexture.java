package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.resource.metadata.TextureResourceMetadata;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
public class ResourceTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final Identifier location;

   public ResourceTexture(Identifier location) {
      this.location = location;
   }

   public void load(ResourceManager manager) throws IOException {
      ResourceTexture.TextureData textureData = this.loadTextureData(manager);
      textureData.checkException();
      TextureResourceMetadata textureResourceMetadata = textureData.getMetadata();
      boolean bl3;
      boolean bl4;
      if (textureResourceMetadata != null) {
         bl3 = textureResourceMetadata.shouldBlur();
         bl4 = textureResourceMetadata.shouldClamp();
      } else {
         bl3 = false;
         bl4 = false;
      }

      NativeImage nativeImage = textureData.getImage();
      this.upload(nativeImage, bl3, bl4);
   }

   private void upload(NativeImage nativeImage, boolean blur, boolean clamp) {
      TextureUtil.prepareImage(this.getGlId(), 0, nativeImage.getWidth(), nativeImage.getHeight());
      nativeImage.upload(0, 0, 0, 0, 0, nativeImage.getWidth(), nativeImage.getHeight(), blur, clamp, false, true);
   }

   protected ResourceTexture.TextureData loadTextureData(ResourceManager resourceManager) {
      return ResourceTexture.TextureData.load(resourceManager, this.location);
   }

   @Environment(EnvType.CLIENT)
   public static class TextureData implements Closeable {
      @Nullable
      private final TextureResourceMetadata metadata;
      @Nullable
      private final NativeImage image;
      @Nullable
      private final IOException exception;

      public TextureData(@Nullable IOException exception) {
         this.exception = exception;
         this.metadata = null;
         this.image = null;
      }

      public TextureData(@Nullable TextureResourceMetadata metadata, @Nullable NativeImage image) {
         this.exception = null;
         this.metadata = metadata;
         this.image = image;
      }

      public static ResourceTexture.TextureData load(ResourceManager resourceManager, Identifier identifier) {
          try {
              NativeImage nativeImage;
              Resource resource = resourceManager.getResourceOrThrow(identifier);
              try (InputStream inputStream = resource.getInputStream()){
                  nativeImage = NativeImage.read(inputStream);
              }
              TextureResourceMetadata textureResourceMetadata = null;
              try {
                  textureResourceMetadata = resource.getMetadata().decode(TextureResourceMetadata.READER).orElse(null);
              }
              catch (RuntimeException runtimeException) {
                  LOGGER.warn("Failed reading metadata of: {}", identifier, runtimeException);
              }
              return new TextureData(textureResourceMetadata, nativeImage);
          }
          catch (IOException iOException) {
              return new TextureData(iOException);
          }
      }

      @Nullable
      public TextureResourceMetadata getMetadata() {
         return this.metadata;
      }

      public NativeImage getImage() throws IOException {
         if (this.exception != null) {
            throw this.exception;
         } else {
            return this.image;
         }
      }

      public void close() {
         if (this.image != null) {
            this.image.close();
         }

      }

      public void checkException() throws IOException {
         if (this.exception != null) {
            throw this.exception;
         }
      }
   }
}
