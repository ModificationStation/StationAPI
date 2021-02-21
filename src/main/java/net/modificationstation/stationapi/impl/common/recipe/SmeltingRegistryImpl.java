package net.modificationstation.stationapi.impl.common.recipe;

import lombok.Getter;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.impl.common.util.UnsafeProvider;
import net.modificationstation.stationapi.mixin.common.accessor.TileEntityFurnaceAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class SmeltingRegistryImpl {

    @Getter
    private static TileEntityFurnaceAccessor warcrimes;

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerTileEntities(TileEntityRegister event) {
        try {
            warcrimes = (TileEntityFurnaceAccessor) UnsafeProvider.getUnsafe().allocateInstance(TileEntityFurnace.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
