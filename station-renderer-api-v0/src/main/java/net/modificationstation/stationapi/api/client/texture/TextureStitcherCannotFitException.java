package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class TextureStitcherCannotFitException extends RuntimeException {
   private final Collection<Sprite.Info> sprites;

   public TextureStitcherCannotFitException(Sprite.Info sprite, Collection<Sprite.Info> sprites) {
      super(String.format("Unable to fit: %s - size: %dx%d - Maybe try a lower resolution texturepack?", sprite.getId(), sprite.getWidth(), sprite.getHeight()));
      this.sprites = sprites;
   }

   public Collection<Sprite.Info> getSprites() {
      return this.sprites;
   }
}