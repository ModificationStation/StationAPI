package net.modificationstation.sltest.item;

import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.common.util.BlockFaces;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

public class ModdedPickaxe extends TemplatePickaxe {

    public ModdedPickaxe(Identifier id, ToolMaterial material) {
        super(id, material);
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
        if (facing == BlockFaces.UP.ordinal()) {
            if (!level.isClient) {
                Living entity = (Living) EntityRegistry.create("GPoor", level);
                entity.setPosition(x + 0.5, y + 1, z + 0.5);
                level.spawnEntity(entity);
                entity.onSpawnedFromSpawner();
            }
            return true;
        } else
            return false;
    }
}
