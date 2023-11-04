package net.modificationstation.stationapi.mixin.audio;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.sound.BlockSoundGroup;
import net.modificationstation.stationapi.api.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockSoundGroup.class)
class BlockSoundGroupMixin {
    @Shadow @Final public String soundName;

    @ModifyExpressionValue(
            method = {
                    "getSound",
                    "getBreakSound"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/sound/BlockSoundGroup;soundName:Ljava/lang/String;",
                    opcode = Opcodes.GETFIELD
            )
    )
    private String stationapi_removeNamespaceIfPresent(
            String soundName,
            @Share("sepIndex") LocalIntRef sepIndex
    ) {
        int index = soundName.indexOf(Identifier.NAMESPACE_SEPARATOR);
        sepIndex.set(index);
        return index == -1 ? soundName : soundName.substring(index + 1);
    }

    @ModifyReturnValue(
            method = {
                    "getSound",
                    "getBreakSound"
            },
            at = @At("RETURN")
    )
    private String stationapi_readdNamespaceIfPresent(
            String soundName,
            @Share("sepIndex") LocalIntRef sepIndex
    ) {
        int index = sepIndex.get();
        return index == -1 ? soundName : this.soundName.substring(0, index + 1) + soundName;
    }
}
