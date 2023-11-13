package net.modificationstation.stationapi.mixin.vanillafix;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldProperties.class)
class WorldPropertiesMixin {
    @Shadow private NbtCompound playerNbt;

    /**
     * Since Minecraft reads the entire level.dat of all worlds whenever it shows the world selection menu,
     * datafixers are getting built, introducing a severe lagspike at first click of "Singleplayer" button.
     * So in order to only update player data when it's actually accessed and not do it multiple times,
     * this boolean was added.
     */
    @Unique
    private boolean stationapi_playerNbtUnchecked;

    @ModifyVariable(
            method = "asNbt(Ljava/util/List;)Lnet/minecraft/nbt/NbtCompound;",
            index = 4,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;write(Lnet/minecraft/nbt/NbtCompound;)V",
                    shift = At.Shift.AFTER
            )
    )
    private NbtCompound stationapi_addDataVersions(NbtCompound playerTag) {
        return NbtHelper.addDataVersions(playerTag);
    }

    @Unique
    private void stationapi_assertPlayerDataVersion() {
        if (stationapi_playerNbtUnchecked) {
            stationapi_playerNbtUnchecked = false;
            playerNbt = NbtHelper.update(TypeReferences.PLAYER, playerNbt);
        }
    }

    @Inject(
            method = "getPlayerNbt",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/LevelProperties;playerData:Lnet/minecraft/util/io/CompoundTag;",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_assert1(CallbackInfoReturnable<NbtCompound> cir) {
        stationapi_assertPlayerDataVersion();
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "getPlayerNbt",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/WorldProperties;playerNbt:Lnet/minecraft/nbt/NbtCompound;",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_assert2(CallbackInfoReturnable<NbtCompound> cir) {
        stationapi_assertPlayerDataVersion();
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "setPlayerNbt",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/WorldProperties;playerNbt:Lnet/minecraft/nbt/NbtCompound;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_assertTrue(NbtCompound par1, CallbackInfo ci) {
        stationapi_playerNbtUnchecked = false;
    }
}
