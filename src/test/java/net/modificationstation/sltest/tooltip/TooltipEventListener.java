package net.modificationstation.sltest.tooltip;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.context.BlockTagContext;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.item.context.ItemTagContext;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Formatting;

public class TooltipEventListener {
    @EventListener
    public void addTagsToTooltip(TooltipBuildEvent event) {
        ItemStack stack = event.itemStack;
        
        event.tooltip.add(Formatting.DARK_PURPLE + "Item Tags:");
        for (TagKey<Item> tag : stack.getItem().getRegistryEntry().getTags(ItemTagContext.of(stack))) {
            event.tooltip.add(" " + Formatting.LIGHT_PURPLE + tag.id().toString());
        }
        
        
        if (stack.getItem() instanceof BlockItem blockItem) {
            event.tooltip.add("");
            event.tooltip.add(Formatting.DARK_PURPLE + "Block Tags:");
            Block placedBlock = blockItem.getBlock();
            for (TagKey<Block> tag : placedBlock.getRegistryEntry().getTags(BlockTagContext.of(blockItem.getPlacementMetadata(stack.getDamage())))) {
                event.tooltip.add(" " + Formatting.LIGHT_PURPLE + tag.id().toString());                
            }
        }
    }
}
