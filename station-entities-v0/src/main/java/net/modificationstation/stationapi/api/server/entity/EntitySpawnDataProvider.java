package net.modificationstation.stationapi.api.server.entity;

import com.google.common.primitives.Doubles;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.HasOwner;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.util.Identifier;

public interface EntitySpawnDataProvider extends StationSpawnDataProvider {

    @Override
    default Packet getSpawnData() {
        Entity entityBase = (Entity) this;
        int ownerId = 0;
        if (entityBase instanceof HasOwner hasOwner) {
            Entity owner = hasOwner.getOwner();
            owner = owner == null ? entityBase : owner;
            ownerId = owner.id;
        }
        Message message = new Message(Identifier.of(StationAPI.NAMESPACE, "spawn_entity"));
        message.strings = new String[] { getHandlerIdentifier().toString() };
        message.ints = new int[] { entityBase.id, MathHelper.floor(entityBase.x * 32), MathHelper.floor(entityBase.y * 32), MathHelper.floor(entityBase.z * 32), ownerId };
        if (ownerId > 0) {
            double var10 = 3.9D;
            message.shorts = new short[] { (short) (Doubles.constrainToRange(entityBase.velocityX, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityY, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityZ, -var10, var10) * 8000) };
        }
        writeToMessage(message);
        return message;
    }
}
