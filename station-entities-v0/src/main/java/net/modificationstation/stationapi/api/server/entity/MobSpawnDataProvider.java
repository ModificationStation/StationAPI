package net.modificationstation.stationapi.api.server.entity;

import com.google.common.primitives.Bytes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public interface MobSpawnDataProvider extends StationSpawnDataProvider {

    @Override
    default Packet getSpawnData() {
        LivingEntity mob = (LivingEntity) this;
        MessagePacket message = new MessagePacket(of(NAMESPACE, "spawn_mob"));
        message.strings = new String[] { getHandlerIdentifier().toString() };
        message.ints = new int[] {
                mob.id,
                MathHelper.floor(mob.x * 32.0D),
                MathHelper.floor(mob.y * 32.0D),
                MathHelper.floor(mob.z * 32.0D)
        };
        byte[] rotations = new byte[] {
                (byte)((int)(mob.yaw * 256.0F / 360.0F)),
                (byte)((int)(mob.pitch * 256.0F / 360.0F))
        };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mob.method_1331().writeAllEntries(new DataOutputStream(outputStream));
        byte[] data = outputStream.toByteArray();
        message.bytes = Bytes.concat(rotations, data);
        writeToMessage(message);
        return message;
    }
}
