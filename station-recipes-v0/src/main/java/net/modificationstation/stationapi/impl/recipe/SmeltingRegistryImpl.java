package net.modificationstation.stationapi.impl.recipe;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lombok.Getter;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import net.modificationstation.stationapi.mixin.recipe.TileEntityFurnaceAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class SmeltingRegistryImpl {

    @Getter
    private static TileEntityFurnaceAccessor warcrimes;

    public static final Reference2IntMap<TagKey<ItemBase>> TAG_FUEL_TIME = new Reference2IntOpenHashMap<>();

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerTileEntities(TileEntityRegisterEvent event) {
        try {
            warcrimes = (TileEntityFurnaceAccessor) UnsafeProvider.theUnsafe.allocateInstance(TileEntityFurnace.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
