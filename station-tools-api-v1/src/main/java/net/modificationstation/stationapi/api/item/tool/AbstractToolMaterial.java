package net.modificationstation.stationapi.api.item.tool;

public interface AbstractToolMaterial {
    ToolLevel toolLevel();

    int itemDurability();

    float miningSpeed();

    int attackDamage();
}
