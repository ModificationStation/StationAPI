package net.modificationstation.stationloader.mixin.common;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.packet.StationHandshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(HandshakeC2S.class)
public abstract class MixinHandshakeC2S extends AbstractPacket implements StationHandshake {

    @Getter
    public String
            stationLoader,
            version;
    @Getter
    public Map<String, String> mods;

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Ljava/lang/String;I)V", at = @At("RETURN"))
    private void newClient(String string, int i, CallbackInfo ci) {
        ModMetadata slData = StationLoader.INSTANCE.getContainer().getMetadata();
        stationLoader = slData.getId();
        version = slData.getVersion().getFriendlyString();
        mods = new HashMap<>();
        StationLoader.INSTANCE.getModsToVerifyOnClient().forEach(modContainer -> {
            ModMetadata modMetadata = modContainer.getMetadata();
            mods.put(modMetadata.getId(), modMetadata.getVersion().getFriendlyString());
        });
    }

    @Environment(EnvType.SERVER)
    @Inject(method = "read(Ljava/io/DataInputStream;)V", at = @At("RETURN"))
    private void read(DataInputStream in, CallbackInfo ci) throws IOException {
        String sl;
        if (in.available() >= 2) {
            sl = method_802(in, MAX_STRING_LENGTH);
            if (in.available() >= 2) {
                stationLoader = sl;
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
        if (stationLoader != null && version != null && mods != null) {
            writeString(stationLoader, out);
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
        if (stationLoader != null && version != null && mods != null) {
            extra.set(8 + stationLoader.length() + version.length());
            mods.forEach((key, value) -> extra.addAndGet(4 + key.length() * 2 + value.length() * 2));
        }
        cir.setReturnValue(cir.getReturnValueI() + extra.get());
    }

    private static final int MAX_STRING_LENGTH = 32767;
}
