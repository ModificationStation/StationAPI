package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.sltest.celestial.CelestialListener;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class CelestialToggleItem extends TemplateItem {
    public CelestialToggleItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ItemStack use(ItemStack item, World world, PlayerEntity player) {
        if (CelestialListener.fallingDimando.isActive()) {
            CelestialListener.fallingDimando.stopEvent();
            System.out.println("Stopping Falling Dimando");
        } else if (CelestialListener.fallingDimando.activateEvent(world.getTime(), random)) {
            System.out.println("Activating Falling Dimando");
        } else {
            System.out.println("Falling Dimando not activated");
        }
        if (CelestialListener.flyingDimando.isActive()) {
            CelestialListener.flyingDimando.stopEvent();
            System.out.println("Stopping Flying Dimando");
        } else if (CelestialListener.flyingDimando.activateEvent(world.getTime(), random)) {
            System.out.println("Activating Flying Dimando");
        } else {
            System.out.println("Flying Dimando not activated");
        }
        return super.use(item, world, player);
    }
}
