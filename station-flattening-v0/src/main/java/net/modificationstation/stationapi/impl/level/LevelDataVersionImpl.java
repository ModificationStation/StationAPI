package net.modificationstation.stationapi.impl.level;

import com.google.common.collect.Iterators;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.mixin.nbt.CompoundTagAccessor;

import java.util.Map;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class LevelDataVersionImpl {

    @EventListener
    private static void addDataVersions(LevelPropertiesEvent.Save event) {
        Map.Entry<String, ? extends AbstractTag> entry = Iterators.getOnlyElement(((CompoundTagAccessor) NbtHelper.addDataVersions(new CompoundTag())).stationapi$getData().entrySet().iterator());
        event.tag.put(entry.getKey(), entry.getValue());
    }
}
