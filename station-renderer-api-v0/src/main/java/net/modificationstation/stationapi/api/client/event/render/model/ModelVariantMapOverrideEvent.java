package net.modificationstation.stationapi.api.client.event.render.model;

import com.mojang.datafixers.util.Pair;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.json.ModelVariantMap;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.List;

/**
 * Fired after the {@link ModelVariantMap} is deserialized, allowing to add custom overrides
 */
@SuperBuilder
public final class ModelVariantMapOverrideEvent extends Event {
    /**
     * The model loader that is loading this blockstate
     */
    public final ModelLoader modelLoader;
    /**
     * The {@link Identifier} of the {@link Block} for which the {@link BlockState} is being loaded
     */
    public final Identifier id;
    /**
     * The map of the variants of the {@link BlockState}
     */
    public final List<Pair<String, ModelVariantMap>> variantMaps;

    /**
     * Adds a custom {@link ModelVariantMap} to the list of overrides for the given {@link BlockState}
     *
     * @param namespace  The {@link Namespace} of your mod
     * @param variantMap The {@link ModelVariantMap} to add
     */
    public void addModelVariantMap(Namespace namespace, ModelVariantMap variantMap) {
        this.variantMaps.add(new Pair<>(namespace.getName() + " Model Variant Event Override", variantMap));
    }
}
