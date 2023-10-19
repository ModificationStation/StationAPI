package net.modificationstation.stationapi.api.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import lombok.val;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.util.*;

public class StationShapedRecipe implements Recipe {
    private static final Random RANDOM = new Random();

    /**
     * Compiles a pattern and series of symbols into an array of ingredients (the matrix) suitable for matching
     * against a crafting grid.
     */
    private static Ingredient[] createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        val matrix = new Ingredient[width * height];
        Arrays.fill(matrix, Ingredient.EMPTY);
        HashSet<String> set = Sets.newHashSet(symbols.keySet());
        set.remove(" ");
        for (int i = 0; i < pattern.length; ++i)
            for (int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = symbols.get(string);
                if (ingredient == null)
                    throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                set.remove(string);
                matrix[j + width * i] = ingredient;
            }
        if (!set.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        return matrix;
    }

    /**
     * Removes empty space from around the recipe pattern.
     *
     * <p>Turns patterns such as:</p>
     * <pre>
     * {@code
     * "   o"
     * "   a"
     * "    "
     * }
     * </pre>
     * Into:
     * <pre>
     * {@code
     * "o"
     * "a"
     * }
     * </pre>
     *
     * @return a new recipe pattern with all leading and trailing empty rows/columns removed
     */
    private static String[] removePadding(String ... pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        for (int m = 0; m < pattern.length; ++m) {
            String string = pattern[m];
            i = Math.min(i, findFirstSymbol(string));
            int n = findLastSymbol(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) ++k;
                ++l;
                continue;
            }
            l = 0;
        }
        if (pattern.length == l) return new String[0];
        String[] strings = new String[pattern.length - l - k];
        for (int o = 0; o < strings.length; ++o) strings[o] = pattern[o + k].substring(i, j + 1);
        return strings;
    }

    private static int findFirstSymbol(String line) {
        int i;
        //noinspection StatementWithEmptyBody
        for (i = 0; i < line.length() && line.charAt(i) == ' '; ++i);
        return i;
    }

    private static int findLastSymbol(String pattern) {
        int i;
        //noinspection StatementWithEmptyBody
        for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i);
        return i;
    }

    private static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 3) throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        if (strings.length == 0) throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        for (int i = 0; i < strings.length; ++i) {
            String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
            if (string.length() > 3) throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
            if (i > 0 && strings[0].length() != string.length())
                throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            strings[i] = string;
        }
        return strings;
    }

    /**
     * Reads the pattern symbols.
     *
     * @return a mapping from a symbol to the ingredient it represents
     */
    private static Map<String, Ingredient> readSymbols(JsonObject json) {
        HashMap<String, Ingredient> map = Maps.newHashMap();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemInstance outputFromJson(JsonObject json) {
        ItemBase item = getItem(json);
        if (json.has("data")) throw new JsonParseException("Disallowed data tag found");
        int i = JsonHelper.getInt(json, "count", 1);
        if (i < 1) throw new JsonSyntaxException("Invalid output count: " + i);
        return new ItemInstance(item, i);
    }

    public static ItemBase getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        ItemBase item = ItemRegistry.INSTANCE.getOrEmpty(Identifier.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == States.AIR.get().getBlock().asItem())
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        return item;
    }

    private final int width, height;
    private final Ingredient[] matrix;
    private final ItemInstance output;
    private final boolean dataDriven;

    public StationShapedRecipe(int width, int height, Ingredient[] matrix, ItemInstance output) {
        this(width, height, matrix, output, false);
    }

    private StationShapedRecipe(int width, int height, Ingredient[] matrix, ItemInstance output, boolean dataDriven) {
        this.width = width;
        this.height = height;
        this.matrix = matrix;
        this.output = output;
        this.dataDriven = dataDriven;
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
                Ingredient ingredient = Ingredient.EMPTY;
                if (dx >= 0 && dy >= 0 && dx < this.width && dy < this.height)
                    ingredient = this.matrix[(mirror ? this.width - dx - 1 : dx) + dy * this.width];
                ItemInstance itemToTest = grid.getInventoryItemXY(x, y);
                if (!ingredient.test(itemToTest)) return false;
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
                Ingredient ingredient = matrix[localId];
                if (ingredient == null) continue;
                int id = (h * 3) + w;
                itemInstances[id] = ingredient.getRandom(RANDOM);
            }
        return itemInstances;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }

    @Override
    public boolean isDataDriven() {
        return dataDriven;
    }

    public static class Serializer implements RecipeSerializer<StationShapedRecipe> {
        @Override
        public StationShapedRecipe read(Identifier identifier, JsonObject jsonObject) {
            val key = readSymbols(JsonHelper.getObject(jsonObject, "key"));
            val pattern = removePadding(getPattern(JsonHelper.getArray(jsonObject, "pattern")));
            val width = pattern[0].length();
            val height = pattern.length;
            val matrix = createPatternMatrix(pattern, key, width, height);
            val output = outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new StationShapedRecipe(width, height, matrix, output, true);
        }
    }
}
