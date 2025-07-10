package net.modificationstation.stationapi.mixin.network.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.packet.handshake.HandshakePacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModListHelloPacket;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.impl.network.VanillaChecker.CLIENT_REQUIRED_MODS;

@Mixin(ServerLoginNetworkHandler.class)
abstract class ServerLoginNetworkHandlerMixin implements ModdedPacketHandler, ModdedPacketHandlerSetter {

    @Shadow public abstract void disconnect(String reason);

    @Shadow private String username;

    @WrapOperation(method = "onHandshake", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/network/packet/handshake/HandshakePacket;"))
    private HandshakePacket stationapi_tellThemIModToo(String s, Operation<HandshakePacket> original, @Local(argsOnly = true) HandshakePacket handshakePacket) {
        if (Arrays.asList(handshakePacket.name.split(";")).contains("stapi")) {
            s += ";stapi;";
            StringBuilder builder = new StringBuilder(s);
            FabricLoader.getInstance().getAllMods().forEach(mod -> builder.append(mod.getMetadata().getId()).append("=").append(mod.getMetadata().getVersion()).append(";"));
            s += builder.toString();
            StationAPI.LOGGER.info("Sending modlist to client.");
        }
        return original.call(s);
    }

    @Inject(method = "accept", at = @At(value = "HEAD"), cancellable = true)
    private void stationapi_setAndCheckModList(LoginHelloPacket arg, CallbackInfo ci) {
        ModListHelloPacket modListHelloPacket = (ModListHelloPacket) arg;
        if (((ModListHelloPacket) arg).stationAPI$getModList() != null) {
            setModded(modListHelloPacket.stationAPI$getModList());
            StationAPI.LOGGER.info("Applied modlist from client.");
        }
        if (!isModded()) {
            LOGGER.error("Player \"{}\" attempted joining the server without {}, disconnecting.", arg.username, NAMESPACE.getName());
            disconnect("Station API is required to join this server.");
        }

        LOGGER.info("Player \"{}\"'s mods: {}", arg.username, getMods().entrySet().stream().map(stringStringEntry -> "modid=" + stringStringEntry.getKey() + " version=" + stringStringEntry.getValue()).collect(Collectors.joining(", ", "[", "]")));

        if (!CLIENT_REQUIRED_MODS.isEmpty()) {
            LOGGER.info("Validating modlist of \"{}\"...", arg.username);
            String version = getMods().get(NAMESPACE.toString());
            String serverStationVersion = NAMESPACE.getVersion().getFriendlyString();
            if (!version.equals(serverStationVersion)) {
                LOGGER.error("Player \"{}\" has a mismatching {} version \"{}\", disconnecting.", arg.username, NAMESPACE.getName(), version);
                disconnect(String.format("This server runs on Station API %s (you have %s).", serverStationVersion, version));
                ci.cancel();
                return;
            }
            String modid;
            String clientVersion;
            String serverVersion;
            for (ModContainer serverMod : CLIENT_REQUIRED_MODS) {
                ModMetadata modMetadata = serverMod.getMetadata();
                modid = modMetadata.getId();
                serverVersion = modMetadata.getVersion().getFriendlyString();
                if (getMods().containsKey(modid)) {
                    clientVersion = getMods().get(modid);
                    if (clientVersion == null || !clientVersion.equals(serverVersion)) {
                        LOGGER.error("Player \"{}\" has a mismatching {} ({}) version \"{}\", disconnecting.", arg.username, modMetadata.getName(), modid, clientVersion);
                        disconnect(String.format("%s (%s) %s is required (you have %s).", modMetadata.getName(), modid, serverVersion, clientVersion == null ? "null" : clientVersion));
                        ci.cancel();
                        return;
                    }
                } else {
                    LOGGER.error("Player \"{}\" has a missing mod {} ({}), disconnecting.", arg.username, modMetadata.getName(), modid);
                    disconnect(I18n.getTranslation("%s (%s) %s is required to join.", modMetadata.getName(), modid, serverVersion));
                    ci.cancel();
                    return;
                }
            }
            LOGGER.info("Player \"{}\"'s mods have passed verification.", arg.username);
        }
    }
}
