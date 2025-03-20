package net.modificationstation.stationapi.api.item.tool;

import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial extends AbstractToolMaterial {
    @Override
    default ToolLevel toolLevel() {
        return Util.assertImpl();
    }

    @Override
    default int itemDurability() {
        return Util.assertImpl();
    }

    @Override
    default float miningSpeed() {
        return Util.assertImpl();
    }

    @Override
    default int attackDamage() {
        return Util.assertImpl();
    }
}
