package net.modificationstation.sltest.item;

import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;
import net.modificationstation.stationapi.api.util.math.Direction;

public class ModdedPickaxe extends TemplatePickaxe {//implements ToolLevel {

    public ModdedPickaxe(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
        if (facing == Direction.UP.ordinal()) {
            if (!level.isClient) {
                Living entity = (Living) EntityRegistry.create("GPoor", level);
                entity.setPosition(x + 0.5, y + 1, z + 0.5);
                level.spawnEntity(entity);
                entity.onSpawnedFromSpawner();
            }
            item.cooldown = 20;
            return true;
        } else
            return false;
    }

    //@Override
    public int getToolLevel() {
        return 3;
    }

    //@Override
    public ToolMaterial getMaterial() {
        return toolMaterial;
    }
}
