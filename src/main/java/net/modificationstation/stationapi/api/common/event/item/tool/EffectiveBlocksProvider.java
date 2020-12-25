package net.modificationstation.stationapi.api.common.event.item.tool;

import lombok.Getter;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.List;
import java.util.function.Consumer;

public interface EffectiveBlocksProvider {

    GameEvent<EffectiveBlocksProvider> EVENT = new GameEvent<>(EffectiveBlocksProvider.class,
            listeners ->
                    (tool, toolMaterial, effectiveBlocks) -> {
                        for (EffectiveBlocksProvider listener : listeners)
                            listener.getEffectiveBlocks(tool, toolMaterial, effectiveBlocks);
                    },
            (Consumer<GameEvent<EffectiveBlocksProvider>>) effectiveBlocksProvider ->
                    effectiveBlocksProvider.register((tool, toolMaterial, effectiveBlocks) -> GameEvent.EVENT_BUS.post(new Data(tool, toolMaterial, effectiveBlocks)))
    );

    void getEffectiveBlocks(ToolBase tool, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocks);

    final class Data extends GameEvent.Data<EffectiveBlocksProvider> {

        @Getter
        private final ToolBase tool;
        @Getter
        private final ToolMaterial toolMaterial;
        @Getter
        private final List<BlockBase> effectiveBlocks;

        private Data(ToolBase tool, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocks) {
            super(EVENT);
            this.tool = tool;
            this.toolMaterial = toolMaterial;
            this.effectiveBlocks = effectiveBlocks;
        }
    }
}
