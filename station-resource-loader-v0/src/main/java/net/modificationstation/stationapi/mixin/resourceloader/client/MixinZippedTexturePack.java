package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.InputStream;

@Mixin(ZippedTexturePack.class)
public class MixinZippedTexturePack extends TexturePack {

    @Redirect(
            method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Class;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"
            )
    )
    private InputStream fakeResource(Class<TexturePack> instance, String path) {
        return super.getResourceAsStream(path);
    }
}
