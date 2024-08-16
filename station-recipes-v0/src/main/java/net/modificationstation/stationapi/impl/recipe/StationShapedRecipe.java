package net.modificationstation.stationapi.impl.recipe;

import com.mojang.datafixers.util.Either;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.*;

public class StationShapedRecipe implements CraftingRecipe {
    public final int width, height;
    private final Either<TagKey<Item>, ItemStack>[] grid;
    public final ItemStack output;

    public StationShapedRecipe(int width, int height, Either<TagKey<Item>, ItemStack>[] grid, ItemStack output) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.output = output;
    }

    @Override
    public boolean matches(CraftingInventory grid) {
        for(int x = 0; x <= 3 - this.width; ++x)
            for (int y = 0; y <= 3 - this.height; ++y) {
                if (this.matches(grid, x, y, true)) return true;

                if (this.matches(grid, x, y, false)) return true;
            }
        return false;
    }

    private boolean matches(CraftingInventory grid, int startX, int startY, boolean mirror) {
        for(int x = 0; x < 3; ++x)
            for (int y = 0; y < 3; ++y) {
                int dx = x - startX;
                int dy = y - startY;
                Either<TagKey<Item>, ItemStack> ingredient = null;
                if (dx >= 0 && dy >= 0 && dx < this.width && dy < this.height)
                    ingredient = this.grid[(mirror ? this.width - dx - 1 : dx) + dy * this.width];
                ItemStack itemToTest = grid.getStack(x, y);
                if (itemToTest != null || ingredient != null) {
                    if (itemToTest == null || ingredient == null) return false;
                    Optional<TagKey<Item>> tagOpt = ingredient.left();
                    if (tagOpt.isPresent()) {
                        if (!itemToTest.isIn(tagOpt.get()))
                            return false;
                    } else {
                        Optional<ItemStack> itemOpt = ingredient.right();
                        if (itemOpt.isPresent()) {
                            ItemStack item = itemOpt.get();
                            boolean ignoreDamage = item.getDamage() == -1;
                            if (ignoreDamage) item.setDamage(itemToTest.getDamage());
                            boolean equals = item.isItemEqual(itemToTest);
                            if (ignoreDamage) item.setDamage(-1);
                            if (!equals) return false;
                        }
                    }
                }
            }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory arg) {
        return output.copy();
    }

    @Override
    public int getSize() {
        return width * height;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    public Either<TagKey<Item>, ItemStack>[] getGrid() {
        //noinspection unchecked
        return (Either<TagKey<Item>, ItemStack>[]) Arrays.stream(grid).map(entry -> entry == null ? null : entry.mapRight(ItemStack::copy)).toArray(Either[]::new);
    }
}
