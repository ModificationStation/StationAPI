package net.modificationstation.stationloader.api.common.event.item.tool;

import lombok.Getter;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.List;
import java.util.function.Consumer;

public interface EffectiveBlocksProvider {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<EffectiveBlocksProvider> EVENT = new SimpleEvent<>(EffectiveBlocksProvider.class,
            listeners ->
                    (tool, toolMaterial, effectiveBlocks) -> {
                        for (EffectiveBlocksProvider listener : listeners)
                            listener.getEffectiveBlocks(tool, toolMaterial, effectiveBlocks);
                    }, (Consumer<SimpleEvent<EffectiveBlocksProvider>>) effectiveBlocksProvider ->
            effectiveBlocksProvider.register((tool, toolMaterial, effectiveBlocks) -> SimpleEvent.EVENT_BUS.post(new Data(tool, toolMaterial, effectiveBlocks)))
    );

    void getEffectiveBlocks(ToolBase tool, ToolMaterial toolMaterial, List<BlockBase> effectiveBlocks);

    final class Data extends SimpleEvent.Data<EffectiveBlocksProvider> {

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
