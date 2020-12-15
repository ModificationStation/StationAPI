package net.modificationstation.stationloader.api.server.entity;

import com.google.common.primitives.Doubles;
import net.minecraft.entity.EntityBase;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.entity.HasOwner;
import net.modificationstation.stationloader.api.common.packet.Message;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public interface StationSpawnData extends CustomSpawnData {

    @Override
    default AbstractPacket getSpawnData() {
        EntityBase entityBase = (EntityBase) this;
        int ownerId = 0;
        if (entityBase instanceof HasOwner) {
            EntityBase owner = ((HasOwner) entityBase).getOwner();
            owner = owner == null ? entityBase : owner;
            ownerId = owner.entityId;
        }
        Message message = new Message(Identifier.of(StationLoader.INSTANCE.getModID(), "spawn_entity"));
        message.put(new String[]{getHandlerIdentifier().toString()});
        int[] ints = new int[]{entityBase.entityId, MathHelper.floor(entityBase.x * 32), MathHelper.floor(entityBase.y * 32), MathHelper.floor(entityBase.z * 32), ownerId};
        message.put(ints);
        if (ownerId > 0) {
            double var10 = 3.9D;
            //noinspection UnstableApiUsage
            short[] shorts = new short[]{(short) (Doubles.constrainToRange(entityBase.velocityX, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityY, -var10, var10) * 8000), (short) (Doubles.constrainToRange(entityBase.velocityZ, -var10, var10) * 8000)};
            message.put(shorts);
        }
        writeToMessage(message);
        return message;
    }

    Identifier getHandlerIdentifier();

    default void writeToMessage(Message message) {
    }

    default void readFromMessage(Message message) {
    }
}
