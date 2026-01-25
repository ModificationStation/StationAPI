package net.modificationstation.sltest.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.item.ModdedItem;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;

import static net.modificationstation.sltest.SLTest.NAMESPACE;

public class MessageListenerListener {

    @EventListener
    public void registerMessageListeners(MessageListenerRegistryEvent event) {
        event.register(SLTest.NAMESPACE)
                .accept("give_me_diamonds", this::handleGiveMeDiamonds)
                .accept("send_an_object", this::handleSendCoords)
                .accept("skylands_switch", this::handleSkylandsSwitch);
    }

    public void handleGiveMeDiamonds(PlayerEntity playerBase, MessagePacket message) {
        playerBase.sendMessage("Have a diamond!");
        playerBase.inventory.addStack(new ItemStack(Item.DIAMOND));
    }

    public void handleSendCoords(PlayerEntity playerBase, MessagePacket message) {
        SLTest.LOGGER.info(String.valueOf(((ModdedItem.TestNetworkData) message.objects[0]).getHmmSho()));
    }

    public void handleSkylandsSwitch(PlayerEntity playerBase, MessagePacket message) {
        DimensionHelper.switchDimension(
                playerBase, DimensionTypeRegistry.INSTANCE.get(NAMESPACE.id("test_dimension")), 1, new PortalForcer()
        );
    }
}
