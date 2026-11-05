package net.modificationstation.stationapi.api.util.collection;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * A table of references indexed by non-negative {@code int} IDs that stays small when the IDs are sparse.
 *
 * <p>IDs are split into a page index ({@code id >>> PAGE_BITS}) and a slot within that page.
 * Pages are only allocated when something is written into them and grow in powers of two up to
 * {@link #PAGE_SIZE}, so memory is proportional to the occupied ID clusters, not to the highest ID.
 * Lookups are two array reads, and iteration is in ascending ID order.
 *
 * <p>This exists for registries whose raw IDs are fixed by vanilla and far apart, like stats
 * ({@code 1000}, {@code 0x500000 + n}, {@code 0x1000000 + blockId}, ...), where a flat list
 * would need tens of millions of slots to hold a few thousand entries.
 *
 * <p>Not thread-safe.
 *
 * @param <V> the value type
 */
@ApiStatus.Internal
public final class PagedTable<V> {
    private static final int PAGE_BITS = 12;
    private static final int PAGE_SIZE = 1 << PAGE_BITS;
    private static final int PAGE_MASK = PAGE_SIZE - 1;
    private static final int MIN_PAGE_SIZE = 16;
    private static final int MAX_PAGES = 1 << (Integer.SIZE - 1 - PAGE_BITS);
    private static final Object[][] NO_PAGES = new Object[0][];

    private V[][] pages = emptyPages();
    private int size;

    @SuppressWarnings("unchecked")
    private static <V> V[][] emptyPages() {
        return (V[][]) NO_PAGES;
    }

    /**
     * {@return the value at {@code id}, or {@code null} if there is none or {@code id} is negative}
     */
    public @Nullable V get(int id) {
        final V[] page = pageOf(id);
        return page == null ? null : page[id & PAGE_MASK];
    }

    /**
     * {@return the allocated page that reaches {@code id}, or {@code null} if there is none or {@code id} is negative}
     */
    private V @Nullable [] pageOf(int id) {
        if (id < 0) return null;
        final int pageIndex = id >>> PAGE_BITS;
        if (pageIndex >= pages.length) return null;
        final V[] page = pages[pageIndex];
        return page != null && (id & PAGE_MASK) < page.length ? page : null;
    }

    /**
     * Sets the value at {@code id}. Setting {@code null} is the same as {@link #remove(int)}.
     *
     * @return the previous value at {@code id}, or {@code null} if there was none
     * @throws IndexOutOfBoundsException if {@code id} is negative
     */
    @SuppressWarnings("unchecked")
    public @Nullable V set(int id, @Nullable V value) {
        if (id < 0) throw new IndexOutOfBoundsException("ID must not be negative: " + id);
        if (value == null) return remove(id);
        final int pageIndex = id >>> PAGE_BITS;
        final int slot = id & PAGE_MASK;

        if (pageIndex >= pages.length)
            pages = Arrays.copyOf(pages, Math.min(MAX_PAGES, Math.max(pageIndex + 1, pages.length + (pages.length >> 1))));

        V[] page = pages[pageIndex];
        if (page == null)
            page = pages[pageIndex] = (V[]) new Object[pageLengthFor(slot)];
        else if (slot >= page.length)
            page = pages[pageIndex] = Arrays.copyOf(page, Math.max(page.length << 1, pageLengthFor(slot)));

        final V previous = page[slot];
        page[slot] = value;
        if (previous == null) size++;
        return previous;
    }

    /**
     * Removes the value at {@code id}. Never allocates.
     *
     * @return the removed value, or {@code null} if there was none or {@code id} is negative
     */
    public @Nullable V remove(int id) {
        final V[] page = pageOf(id);
        if (page == null) return null;
        final int slot = id & PAGE_MASK;
        final V previous = page[slot];
        page[slot] = null;
        if (previous != null) size--;
        return previous;
    }

    /**
     * {@return the smallest power-of-two page length that can hold {@code slot}, at least {@link #MIN_PAGE_SIZE}}
     */
    private static int pageLengthFor(int slot) {
        return Math.max(MIN_PAGE_SIZE, MathHelper.smallestEncompassingPowerOfTwo(slot + 1));
    }

    /**
     * Removes all values and releases all pages.
     */
    public void clear() {
        pages = emptyPages();
        size = 0;
    }

    /**
     * {@return the number of non-null values}
     */
    public int size() {
        return size;
    }

    /**
     * Passes every non-null value and its ID to {@code action}, in ascending ID order.
     */
    public void forEach(EntryConsumer<? super V> action) {
        final V[][] pages = this.pages;
        for (int pageIndex = 0; pageIndex < pages.length; pageIndex++) {
            final V[] page = pages[pageIndex];
            if (page == null) continue;
            final int base = pageIndex << PAGE_BITS;
            for (int slot = 0; slot < page.length; slot++)
                if (page[slot] != null) action.accept(base | slot, page[slot]);
        }
    }

    /**
     * {@return an unmodifiable list of the non-null values, in ascending ID order}
     */
    @SuppressWarnings("unchecked")
    public List<V> values() {
        final V[] values = (V[]) new Object[size];
        final int[] i = {0};
        forEach((id, value) -> values[i[0]++] = value);
        return List.of(values);
    }

    @FunctionalInterface
    public interface EntryConsumer<V> {
        void accept(int id, V value);
    }
}
