package net.modificationstation.stationapi.mixin.worldgen.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Material;
import net.minecraft.class_555;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_555.class)
class GameRendererMixin {
    @Unique private float stationapi_multiplierA;
    @Unique private float stationapi_multiplierB;
    @Shadow private Minecraft field_2349;
    @Shadow float field_2346;
    @Shadow float field_2347;
    @Shadow float field_2348;
    
    @WrapOperation(method = "method_1852", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;method_221(F)F"
    ))
    private float statioapi_captureRainMultiplier(World world, float delta, Operation<Float> original) {
        float value = original.call(world, delta);
        if (value > 0.0F) {
            stationapi_multiplierA = 1.0F - value * 0.5F;
            stationapi_multiplierB = 1.0F - value * 0.4F;
        }
        else {
            stationapi_multiplierA = 1.0F;
            stationapi_multiplierB = 1.0F;
        }
        return value;
    }
    
    @WrapOperation(method = "method_1852", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;method_213(F)F"
    ))
    private float statioapi_captureThunderMultiplier(World world, float delta, Operation<Float> original) {
        float value = original.call(world, delta);
        if (value > 0.0F) {
            float multiplier = 1.0f - value * 0.5f;
            stationapi_multiplierA *= multiplier;
            stationapi_multiplierB *= multiplier;
        }
        return value;
    }

    @Inject(
            method = "method_1842(IF)V",
            at = @At("HEAD")
    )
    private void stationapi_changeFogColor(int i, float delta, CallbackInfo info) {
        LivingEntity livingEntity = this.field_2349.field_2807;
        if (!livingEntity.isInFluid(Material.WATER) && !livingEntity.isInFluid(Material.LAVA)) {
            FogRendererImpl.setupFog(field_2349, delta);
            field_2346 = FogRendererImpl.getR() * stationapi_multiplierA;
            field_2347 = FogRendererImpl.getG() * stationapi_multiplierA;
            field_2348 = FogRendererImpl.getB() * stationapi_multiplierB;
        }
    }

    @Inject(
            method = "method_1852(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glClearColor(FFFF)V",
                    remap = false,
                    shift = Shift.AFTER
            )
    )
    private void stationapi_clearWithFogColor(float delta, CallbackInfo info) {
        LivingEntity livingEntity = this.field_2349.field_2807;
        if (!livingEntity.isInFluid(Material.WATER) && !livingEntity.isInFluid(Material.LAVA)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glClearColor(
                FogRendererImpl.getR() * stationapi_multiplierA,
                FogRendererImpl.getG() * stationapi_multiplierA,
                FogRendererImpl.getB() * stationapi_multiplierB,
                1F
            );
        }
    }
}
