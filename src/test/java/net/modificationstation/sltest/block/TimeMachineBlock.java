package net.modificationstation.sltest.block;

import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TimeMachineBlock extends TemplateBlock {
    public TimeMachineBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        world.setTime(world.getTime() + 1000);
        return true;
    }
}
