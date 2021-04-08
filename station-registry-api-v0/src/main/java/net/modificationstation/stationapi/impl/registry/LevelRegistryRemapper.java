package net.modificationstation.stationapi.impl.registry;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class LevelRegistryRemapper {
//
//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void loadProperties(LevelPropertiesEvent.LoadOnLevelInit event) {
//        LevelRegistry.remapping = true;
//        StatsAccessor.setBlocksInit(false);
//        StatsAccessor.setItemsInit(false);
//        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
//        CompoundTag registriesTag = event.tag.getCompoundTag(registriesRegistry.getRegistryId().toString());
//        registriesRegistry.forEach((identifier, registry) -> {
//            if (registry instanceof LevelRegistry)
//                ((LevelRegistry<?>) registry).load(registriesTag.getCompoundTag(registry.getRegistryId().toString()));
//        });
//        LevelRegistry.remapping = false;
//    }
//
//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void saveProperties(LevelPropertiesEvent.Save event) {
//        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
//        CompoundTag registriesTag = new CompoundTag();
//        registriesRegistry.forEach((identifier, registry) -> {
//            if (registry instanceof LevelRegistry) {
//                CompoundTag registryTag = new CompoundTag();
//                ((LevelRegistry<?>) registry).save(registryTag);
//                registriesTag.put(identifier.toString(), registryTag);
//            }
//        });
//        event.tag.put(registriesRegistry.getRegistryId().toString(), registriesTag);
//    }
//
//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void resetRecipes(BeforeRecipeStatsEvent event) {
//        RecipeRegistryAccessor.invokeCor();
//        SmeltingRecipeRegistryAccessor.invokeCor();
//    }
}
