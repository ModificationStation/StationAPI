package net.modificationstation.stationapi.mixin.sortme.common;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.common.packet.StationHandshake;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@Mixin(HandshakeC2S.class)
public abstract class MixinHandshakeC2S extends AbstractPacket implements StationHandshake {

    private static final int MAX_STRING_LENGTH = 32767;
    @Getter
    public String
            stationAPI,
            version;
    @Getter
    public Map<String, String> mods;

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Ljava/lang/String;I)V", at = @At("RETURN"))
    private void newClient(String string, int i, CallbackInfo ci) {
        ModID modID = StationAPI.MODID;
        stationAPI = modID.toString();
        version = modID.getVersion().getFriendlyString();
        mods = new HashMap<>();
        StationAPI.INSTANCE.getModsToVerifyOnClient().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            mods.put(modMetadata.getId(), modMetadata.getVersion().getFriendlyString());
        });
    }

    @Environment(EnvType.SERVER)
    @Inject(method = "read(Ljava/io/DataInputStream;)V", at = @At("RETURN"))
    private void read(DataInputStream in, CallbackInfo ci) throws IOException {
        String station;
        if (in.available() >= 2) {
            station = method_802(in, MAX_STRING_LENGTH);
            if (in.available() >= 2) {
                stationAPI = station;
                version = method_802(in, MAX_STRING_LENGTH);
                mods = new HashMap<>();
                int size = in.readInt();
                for (int i = 0; i < size; i++)
                    mods.put(method_802(in, MAX_STRING_LENGTH), method_802(in, MAX_STRING_LENGTH));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "write(Ljava/io/DataOutputStream;)V", at = @At("RETURN"))
    private void write(DataOutputStream out, CallbackInfo ci) {
        if (stationAPI != null && version != null && mods != null) {
            writeString(stationAPI, out);
            writeString(version, out);
            try {
                out.writeInt(mods.size());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mods.forEach((key, value) -> {
                writeString(key == null ? "" : key, out);
                writeString(value == null ? "" : value, out);
            });
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "length()I", at = @At("RETURN"), cancellable = true)
    private void length(CallbackInfoReturnable<Integer> cir) {
        AtomicInteger extra = new AtomicInteger();
        if (stationAPI != null && version != null && mods != null) {
            extra.set(8 + stationAPI.length() + version.length());
            mods.forEach((key, value) -> extra.addAndGet(4 + key.length() * 2 + value.length() * 2));
        }
        cir.setReturnValue(cir.getReturnValueI() + extra.get());
    }
}
