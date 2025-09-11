package net.modificationstation.stationapi.mixin.network.server;

import net.minecraft.class_174;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.server.StationEntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(class_174.class)
public class EntityTrackerEntryMixin implements StationEntityTrackerEntry {
    @Shadow public Set<ServerPlayerEntity> field_610;

    @Shadow public Entity field_597;

    @Override
    public <T extends Payload> void sendToListeners(PayloadType<PacketByteBuf, T> type, T payload) {
        for (ServerPlayerEntity player : this.field_610) {
            player.field_255.sendPayload(type, payload);
        }
    }

    @Override
    public <T extends Payload> void sendToAround(PayloadType<PacketByteBuf, T> type, T payload) {
        this.sendToListeners(type, payload);
        if (this.field_597 instanceof ServerPlayerEntity player) {
            player.field_255.sendPayload(type, payload);
        }
    }
}
