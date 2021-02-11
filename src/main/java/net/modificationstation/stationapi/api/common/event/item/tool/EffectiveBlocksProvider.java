package net.modificationstation.stationapi.api.common.event.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.List;
import java.util.function.Consumer;

public interface EffectiveBlocksProvider {

    GameEventOld<EffectiveBlocksProvider> EVENT = new GameEventOld<>(EffectiveBlocksProvider.class,
            listeners ->
                    (tool, toolMaterial, effectiveBlocks) -> {
                        for (EffectiveBlocksProvider listener : listeners)
                            listener.getEffectiveBlocks(tool, toolMaterial, effectiveBlocks);
                    },
            (Consumer<GameEventOld<EffectiveBlocksProvider>>) effectiveBlocksProvider ->
                    effectiveBlocksProvider.register((tool, toolMaterial, effectiveBlocks) -> GameEventOld.EVENT_BUS.post(new Data(tool, toolMaterial, effectiveBlocks)))
    );

    void getEffectiveBlocks(ToolBase tool, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocks);

    final class Data extends GameEventOld.Data<EffectiveBlocksProvider> {

        public final ToolBase tool;
        public final ToolMaterial toolMaterial;
        public final List<BlockBase> effectiveBlocks;

        private Data(ToolBase tool, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocks) {
            super(EVENT);
            this.tool = tool;
            this.toolMaterial = toolMaterial;
            this.effectiveBlocks = effectiveBlocks;
        }
    }
}
