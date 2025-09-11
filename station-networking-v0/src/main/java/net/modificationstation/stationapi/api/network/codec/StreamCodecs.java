package net.modificationstation.stationapi.api.network.codec;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.network.PacketByteBuf;

/**
 * Provides {@link StreamCodec} for common types.
 */
public interface StreamCodecs {
    StreamCodec<PacketByteBuf, Boolean> BOOL = new StreamCodec<>() {
        public Boolean decode(PacketByteBuf buf) {
            return buf.readBoolean();
        }

        public void encode(PacketByteBuf buf, Boolean bool) {
            buf.writeBoolean(bool);
        }
    };

}
