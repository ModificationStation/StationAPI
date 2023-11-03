package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_321;
import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_556.class)
public interface class_556Accessor {

    @Accessor("field_2401")
    Minecraft stationapi$getField_2401();

    @Accessor("field_2402")
    ItemStack stationapi$getField_2402();

    @Accessor("field_2403")
    float stationapi$getField_2403();

    @Accessor("field_2404")
    float stationapi$getField_2404();

    @Accessor("field_2405")
    BlockRenderManager stationapi$getField_2405();

    @Accessor("field_2406")
    class_321 stationapi$getField_2406();
}
