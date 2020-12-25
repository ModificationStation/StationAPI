package net.modificationstation.stationapi.api.common.event.item.tool;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.event.GameEvent;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;

import java.util.function.Consumer;

public interface OverrideIsEffectiveOn {

    GameEvent<OverrideIsEffectiveOn> EVENT = new GameEvent<>(OverrideIsEffectiveOn.class,
            listeners ->
                    (toolBase, block, meta, effective) -> {
                        for (OverrideIsEffectiveOn listener : listeners)
                            effective = listener.overrideIsEffectiveOn(toolBase, block, meta, effective);
                        return effective;
                    },
            (Consumer<GameEvent<OverrideIsEffectiveOn>>) isEffectiveOn ->
                    isEffectiveOn.register((toolLevel, block, meta, effective) -> {
                        Data data = new Data(toolLevel, block, meta, effective);
                        GameEvent.EVENT_BUS.post(data);
                        return data.isEffective();
                    })
    );

    boolean overrideIsEffectiveOn(ToolLevel toolLevel, BlockBase block, int meta, boolean effective);

    final class Data extends GameEvent.Data<OverrideIsEffectiveOn> {

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
