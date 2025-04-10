package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TextureStitcher<T extends TextureStitcher.Stitchable> {
    private static final Comparator<Holder<?>> COMPARATOR = Comparator.<Holder<?>, Integer>comparing((holder) -> -holder.height).thenComparing((holder) -> -holder.width).thenComparing((holder) -> holder.sprite.getId());
    private final Set<Holder<T>> holders = Sets.newHashSetWithExpectedSize(256);
    private final List<Slot<T>> slots = Lists.newArrayListWithCapacity(256);
    private int width;
    private int height;
    private final int maxWidth;
    private final int maxHeight;

    public TextureStitcher(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void add(T info) {
        Holder<T> holder = new Holder<>(info);
        holders.add(holder);
    }

    public void stitch() {
        List<Holder<T>> list = new ArrayList<>(holders);
        list.sort(COMPARATOR);
        Iterator<Holder<T>> var2 = list.iterator();

        Holder<T> holder;
        do {
            if (!var2.hasNext()) {
                width = MathHelper.smallestEncompassingPowerOfTwo(width);
                height = MathHelper.smallestEncompassingPowerOfTwo(height);
                return;
            }

            holder = var2.next();
        } while(fit(holder));

        throw new TextureStitcherCannotFitException(holder.sprite, list.stream().map((holderx) -> holderx.sprite).collect(ImmutableList.toImmutableList()));
    }

    public void getStitchedSprites(SpriteConsumer<T> consumer) {
        for (Slot<T> slot : this.slots) slot.addAllFilledSlots(consumer);
    }

    private boolean fit(Holder<T> holder) {
        for (Slot<T> slot : this.slots) if (slot.fit(holder)) return true;
        return this.growAndFit(holder);
    }

    private boolean growAndFit(Holder<T> holder) {
        int i = MathHelper.smallestEncompassingPowerOfTwo(width);
        int j = MathHelper.smallestEncompassingPowerOfTwo(height);
        int k = MathHelper.smallestEncompassingPowerOfTwo(width + holder.width);
        int l = MathHelper.smallestEncompassingPowerOfTwo(height + holder.height);
        boolean bl = k <= maxWidth;
        boolean bl2 = l <= maxHeight;
        if (!bl && !bl2) return false;
        else {
            boolean bl3 = bl && i != k;
            boolean bl4 = bl2 && j != l;
            boolean bl6;
            if (bl3 ^ bl4) bl6 = bl3;
            else bl6 = bl && i <= j;

            Slot<T> slot;
            if (bl6) {
                if (height == 0) height = holder.height;

                slot = new Slot<>(width, 0, holder.width, height);
                width += holder.width;
            } else {
                slot = new Slot<>(0, height, width, holder.height);
                height += holder.height;
            }

            slot.fit(holder);
            slots.add(slot);
            return true;
        }
    }

    record Holder<T extends Stitchable>(T sprite, int width, int height) {
        public Holder(T sprite) {
            this(sprite, sprite.getWidth(), sprite.getHeight());
        }
    }

    public interface Stitchable {
        int getWidth();

        int getHeight();

        Identifier getId();
    }

    public static class Slot<T extends Stitchable> {
        private final int x, y, width, height;
        @Nullable
        private List<Slot<T>> subSlots;
        @Nullable
        private Holder<T> texture;

        public Slot(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean fit(Holder<T> holder) {
            if (texture != null) return false;
            int i = holder.width;
            int j = holder.height;
            if (i > width || j > height) return false;
            if (i == width && j == height) {
                texture = holder;
                return true;
            }
            if (subSlots == null) {
                subSlots = new ArrayList<>(1);
                subSlots.add(new Slot<>(x, y, i, j));
                int k = width - i;
                int l = height - j;
                if (l > 0 && k > 0) {
                    int m = Math.max(height, k);
                    if (m >= Math.max(width, l)) {
                        subSlots.add(new Slot<>(x, y + j, i, l));
                        subSlots.add(new Slot<>(x + i, y, k, height));
                    } else {
                        subSlots.add(new Slot<>(x + i, y, k, j));
                        subSlots.add(new Slot<>(x, y + j, width, l));
                    }
                } else if (k == 0) subSlots.add(new Slot<>(x, y + j, i, l));
                else if (l == 0) subSlots.add(new Slot<>(x + i, y, k, j));
            }
            for (Slot<T> slot : subSlots) {
                if (!slot.fit(holder)) continue;
                return true;
            }
            return false;
        }

        public void addAllFilledSlots(SpriteConsumer<T> consumer) {
            if (texture != null) consumer.load(texture.sprite, getX(), getY());
            else if (subSlots != null) for (Slot<T> slot : subSlots) slot.addAllFilledSlots(consumer);
        }

        public String toString() {
            return "Slot{originX=" + x + ", originY=" + y + ", width=" + width + ", height=" + height + ", texture=" + texture + ", subSlots=" + subSlots + "}";
        }
    }

    public interface SpriteConsumer<T extends Stitchable> {
        void load(T sprite, int x, int y);
    }
}
