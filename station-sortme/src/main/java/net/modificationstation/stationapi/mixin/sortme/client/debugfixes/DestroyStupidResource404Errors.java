package net.modificationstation.stationapi.mixin.sortme.client.debugfixes;

import net.minecraft.client.util.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ThreadDownloadResources.class, priority = 0)
public class DestroyStupidResource404Errors {

    @ModifyConstant(method = "run", constant = @Constant(stringValue = "http://s3.amazonaws.com/MinecraftResources/"), remap = false, require = 0)
    public String run(String string) {
        return "https://resourceproxy.pymcl.net/MinecraftResources/";
    }
}
