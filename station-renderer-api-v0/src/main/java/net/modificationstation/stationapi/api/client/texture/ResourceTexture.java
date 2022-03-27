package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.resource.metadata.TextureResourceMetadata;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ResourceTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final Identifier location;

   public ResourceTexture(Identifier location) {
      this.location = location;
   }

   public void load(TexturePack manager) throws IOException {
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
      TextureUtil.allocate(this.getGlId(), 0, nativeImage.getWidth(), nativeImage.getHeight());
      nativeImage.upload(0, 0, 0, 0, 0, nativeImage.getWidth(), nativeImage.getHeight(), blur, clamp, false, true);
   }

   protected ResourceTexture.TextureData loadTextureData(TexturePack resourceManager) {
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

      public static ResourceTexture.TextureData load(TexturePack resourceManager, Identifier identifier) {
         try {
            Resource resource = Resource.of(resourceManager.getResourceAsStream(ResourceManager.ASSETS.toPath(identifier)));

            ResourceTexture.TextureData var6;
            try {
               NativeImage nativeImage = NativeImage.read(resource.getInputStream());
               TextureResourceMetadata textureResourceMetadata = null;

               try {
                  textureResourceMetadata = resource.getMetadata(TextureResourceMetadata.READER);
               } catch (RuntimeException var17) {
                  ResourceTexture.LOGGER.warn("Failed reading metadata of: {}", identifier, var17);
               }

               var6 = new ResourceTexture.TextureData(textureResourceMetadata, nativeImage);
            } finally {
               resource.close();
            }

            return var6;
         } catch (IOException var20) {
            return new ResourceTexture.TextureData(var20);
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
