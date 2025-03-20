package net.modificationstation.stationapi.api.item.tool;

public record SimpleToolMaterial(
        ToolLevel toolLevel,
        int itemDurability,
        float miningSpeed,
        int attackDamage
) implements AbstractToolMaterial {}
