package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitType;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.CustomReachProvider;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

import java.util.*;

public class ModdedItem extends TemplateItemBase implements CustomReachProvider, CustomTooltipProvider {
    public ModdedItem(Identifier id) {
        super(id);
    }

    @Override
    public ItemInstance use(ItemInstance item, Level level, PlayerBase player) {
        Message message = new Message(Identifier.of(SLTest.MODID, "send_an_object"));
        hmmSho = new Random().nextInt();
        System.out.println(hmmSho);
        message.objects = new Object[] { this };
        PacketHelper.sendTo(player, message);
        return super.use(item, level, player);
    }

    public int hmmSho;

    @Override
    public double getReach(ItemInstance itemInstance, PlayerBase player, HitType type, double currentReach) {
        switch (type) {
            case TILE:
                return 50;
            case ENTITY:
                return 10;
            default:
                return currentReach;
        }
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        return new String[] {
                "This has 50 block reach for tiles!",
                originalTooltip
        };
    }
}
