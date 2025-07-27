package net.modificationstation.stationapi.api.server.entity;

import com.google.common.primitives.Doubles;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.HasOwner;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public interface EntitySpawnDataProvider extends StationSpawnDataProvider {

    @Override
    default Packet getSpawnData() {
        Entity entity = (Entity) this;
        int ownerId = 0;
        if (entity instanceof HasOwner hasOwner) {
            Entity owner = hasOwner.getOwner();
            owner = owner == null ? entity : owner;
            ownerId = owner.id;
        }
        MessagePacket message = new MessagePacket(Identifier.of(StationAPI.NAMESPACE, "spawn_entity"));
        message.strings = new String[] { getHandlerIdentifier().toString() };
        message.ints = new int[] { entity.id, MathHelper.floor(entity.x * 32), MathHelper.floor(entity.y * 32), MathHelper.floor(entity.z * 32), ownerId };
        if (ownerId > 0) {
            double var10 = 3.9D;
            message.shorts = new short[] { (short) (Doubles.constrainToRange(entity.velocityX, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entity.velocityY, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entity.velocityZ, -var10, var10) * 8000) };
        }
        if (syncTrackerAtSpawn()) {
            var stream = new ByteArrayOutputStream();
            entity.getDataTracker().writeAllEntries(new DataOutputStream(stream));
            message.bytes = stream.toByteArray();
        }
        writeToMessage(message);
        return message;
    }

    default boolean syncTrackerAtSpawn() {
        return false;
    }
}
