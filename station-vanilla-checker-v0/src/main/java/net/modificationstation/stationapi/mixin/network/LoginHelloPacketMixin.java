package net.modificationstation.stationapi.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModListHelloPacket;
import net.modificationstation.stationapi.mixin.network.client.ConnectScreenAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mixin(LoginHelloPacket.class)
class LoginHelloPacketMixin implements ModListHelloPacket {
    @Shadow public String username;
    @Unique
    private HashMap<String, String> modList;

    @Environment(EnvType.CLIENT)
    @WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/login/LoginHelloPacket;writeString(Ljava/lang/String;Ljava/io/DataOutputStream;)V"))
    private void stationapi_giveTheSauce(String s, DataOutputStream dataOutputStream, Operation<Void> original) {
        //noinspection deprecation
        if (((ModdedPacketHandler) ((ConnectScreenAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).currentScreen).getNetworkHandler()).isModded()) {
            StringBuilder builder = new StringBuilder(";");
            FabricLoader.getInstance().getAllMods().forEach(mod -> builder.append(mod.getMetadata().getId()).append("=").append(mod.getMetadata().getVersion()).append(";"));
            s += builder.toString();
            StationAPI.LOGGER.info("Sending modlist to server.");
        }
        original.call(s, dataOutputStream);
    }

    @Environment(EnvType.SERVER)
    @Inject(method = "read", at = @At("TAIL"))
    private void stationapi_readTheSauce(DataInputStream par1, CallbackInfo ci) {
        if (username.contains(";")) {
            modList = new HashMap<>();
            Arrays.asList(username.split(";")).forEach(modString -> {
                String[] mod = modString.split("=");
                if (mod.length == 2) {
                    modList.put(mod[0], mod[1]);
                }
            });
            username = username.split(";")[0];
            StationAPI.LOGGER.info("Got modlist from client, containing {} mods.", modList.size());
        }
    }

    @Override
    public Map<String, String> stationapi_getModList() {
        return modList;
    }

    @ModifyConstant(
            method = "read",
            constant = @Constant(intValue = 16)
    )
    private int stationapi_preventTheWorldFromExploding(int constant) {
        return Short.MAX_VALUE;
    }
}
