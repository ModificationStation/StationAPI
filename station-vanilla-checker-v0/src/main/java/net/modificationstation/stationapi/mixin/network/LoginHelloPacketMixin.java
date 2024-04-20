package net.modificationstation.stationapi.mixin.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.login.LoginHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.*;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.impl.network.VanillaChecker.MASK;

@Mixin(LoginHelloPacket.class)
class LoginHelloPacketMixin {
    @Shadow public String username;

    @Shadow public long worldSeed;

    @Inject(
            method = "<init>(Ljava/lang/String;IJB)V",
            at = @At("RETURN")
    )
    @Environment(EnvType.SERVER)
    private void stationapi_injectStAPIFlagAndModList(String username, int protocolVersion, long worldSeed, byte dimensionId, CallbackInfo ci) {
        this.username += NAMESPACE + ";";

        List<String> mods = FabricLoader.getInstance().getAllMods().stream().map((modContainer -> modContainer.getMetadata().getId() + "=" + modContainer.getMetadata().getVersion().getFriendlyString())).toList();
        mods.forEach(mod -> this.username += mod + ":");
        this.username = this.username.replaceFirst(":$", "");
        this.username += ";";
    }

    @Inject(
            method = "<init>(Ljava/lang/String;I)V",
            at = @At("RETURN")
    )
    @Environment(EnvType.CLIENT)
    private void stationapi_injectStAPIFlag(String username, int protocolVersion, CallbackInfo ci) {
        worldSeed |= MASK;
    }

    @ModifyConstant(
            method = "read",
            constant = @Constant(
                    ordinal = 0,
                    intValue = 16
            )
    )
    private int stationapi_injectHugeStringLimit(int constant) {
        return Short.MAX_VALUE;
    }

}
