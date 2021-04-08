package net.modificationstation.stationapi.impl.recipe;

import lombok.Getter;
import net.minecraft.tileentity.TileEntityFurnace;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.impl.util.UnsafeProvider;
import net.modificationstation.stationapi.mixin.recipe.TileEntityFurnaceAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class SmeltingRegistryImpl {

    @Getter
    private static TileEntityFurnaceAccessor warcrimes;

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerTileEntities(TileEntityRegisterEvent event) {
        try {
            warcrimes = (TileEntityFurnaceAccessor) UnsafeProvider.theUnsafe.allocateInstance(TileEntityFurnace.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
