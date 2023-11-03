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
public class MixinLevelProperties {
    @Shadow private NbtCompound playerData;

    /**
     * Since Minecraft reads the entire level.dat of all worlds whenever it shows the world selection menu,
     * datafixers are getting built, introducing a severe lagspike at first click of "Singleplayer" button.
     * So in order to only update player data when it's actually accessed and not do it multiple times,
     * this boolean was added.
     */
    @Unique
    private boolean stationapi$playerDataUnchecked;

    @ModifyVariable(
            method = "getFirstEntityDataFromList(Ljava/util/List;)Lnet/minecraft/util/io/CompoundTag;",
            index = 4,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerBase;toTag(Lnet/minecraft/util/io/CompoundTag;)V",
                    shift = At.Shift.AFTER
            )
    )
    private NbtCompound addDataVersions(NbtCompound playerTag) {
        return NbtHelper.addDataVersions(playerTag);
    }

    @Unique
    private void stationapi$assertPlayerDataVersion() {
        if (stationapi$playerDataUnchecked) {
            stationapi$playerDataUnchecked = false;
            playerData = NbtHelper.update(TypeReferences.PLAYER, playerData);
        }
    }

    @Inject(
            method = "getPlayerTag()Lnet/minecraft/util/io/CompoundTag;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/LevelProperties;playerData:Lnet/minecraft/util/io/CompoundTag;",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE
            )
    )
    private void assert1(CallbackInfoReturnable<NbtCompound> cir) {
        stationapi$assertPlayerDataVersion();
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "getPlayerData()Lnet/minecraft/util/io/CompoundTag;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/LevelProperties;playerData:Lnet/minecraft/util/io/CompoundTag;",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE
            )
    )
    private void assert2(CallbackInfoReturnable<NbtCompound> cir) {
        stationapi$assertPlayerDataVersion();
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "setPlayerData(Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/LevelProperties;playerData:Lnet/minecraft/util/io/CompoundTag;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void assertTrue(NbtCompound par1, CallbackInfo ci) {
        stationapi$playerDataUnchecked = false;
    }
}
