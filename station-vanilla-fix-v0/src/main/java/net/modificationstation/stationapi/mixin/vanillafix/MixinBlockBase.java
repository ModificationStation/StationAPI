package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Wool;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Wool;"
            )
    )
    private static Wool stopRegisterWoolItem(int id) throws InstantiationException {
        return (Wool) UnsafeProvider.theUnsafe.allocateInstance(Wool.class);
    }
}
