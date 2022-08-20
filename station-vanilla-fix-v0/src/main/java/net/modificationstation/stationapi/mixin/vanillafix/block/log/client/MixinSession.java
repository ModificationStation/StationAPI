package net.modificationstation.stationapi.mixin.vanillafix.block.log.client;

import net.minecraft.client.util.Session;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Session.class)
public class MixinSession {

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 5
            )
    )
    private static boolean redirect(List<Object> instance, Object e) {
        return instance.add(Blocks.OAK_LOG);
    }
}
