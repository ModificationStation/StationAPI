package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class TextureStitcher {
   private static final Comparator<TextureStitcher.Holder> COMPARATOR = Comparator.<Holder, Integer>comparing((holder) -> -holder.height).thenComparing((holder) -> -holder.width).thenComparing((holder) -> holder.sprite.index);
   private final int mipLevel;
   private final Set<TextureStitcher.Holder> holders = Sets.newHashSetWithExpectedSize(256);
   private final List<TextureStitcher.Slot> slots = Lists.newArrayListWithCapacity(256);
   private int width;
   private int height;
   private final int maxWidth;
   private final int maxHeight;

   public TextureStitcher(int maxWidth, int maxHeight, int mipLevel) {
      this.mipLevel = mipLevel;
      this.maxWidth = maxWidth;
      this.maxHeight = maxHeight;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void add(ExpandableAtlas.FileSprite info) {
      TextureStitcher.Holder holder = new TextureStitcher.Holder(info, this.mipLevel);
      this.holders.add(holder);
   }

   public void stitch() {
      List<TextureStitcher.Holder> list = Lists.newArrayList(this.holders);
      list.sort(COMPARATOR);
      Iterator<Holder> var2 = list.iterator();

      TextureStitcher.Holder holder;
      do {
         if (!var2.hasNext()) {
            this.width = MathHelper.smallestEncompassingPowerOfTwo(this.width);
            this.height = MathHelper.smallestEncompassingPowerOfTwo(this.height);
            return;
         }

         holder = var2.next();
      } while(this.fit(holder));

      throw new RuntimeException("Textures cannot fit!");
   }

   public void getStitchedSprites(TextureStitcher.SpriteConsumer spriteConsumer) {

      for (Slot slot : this.slots) {
         slot.addAllFilledSlots((slotx) -> {
            Holder holder = slotx.getTexture();
            ExpandableAtlas.FileSprite info = holder.sprite;
            spriteConsumer.load(info, this.width, this.height, slotx.getX(), slotx.getY());
         });
      }

   }

   private static int applyMipLevel(int size, int mipLevel) {
      return (size >> mipLevel) + ((size & (1 << mipLevel) - 1) == 0 ? 0 : 1) << mipLevel;
   }

   private boolean fit(TextureStitcher.Holder holder) {
      Iterator<Slot> var2 = this.slots.iterator();

      TextureStitcher.Slot slot;
      do {
         if (!var2.hasNext()) {
            return this.growAndFit(holder);
         }

         slot = var2.next();
      } while(!slot.fit(holder));

      return true;
   }

   private boolean growAndFit(TextureStitcher.Holder holder) {
      int i = MathHelper.smallestEncompassingPowerOfTwo(this.width);
      int j = MathHelper.smallestEncompassingPowerOfTwo(this.height);
      int k = MathHelper.smallestEncompassingPowerOfTwo(this.width + holder.width);
      int l = MathHelper.smallestEncompassingPowerOfTwo(this.height + holder.height);
      boolean bl = k <= this.maxWidth;
      boolean bl2 = l <= this.maxHeight;
      if (!bl && !bl2) {
         return false;
      } else {
         boolean bl3 = bl && i != k;
         boolean bl4 = bl2 && j != l;
         boolean bl6;
         if (bl3 ^ bl4) {
            bl6 = bl3;
         } else {
            bl6 = bl && i <= j;
         }

         TextureStitcher.Slot slot;
         if (bl6) {
            if (this.height == 0) {
               this.height = holder.height;
            }

            slot = new TextureStitcher.Slot(this.width, 0, holder.width, this.height);
            this.width += holder.width;
         } else {
            slot = new TextureStitcher.Slot(0, this.height, this.width, holder.height);
            this.height += holder.height;
         }

         slot.fit(holder);
         this.slots.add(slot);
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   public static class Slot {
      private final int x;
      private final int y;
      private final int width;
      private final int height;
      private List<TextureStitcher.Slot> subSlots;
      private TextureStitcher.Holder texture;

      public Slot(int x, int y, int width, int height) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }

      public TextureStitcher.Holder getTexture() {
         return this.texture;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public boolean fit(TextureStitcher.Holder holder) {
         if (this.texture != null) {
            return false;
         } else {
            int i = holder.width;
            int j = holder.height;
            if (i <= this.width && j <= this.height) {
               if (i == this.width && j == this.height) {
                  this.texture = holder;
               } else {
                  if (this.subSlots == null) {
                     this.subSlots = Lists.newArrayListWithCapacity(1);
                     this.subSlots.add(new TextureStitcher.Slot(this.x, this.y, i, j));
                     int k = this.width - i;
                     int l = this.height - j;
                     if (l > 0 && k > 0) {
                        int m = Math.max(this.height, k);
                        int n = Math.max(this.width, l);
                        if (m >= n) {
                           this.subSlots.add(new TextureStitcher.Slot(this.x, this.y + j, i, l));
                           this.subSlots.add(new TextureStitcher.Slot(this.x + i, this.y, k, this.height));
                        } else {
                           this.subSlots.add(new TextureStitcher.Slot(this.x + i, this.y, k, j));
                           this.subSlots.add(new TextureStitcher.Slot(this.x, this.y + j, this.width, l));
                        }
                     } else if (k == 0) {
                        this.subSlots.add(new TextureStitcher.Slot(this.x, this.y + j, i, l));
                     } else if (l == 0) {
                        this.subSlots.add(new TextureStitcher.Slot(this.x + i, this.y, k, j));
                     }
                  }

                  Iterator<Slot> var8 = this.subSlots.iterator();

                  TextureStitcher.Slot slot;
                  do {
                     if (!var8.hasNext()) {
                        return false;
                     }

                     slot = var8.next();
                  } while(!slot.fit(holder));

               }
               return true;
            } else {
               return false;
            }
         }
      }

      public void addAllFilledSlots(Consumer<TextureStitcher.Slot> consumer) {
         if (this.texture != null) {
            consumer.accept(this);
         } else if (this.subSlots != null) {

            for (Slot slot : this.subSlots) {
               slot.addAllFilledSlots(consumer);
            }
         }

      }

      public String toString() {
         return "Slot{originX=" + this.x + ", originY=" + this.y + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.texture + ", subSlots=" + this.subSlots + '}';
      }
   }

   @Environment(EnvType.CLIENT)
   static class Holder {
      public final ExpandableAtlas.FileSprite sprite;
      public final int width;
      public final int height;

      public Holder(ExpandableAtlas.FileSprite sprite, int mipLevel) {
         this.sprite = sprite;
         this.width = TextureStitcher.applyMipLevel(sprite.getWidth(), mipLevel);
         this.height = TextureStitcher.applyMipLevel(sprite.getHeight(), mipLevel);
      }

      public String toString() {
         return "Holder{width=" + this.width + ", height=" + this.height + '}';
      }
   }

   @Environment(EnvType.CLIENT)
   public interface SpriteConsumer {
      void load(ExpandableAtlas.FileSprite info, int width, int height, int x, int y);
   }
}
