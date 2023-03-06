package net.modificationstation.stationapi.impl.recipe;

import com.mojang.datafixers.util.Either;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class StationShapedRecipe implements Recipe, StationRecipe {

    private static final Random RANDOM = new Random();

    private final int width, height;
    private final Either<TagKey<ItemBase>, ItemInstance>[] grid;
    private final ItemInstance output;

    public StationShapedRecipe(int width, int height, Either<TagKey<ItemBase>, ItemInstance>[] grid, ItemInstance output) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.output = output;
    }

    @Override
    public boolean canCraft(Crafting grid) {
        for(int x = 0; x <= 3 - this.width; ++x)
            for (int y = 0; y <= 3 - this.height; ++y) {
                if (this.matches(grid, x, y, true)) return true;

                if (this.matches(grid, x, y, false)) return true;
            }
        return false;
    }

    private boolean matches(Crafting grid, int startX, int startY, boolean mirror) {
        for(int x = 0; x < 3; ++x)
            for (int y = 0; y < 3; ++y) {
                int dx = x - startX;
                int dy = y - startY;
                Either<TagKey<ItemBase>, ItemInstance> ingredient = null;
                if (dx >= 0 && dy >= 0 && dx < this.width && dy < this.height)
                    ingredient = this.grid[(mirror ? this.width - dx - 1 : dx) + dy * this.width];
                ItemInstance itemToTest = grid.getInventoryItemXY(x, y);
                if (itemToTest != null || ingredient != null) {
                    if (itemToTest == null || ingredient == null) return false;
                    Optional<TagKey<ItemBase>> tagOpt = ingredient.left();
                    if (tagOpt.isPresent()) {
                        if (!StationItemStack.class.cast(itemToTest).isIn(tagOpt.get()))
                            return false;
                    } else {
                        Optional<ItemInstance> itemOpt = ingredient.right();
                        if (itemOpt.isPresent()) {
                            ItemInstance item = itemOpt.get();
                            boolean ignoreDamage = item.getDamage() == -1;
                            if (ignoreDamage) item.setDamage(itemToTest.getDamage());
                            boolean equals = item.isDamageAndIDIdentical(itemToTest);
                            if (ignoreDamage) item.setDamage(-1);
                            if (!equals) return false;
                        }
                    }
                }
            }
        return true;
    }

    @Override
    public ItemInstance craft(Crafting arg) {
        return output.copy();
    }

    @Override
    public int getIngredientCount() {
        return width * height;
    }

    @Override
    public ItemInstance getOutput() {
        return output;
    }

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] itemInstances = new ItemInstance[9];
        for (int h = 0; h < height; h++)
            for (int w = 0; w < width; w++) {
                int localId = (h * width) + w;
                Either<TagKey<ItemBase>, ItemInstance> ingredient = grid[localId];
                if (ingredient == null) continue;
                int id = (h * 3) + w;
                itemInstances[id] = ingredient.map(tag -> new ItemInstance(ItemRegistry.INSTANCE.getEntryList(tag).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + tag.id() + "\" has no entry in the tag registry!")).getRandom(RANDOM).orElseThrow().value()), Function.identity());
            }
        return itemInstances;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }
}
