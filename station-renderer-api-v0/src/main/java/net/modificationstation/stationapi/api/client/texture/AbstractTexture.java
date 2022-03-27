package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
public abstract class AbstractTexture implements AutoCloseable {
   protected int glId = -1;
   protected boolean bilinear;
   protected boolean mipmap;

   public void setFilter(boolean bilinear, boolean mipmap) {
      this.bilinear = bilinear;
      this.mipmap = mipmap;
      int k;
      short l;
      if (bilinear) {
         k = mipmap ? 9987 : 9729;
         l = 9729;
      } else {
         k = mipmap ? 9986 : 9728;
         l = 9728;
      }

      GL11.glTexParameteri(3553, 10241, k);
      GL11.glTexParameteri(3553, 10240, l);
   }

   public int getGlId() {
      if (this.glId == -1) {
         this.glId = TextureUtil.generateId();
      }

      return this.glId;
   }

   public void clearGlId() {
      if (this.glId != -1) {
         TextureUtil.deleteId(this.glId);
         this.glId = -1;
      }
   }

   public abstract void load(TexturePack manager) throws IOException;

   public void bindTexture() {
      GL11.glBindTexture(3553, this.getGlId());
   }

   public void registerTexture(TextureManager textureManager, TexturePack resourceManager, Identifier identifier, Executor executor) {
      StationTextureManager.get(textureManager).registerTexture(identifier, this);
   }

   @Override
   public void close() {}
}
