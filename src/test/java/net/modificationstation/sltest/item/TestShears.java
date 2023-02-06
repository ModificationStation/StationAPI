package net.modificationstation.sltest.item;

import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateShears;
import net.modificationstation.stationapi.api.util.math.Direction;

public class TestShears extends TemplateShears {
    public TestShears(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
        if (player.method_1373()) {
            if (!level.isServerSide) {
                level.setTile(x, y, z, 0);
            }
            item.cooldown = 20;
            return true;
        } else if (facing == Direction.UP.ordinal()) {
            if (!level.isServerSide) {
                Living entity = (Living) EntityRegistry.create("Sheep", level);
                entity.setPosition(x + 0.5, y + 1, z + 0.5);
                level.spawnEntity(entity);
                entity.onSpawnedFromSpawner();
            }
            item.cooldown = 20;
            return true;
        } else
            return false;
    }
}
