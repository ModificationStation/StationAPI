package net.modificationstation.sltest.network;

import net.minecraft.item.ItemStack;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.codec.StreamCodec;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;

public record ExamplePayload(ItemStack stack) implements Payload<ExamplePayloadHandler> {
    public static final StreamCodec<PacketByteBuf, ExamplePayload> STREAM_CODEC = StreamCodec.ofMember(ExamplePayload::write, ExamplePayload::new);
    public static final PayloadType<PacketByteBuf, ExamplePayload> TYPE = new PayloadType<>(SLTest.NAMESPACE.id("example_payload"), STREAM_CODEC);

    public ExamplePayload(PacketByteBuf buf) {
        this(new ItemStack(buf.readShort(), buf.readByte(), buf.readShort()));
    }

    public void write(PacketByteBuf buf) {
        buf.writeShort(stack.itemId);
        buf.writeByte(stack.count);
        buf.writeShort(stack.getDamage2());
    }

    @Override
    public PayloadType<PacketByteBuf, ExamplePayload> type() {
        return TYPE;
    }

    @Override
    public void handle(ExamplePayloadHandler handler) {
        handler.handleExamplePayload(this);
    }
}
