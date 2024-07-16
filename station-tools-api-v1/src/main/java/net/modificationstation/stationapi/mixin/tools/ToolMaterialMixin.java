package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.MiningLevelManager;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolMaterial.class)
class ToolMaterialMixin implements StationToolMaterial {
    @Unique
    private MiningLevelManager.LevelNode stationapi_levelNode;

    @Override
    @Unique
    public ToolMaterial miningLevelNode(MiningLevelManager.LevelNode levelNode) {
        stationapi_levelNode = levelNode;
        return ToolMaterial.class.cast(this);
    }

    @Override
    @Unique
    public MiningLevelManager.LevelNode getMiningLevelNode() {
        return stationapi_levelNode;
    }
}
