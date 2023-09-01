package net.modificationstation.stationapi.mixin.debugfixes;

import net.minecraft.client.util.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(
        value = ThreadDownloadResources.class,
        priority = 0
)
class DestroyStupidResource404Errors {
    @ModifyConstant(
            method = "run",
            constant = @Constant(stringValue = "http://s3.amazonaws.com/MinecraftResources/"),
            remap = false,
            require = 0
    )
    private String run(String string) {
        return "https://resourceproxy.pymcl.net/MinecraftResources/";
    }
}
