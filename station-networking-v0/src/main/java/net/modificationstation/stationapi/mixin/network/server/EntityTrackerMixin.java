package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.class_174;
import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.server.StationEntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_488.class)
public class EntityTrackerMixin implements StationEntityTracker {
    @Shadow private class_80 field_2005;

    @Override
    public <T extends Payload> void sendToListeners(Entity entity, PayloadType<PacketByteBuf, T> type, T payload) {
        class_174 entry = (class_174) this.field_2005.method_772(entity.id);
        if (entry != null) {
            entry.sendToListeners(type, payload);
        }
    }

    @Override
    public <T extends Payload> void sendToAround(Entity entity, PayloadType<PacketByteBuf, T> type, T payload) {
        class_174 entry = (class_174)this.field_2005.method_772(entity.id);
        if (entry != null) {
            entry.sendToAround(type, payload);
        }
    }
}
