package net.modificationstation.sltest.item;

import lombok.Value;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.CustomReachProvider;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class ModdedItem extends TemplateItem implements CustomReachProvider, CustomTooltipProvider {
    public ModdedItem(Identifier id) {
        super(id);
    }

    @SuppressWarnings("ClassCanBeRecord") // can't test with records cuz gson is outdated
    public static @Value class TestNetworkData { int hmmSho; }

    @Override
    public ItemStack use(ItemStack item, World level, PlayerEntity player) {
        if (!level.isRemote) {
            MessagePacket message = new MessagePacket(Identifier.of(SLTest.NAMESPACE, "send_an_object"));
            hmmSho = new Random().nextInt();
            SLTest.LOGGER.info(String.valueOf(hmmSho));
            message.objects = new Object[] { new TestNetworkData(hmmSho) };
            PacketHelper.sendTo(player, message);
        }
        return super.use(item, level, player);
    }

    public int hmmSho;

    @Override
    public double getReach(ItemStack stack, PlayerEntity player, HitResultType type, double currentReach) {
        return switch (type) {
            case BLOCK -> 50;
            case ENTITY -> 10;
        };
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[] {
                "This has 50 block reach for tiles!",
                originalTooltip
        };
    }
}
