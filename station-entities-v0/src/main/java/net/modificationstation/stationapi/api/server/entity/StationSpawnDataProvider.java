package net.modificationstation.stationapi.api.server.entity;

import com.google.common.primitives.Doubles;
import net.minecraft.entity.EntityBase;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.HasOwner;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface StationSpawnDataProvider extends CustomSpawnDataProvider {

    @Override
    default AbstractPacket getSpawnData() {
        EntityBase entityBase = (EntityBase) this;
        int ownerId = 0;
        if (entityBase instanceof HasOwner) {
            EntityBase owner = ((HasOwner) entityBase).getOwner();
            owner = owner == null ? entityBase : owner;
            ownerId = owner.entityId;
        }
        Message message = new Message(Identifier.of(StationAPI.MODID, "spawn_entity"));
        message.strings = new String[] { getHandlerIdentifier().toString() };
        message.ints = new int[] { entityBase.entityId, MathHelper.floor(entityBase.x * 32), MathHelper.floor(entityBase.y * 32), MathHelper.floor(entityBase.z * 32), ownerId };
        if (ownerId > 0) {
            double var10 = 3.9D;
            message.shorts = new short[] { (short) (Doubles.constrainToRange(entityBase.velocityX, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityY, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityZ, -var10, var10) * 8000) };
        }
        writeToMessage(message);
        return message;
    }

    Identifier getHandlerIdentifier();

    default void writeToMessage(Message message) { }

    default void readFromMessage(Message message) { }
}
