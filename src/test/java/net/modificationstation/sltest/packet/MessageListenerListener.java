package net.modificationstation.sltest.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.item.ModdedItem;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;

public class MessageListenerListener {

    @EventListener
    public void registerMessageListeners(MessageListenerRegistryEvent event) {
        event.register(SLTest.NAMESPACE)
                .accept("give_me_diamonds", this::handleGiveMeDiamonds)
                .accept("send_an_object", this::handleSendCoords);
    }

    public void handleGiveMeDiamonds(PlayerEntity playerBase, MessagePacket message) {
        playerBase.method_490("Have a diamond!");
        playerBase.inventory.method_671(new ItemStack(Item.DIAMOND));
    }

    public void handleSendCoords(PlayerEntity playerBase, MessagePacket message) {
        SLTest.LOGGER.info(String.valueOf(((ModdedItem.TestNetworkData) message.objects[0]).getHmmSho()));
    }
}
