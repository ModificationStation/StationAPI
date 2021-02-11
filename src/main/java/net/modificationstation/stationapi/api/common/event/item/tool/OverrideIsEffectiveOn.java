package net.modificationstation.stationapi.api.common.event.item.tool;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;

import java.util.function.Consumer;

public interface OverrideIsEffectiveOn {

    GameEventOld<OverrideIsEffectiveOn> EVENT = new GameEventOld<>(OverrideIsEffectiveOn.class,
            listeners ->
                    (toolBase, block, meta, effective) -> {
                        for (OverrideIsEffectiveOn listener : listeners)
                            effective = listener.overrideIsEffectiveOn(toolBase, block, meta, effective);
                        return effective;
                    },
            (Consumer<GameEventOld<OverrideIsEffectiveOn>>) isEffectiveOn ->
                    isEffectiveOn.register((toolLevel, block, meta, effective) -> {
                        Data data = new Data(toolLevel, block, meta, effective);
                        GameEventOld.EVENT_BUS.post(data);
                        return data.effective;
                    })
    );

    boolean overrideIsEffectiveOn(ToolLevel toolLevel, BlockBase block, int meta, boolean effective);

    final class Data extends GameEventOld.Data<OverrideIsEffectiveOn> {

        public final ToolLevel toolLevel;
        public final BlockBase block;
        public final int meta;
        public boolean effective;

        private Data(ToolLevel toolLevel, BlockBase block, int meta, boolean effective) {
            super(EVENT);
            this.toolLevel = toolLevel;
            this.block = block;
            this.meta = meta;
            this.effective = effective;
        }
    }
}
