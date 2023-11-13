package net.modificationstation.stationapi.impl.level;

import com.google.common.collect.Iterators;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.WorldPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.mixin.nbt.NbtCompoundAccessor;

import java.util.Map;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class LevelDataVersionImpl {

    @EventListener
    private static void addDataVersions(WorldPropertiesEvent.Save event) {
        Map.Entry<String, ? extends NbtElement> entry = Iterators.getOnlyElement(((NbtCompoundAccessor) NbtHelper.addDataVersions(new NbtCompound())).stationapi$getEntries().entrySet().iterator());
        event.nbt.put(entry.getKey(), entry.getValue());
    }
}
