package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite.Info;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TextureStitcher {
    private static final Comparator<Holder> COMPARATOR = Comparator.<Holder, Integer>comparing((holder) -> -holder.height).thenComparing((holder) -> -holder.width).thenComparing((holder) -> holder.sprite.getId());
    private final Set<Holder> holders = Sets.newHashSetWithExpectedSize(256);
    private final List<Slot> slots = Lists.newArrayListWithCapacity(256);
    private int width;
    private int height;
    private final int maxWidth;
    private final int maxHeight;

    public TextureStitcher(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void add(Info info) {
        Holder holder = new Holder(info);
        this.holders.add(holder);
    }

    public void stitch() {
        List<Holder> list = new ArrayList<>(this.holders);
        list.sort(COMPARATOR);
        Iterator<Holder> var2 = list.iterator();

        Holder holder;
        do {
            if (!var2.hasNext()) {
                this.width = MathHelper.smallestEncompassingPowerOfTwo(this.width);
                this.height = MathHelper.smallestEncompassingPowerOfTwo(this.height);
                return;
            }

            holder = var2.next();
        } while(this.fit(holder));

        throw new TextureStitcherCannotFitException(holder.sprite, list.stream().map((holderx) -> holderx.sprite).collect(ImmutableList.toImmutableList()));
    }

    public void getStitchedSprites(SpriteConsumer spriteConsumer) {

        for (Slot slot : this.slots) {
            slot.addAllFilledSlots((slotx) -> {
                Holder holder = slotx.getTexture();
                Info info = holder.sprite;
                spriteConsumer.load(info, this.width, this.height, slotx.getX(), slotx.getY());
            });
        }

    }

    private boolean fit(Holder holder) {
        Iterator<Slot> var2 = this.slots.iterator();

        Slot slot;
        do {
            if (!var2.hasNext()) {
                return this.growAndFit(holder);
            }

            slot = var2.next();
        } while(!slot.fit(holder));

        return true;
    }

    private boolean growAndFit(Holder holder) {
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

            Slot slot;
            if (bl6) {
                if (this.height == 0) {
                    this.height = holder.height;
                }

                slot = new Slot(this.width, 0, holder.width, this.height);
                this.width += holder.width;
            } else {
                slot = new Slot(0, this.height, this.width, holder.height);
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
        private List<Slot> subSlots;
        private Holder texture;

        public Slot(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Holder getTexture() {
            return this.texture;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public boolean fit(Holder holder) {
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
                            this.subSlots.add(new Slot(this.x, this.y, i, j));
                            int k = this.width - i;
                            int l = this.height - j;
                            if (l > 0 && k > 0) {
                                int m = Math.max(this.height, k);
                                int n = Math.max(this.width, l);
                                if (m >= n) {
                                    this.subSlots.add(new Slot(this.x, this.y + j, i, l));
                                    this.subSlots.add(new Slot(this.x + i, this.y, k, this.height));
                                } else {
                                    this.subSlots.add(new Slot(this.x + i, this.y, k, j));
                                    this.subSlots.add(new Slot(this.x, this.y + j, this.width, l));
                                }
                            } else if (k == 0) {
                                this.subSlots.add(new Slot(this.x, this.y + j, i, l));
                            } else if (l == 0) {
                                this.subSlots.add(new Slot(this.x + i, this.y, k, j));
                            }
                        }

                        Iterator<Slot> var8 = this.subSlots.iterator();

                        Slot slot;
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

        public void addAllFilledSlots(Consumer<Slot> consumer) {
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
        public final Info sprite;
        public final int width;
        public final int height;

        public Holder(Info sprite) {
            this.sprite = sprite;
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }

        public String toString() {
            return "Holder{width=" + this.width + ", height=" + this.height + '}';
        }
    }

    @Environment(EnvType.CLIENT)
    public interface SpriteConsumer {
        void load(Info info, int width, int height, int x, int y);
    }
}
