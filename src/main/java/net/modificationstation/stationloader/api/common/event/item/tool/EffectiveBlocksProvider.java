package net.modificationstation.stationloader.api.common.event.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.List;

public interface EffectiveBlocksProvider {

    SimpleEvent<EffectiveBlocksProvider> EVENT = new SimpleEvent<>(EffectiveBlocksProvider.class, listeners ->
            (toolBase, toolMaterial, effectiveBlocksBase) -> {
                for (EffectiveBlocksProvider listener : listeners)
                    listener.getEffectiveBlocks(toolBase, toolMaterial, effectiveBlocksBase);
            });

    void getEffectiveBlocks(ToolBase toolBase, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocksBase);
}
