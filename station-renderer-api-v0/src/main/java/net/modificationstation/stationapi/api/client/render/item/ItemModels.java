package net.modificationstation.stationapi.api.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;
import net.modificationstation.stationapi.api.client.render.model.ModelIdentifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Environment(EnvType.CLIENT)
public class ItemModels {

    private static final Set<ItemModels> ITEM_MODELS = Collections.newSetFromMap(new WeakHashMap<>());

    public final Int2ObjectMap<ModelIdentifier> modelIds = new Int2ObjectOpenHashMap<>(256);
    private final Int2ObjectMap<BakedModel> models = new Int2ObjectOpenHashMap<>(256);
    private final BakedModelManager modelManager;

    public ItemModels(BakedModelManager modelManager) {
        this.modelManager = modelManager;
        ITEM_MODELS.add(this);
    }

    public BakedModel getModel(ItemStack stack) {
        BakedModel bakedModel = this.getModel(stack.getItem());
        return bakedModel == null ? this.modelManager.getMissingModel() : bakedModel;
    }

    @Nullable
    public BakedModel getModel(Item item) {
        return this.models.get(ItemModels.getModelId(item));
    }

    private static int getModelId(Item item) {
        return ItemRegistry.INSTANCE.getRawId(item);
    }

    public void putModel(Item item, ModelIdentifier modelId) {
        this.modelIds.put(ItemModels.getModelId(item), modelId);
    }

    public BakedModelManager getModelManager() {
        return this.modelManager;
    }

    public void reloadModels() {
        this.models.clear();
        for (Int2ObjectMap.Entry<ModelIdentifier> entry : this.modelIds.int2ObjectEntrySet())
            this.models.put(entry.getIntKey(), this.modelManager.getModel(entry.getValue()));
    }

    @ApiStatus.Internal
    public static void reloadModelsAll() {
        ITEM_MODELS.forEach(ItemModels::reloadModels);
    }
}