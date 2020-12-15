package net.modificationstation.stationloader.api.common.event.item.tool;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;

import java.util.function.Consumer;

public interface IsEffectiveOn {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<IsEffectiveOn> EVENT = new SimpleEvent<>(IsEffectiveOn.class,
            listeners ->
                    (toolBase, block, meta, effective) -> {
                        for (IsEffectiveOn listener : listeners)
                            effective = listener.isEffectiveOn(toolBase, block, meta, effective);
                        return effective;
                    },
            (Consumer<SimpleEvent<IsEffectiveOn>>) isEffectiveOn ->
                    isEffectiveOn.register((toolLevel, block, meta, effective) -> {
                        Data data = new Data(toolLevel, block, meta, effective);
                        SimpleEvent.EVENT_BUS.post(data);
                        return data.isEffective();
                    })
    );

    boolean isEffectiveOn(ToolLevel toolLevel, BlockBase block, int meta, boolean effective);

    final class Data extends SimpleEvent.Data<IsEffectiveOn> {

        @Getter
        private final ToolLevel toolLevel;
        @Getter
        private final BlockBase block;
        @Getter
        private final int meta;
        @Getter
        @Setter
        private boolean effective;

        private Data(ToolLevel toolLevel, BlockBase block, int meta, boolean effective) {
            super(EVENT);
            this.toolLevel = toolLevel;
            this.block = block;
            this.meta = meta;
            this.effective = effective;
        }
    }
}
