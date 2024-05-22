package net.modificationstation.sltest.block;

import net.minecraft.block.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.sltest.celestial.CelestialListener;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class TimeMachineBlock extends TemplateBlock {
    public TimeMachineBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setTickRandomly(true);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        world.setTime(world.getTime() + 1000);
        return true;
    }

    @Override
    public void onTick(World world, int x, int y, int z, Random random) {
        super.onTick(world, x, y, z, random);
        if (CelestialListener.flyingDimando.isActive()) {
            world.method_287(new ItemEntity(world, x + 0.5, y + 1, z + 0.5, new ItemStack(Item.DIAMOND)));
        }
    }
}
