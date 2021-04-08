package net.modificationstation.sltest.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.item.ModdedItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class MessageListenerListener {

    @EventListener
    public void registerMessageListeners(RegistryEvent.MessageListeners event) {
        MessageListenerRegistry registry = event.registry;
        registry.registerValue(Identifier.of(SLTest.MODID, "give_me_diamonds"), this::handleGiveMeDiamonds);
        registry.registerValue(Identifier.of(SLTest.MODID, "send_an_object"), this::handleSendCoords);
    }

    public void handleGiveMeDiamonds(PlayerBase playerBase, Message message) {
        playerBase.sendMessage("Have a diamond!");
        playerBase.inventory.addStack(new ItemInstance(ItemBase.diamond));
    }

    public void handleSendCoords(PlayerBase playerBase, Message message) {
        System.out.println(((ModdedItem) message.objects()[0]).hmmSho);
    }
}
