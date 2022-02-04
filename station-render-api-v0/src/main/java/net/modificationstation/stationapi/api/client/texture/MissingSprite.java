package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationFrameResourceMetadata;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.plugin.TextureManagerPlugin;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.impl.client.texture.IdentifierTextureManager;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class MissingSprite extends Sprite {
   private static final Identifier MISSINGNO = Identifier.of("missingno");
   @Nullable
   private static NativeImageBackedTexture texture;
   private static final Lazy<NativeImage> IMAGE = new Lazy<>(() -> {
      NativeImage nativeImage = new NativeImage(16, 16, false);
      int i = 0xff000000;
      int j = 0xfff800f8;

      for (int k = 0; k < 16; ++k) {
         for (int l = 0; l < 16; ++l) {
            if (k < 8 ^ l < 8) {
               nativeImage.setPixelColor(l, k, j);
            } else {
               nativeImage.setPixelColor(l, k, i);
            }
         }
      }

      nativeImage.untrack();
      return nativeImage;
   });
   private static final Sprite.Info INFO;

   private MissingSprite(SpriteAtlasTexture spriteAtlasTexture, int maxLevel, int atlasWidth, int atlasHeight, int x, int y) {
      super(spriteAtlasTexture, INFO, maxLevel, atlasWidth, atlasHeight, x, y, IMAGE.get());
   }

   public static MissingSprite getMissingSprite(SpriteAtlasTexture spriteAtlasTexture, int maxLevel, int atlasWidth, int atlasHeight, int x, int y) {
      return new MissingSprite(spriteAtlasTexture, maxLevel, atlasWidth, atlasHeight, x, y);
   }

   public static Identifier getMissingSpriteId() {
      return MISSINGNO;
   }

   public static Sprite.Info getMissingInfo() {
      return INFO;
   }

   public void close() {
      for(int i = 1; i < this.images.length; ++i) {
         this.images[i].close();
      }

   }

   public static NativeImageBackedTexture getMissingSpriteTexture() {
      if (texture == null) {
         texture = new NativeImageBackedTexture(IMAGE.get());
         //noinspection deprecation
         IdentifierTextureManager cTextureManager = ((TextureManagerPlugin.Provider) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getPlugin();
         cTextureManager.registerTexture(MISSINGNO, texture);
      }

      return texture;
   }

   static {
      INFO = new Sprite.Info(MISSINGNO, 16, 16, new AnimationResourceMetadata(Lists.newArrayList(new AnimationFrameResourceMetadata(0, -1)), 16, 16, 1, false));
   }
}
