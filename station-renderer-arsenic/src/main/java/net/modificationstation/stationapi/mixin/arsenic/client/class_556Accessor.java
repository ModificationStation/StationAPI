package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HeldItemRenderer.class)
public interface class_556Accessor {
    @Accessor("minecraft")
    Minecraft stationapi$getMinecraft();

    @Accessor("stack")
    ItemStack stationapi$getStack();

    @Accessor("height")
    float stationapi$getHeight();

    @Accessor("prevHeight")
    float stationapi$getPrevHeight();

    @Accessor("blockRenderManager")
    BlockRenderManager stationapi$getBlockRenderManager();

    @Accessor("mapRenderer")
    MapRenderer stationapi$getMapRenderer();
}
