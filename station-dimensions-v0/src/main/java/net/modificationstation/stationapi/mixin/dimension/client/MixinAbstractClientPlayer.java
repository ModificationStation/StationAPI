package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.level.dimension.VanillaDimensions;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends PlayerBase {

    public MixinAbstractClientPlayer(Level arg) {
        super(arg);
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @ModifyConstant(
            method = "respawn()V",
            constant = @Constant(intValue = 0)
    )
    private int getRespawnDimension(int constant) {
        return level.dimension.canPlayerSleep() ? dimensionId : DimensionRegistry.INSTANCE.getSerialID(VanillaDimensions.OVERWORLD).orElseThrow(() -> new RuntimeException("Couldn't find overworld dimension in the registry!"));
    }
}
