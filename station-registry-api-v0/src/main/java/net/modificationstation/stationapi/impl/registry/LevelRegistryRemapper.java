package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.LevelRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class LevelRegistryRemapper {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void loadProperties(LevelPropertiesEvent.LoadOnLevelInit event) {
//        LevelRegistry.remapping = true;
//        StatsAccessor.setBlocksInit(false);
//        StatsAccessor.setItemsInit(false);
        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
        CompoundTag registriesTag = event.tag.getCompoundTag(registriesRegistry.getRegistryId().toString());
        registriesRegistry.forEach((identifier, registry) -> {
            if (registry instanceof LevelRegistry)
                ((LevelRegistry<?>) registry).load(registriesTag.getCompoundTag(registry.getRegistryId().toString()));
        });
//        LevelRegistry.remapping = false;
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void saveProperties(LevelPropertiesEvent.Save event) {
        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
        CompoundTag registriesTag = new CompoundTag();
        registriesRegistry.forEach((identifier, registry) -> {
            if (registry instanceof LevelRegistry) {
                CompoundTag registryTag = new CompoundTag();
                ((LevelRegistry<?>) registry).save(registryTag);
                registriesTag.put(identifier.toString(), registryTag);
            }
        });
        event.tag.put(registriesRegistry.getRegistryId().toString(), registriesTag);
    }

// TODO: redo this completely to use a weaklist with all iteminstances to remap manually.

//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void resetRecipes(BeforeRecipeStatsEvent event) {
//        RecipeRegistryAccessor.invokeCor();
//        SmeltingRecipeRegistryAccessor.invokeCor();
//    }
}
