package net.modificationstation.sltest.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.StringIdentifiable;

import java.util.List;

public class VariationBlock extends TemplateBlockBase {

    public enum Variant implements StringIdentifiable {
        IDLE,
        PASSIVE,
        ACTIVE;

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<Variant> VARIANT = EnumProperty.of("variant", Variant.class);

    public VariationBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(VARIANT);
    }

    @Override
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState blockState, int meta) {
        return List.of(new ItemInstance(switch (blockState.get(VARIANT)) {
            case IDLE -> ItemListener.variationBlockIdle;
            case PASSIVE -> ItemListener.variationBlockPassive;
            case ACTIVE -> ItemListener.variationBlockActive;
        }));
    }
}
