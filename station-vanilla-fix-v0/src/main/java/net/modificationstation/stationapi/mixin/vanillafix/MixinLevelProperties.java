package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.vanillafix.datafixer.TypeReferences;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    @Shadow private CompoundTag playerData;

    @Redirect(
            method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/LevelProperties;playerData:Lnet/minecraft/util/io/CompoundTag;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void updatePlayer(LevelProperties instance, CompoundTag playerTag) {
        playerData = NbtHelper.update(TypeReferences.PLAYER, playerTag);
    }

    @ModifyVariable(
            method = "getFirstEntityDataFromList(Ljava/util/List;)Lnet/minecraft/util/io/CompoundTag;",
            index = 4,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerBase;toTag(Lnet/minecraft/util/io/CompoundTag;)V",
                    shift = At.Shift.AFTER
            )
    )
    private CompoundTag addDataVersions(CompoundTag playerTag) {
        return (CompoundTag) DataFixers.addDataVersions(NbtOps.INSTANCE, playerTag);
    }
}
