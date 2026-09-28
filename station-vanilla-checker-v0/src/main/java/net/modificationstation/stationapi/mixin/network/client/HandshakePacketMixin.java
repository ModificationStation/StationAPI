package net.modificationstation.stationapi.mixin.network.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.handshake.HandshakePacket;
import net.minecraft.network.packet.play.DisconnectPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.modificationstation.stationapi.impl.network.VanillaChecker.SERVER_REQUIRED_MODS;

@Mixin(HandshakePacket.class)
class HandshakePacketMixin {
    @Shadow
    public String name;

    @Inject(
            method = "read",
            at = @At("TAIL")
    )
    private void stationapi_stopClientFromDying(DataInputStream par1, CallbackInfo ci) {
        String[] names = name.split(";");
        name = names[0];
        List<String> parts = Arrays.asList(names);
        if (parts.contains("stapi")) {
            HashMap<String, String> modList = new HashMap<>();
            parts.forEach(modString -> {
                String[] mod = modString.split("=");
                if (mod.length == 2) {
                    modList.put(mod[0], mod[1]);
                }
            });

            //noinspection deprecation
            Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
            ClientNetworkHandler networkHandler = ((ConnectScreenAccessor) minecraft.currentScreen).getNetworkHandler();

            for (ModContainer clientMod : SERVER_REQUIRED_MODS) {
                String serverModVersion = modList.get(clientMod.getMetadata().getId());
                if (serverModVersion == null) {
                    networkHandler.sendPacket(new DisconnectPacket("Quitting"));
                    minecraft.setScreen(new DisconnectedScreen("disconnect.lost", String.format("Server is missing mod %s %s", clientMod.getMetadata().getId(), clientMod.getMetadata().getVersion().getFriendlyString())));
                }
                else if (!serverModVersion.equals(clientMod.getMetadata().getVersion().getFriendlyString())) {
                    networkHandler.sendPacket(new DisconnectPacket("Quitting"));
                    minecraft.setScreen(new DisconnectedScreen("disconnect.lost", String.format("Server has the wrong version of mod %s %s (they have %s)", clientMod.getMetadata().getId(), clientMod.getMetadata().getVersion().getFriendlyString(), serverModVersion)));
                }
            }

            ((ModdedPacketHandlerSetter) networkHandler).setModded(modList);
            StationAPI.LOGGER.info("Got modlist from server, containing {} mods.", modList.size());
        }
    }

    @Inject(
            method = "write",
            at = @At("HEAD")
    )
    private void stationapi_injectStapiFlag(DataOutputStream par1, CallbackInfo ci) {
        this.name += ";stapi;";
    }

    @WrapOperation(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/handshake/HandshakePacket;readString(Ljava/io/DataInputStream;I)Ljava/lang/String;"))
    private String yaYeet(DataInputStream dataInputStream, int i, Operation<String> original) {
        return original.call(dataInputStream, (int) Short.MAX_VALUE);
    }
}
