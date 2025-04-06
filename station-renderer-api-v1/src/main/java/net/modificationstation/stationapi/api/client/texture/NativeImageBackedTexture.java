package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NativeImageBackedTexture extends AbstractTexture {
   private static final Logger field_25794 = LogManager.getLogger();
   @Nullable
   private NativeImage image;

   public NativeImageBackedTexture(@Nullable NativeImage image) {
      this.image = image;
      TextureUtil.prepareImage(this.getGlId(), this.image.getWidth(), this.image.getHeight());
      this.upload();
   }

   public NativeImageBackedTexture(int width, int height, boolean useStb) {
      this.image = new NativeImage(width, height, useStb);
      TextureUtil.prepareImage(this.getGlId(), this.image.getWidth(), this.image.getHeight());
   }

   public void load(ResourceManager manager) {}

   public void upload() {
      if (this.image != null) {
         this.bindTexture();
         this.image.upload(0, 0, 0, false);
      } else {
         field_25794.warn("Trying to upload disposed texture {}", this.getGlId());
      }

   }

   @Nullable
   public NativeImage getImage() {
      return this.image;
   }

   public void setImage(NativeImage image) {
      if (this.image != null) {
         this.image.close();
      }

      this.image = image;
   }

   public void close() {
      if (this.image != null) {
         this.image.close();
         this.clearGlId();
         this.image = null;
      }

   }
}
