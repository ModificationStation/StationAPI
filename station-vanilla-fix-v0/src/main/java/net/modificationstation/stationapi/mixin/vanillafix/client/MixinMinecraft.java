package net.modificationstation.stationapi.mixin.vanillafix.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.level.storage.LevelStorage;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.impl.level.storage.FlattenedWorldStorage;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.CURRENT_VERSION;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow private LevelStorage levelStorage;

    @ModifyArg(method = "convertWorldFormat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListenerImpl;notifyWithGameRunning(Ljava/lang/String;)V"))
    private String changeProgressListenerTitle(String string, @Local(ordinal = 0) String worldName) {
        CompoundTag worldTag = ((FlattenedWorldStorage)this.levelStorage).getWorldTag(worldName);

        List<Triple<ModID, Integer, Integer>> updateList = NbtHelper.getUpdateList(worldTag);

        for (Triple<ModID, Integer, Integer> triple : updateList) {
            if (triple.getLeft().equals(MODID) && triple.getRight().equals(CURRENT_VERSION)) {
                return string;
            }
        }

        return "Updating Data for Mods:";
    }

    @ModifyArg(method = "convertWorldFormat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ProgressListenerImpl;method_1796(Ljava/lang/String;)V"))
    private String changeProgressListenerDesc(String string, @Local(ordinal = 0) String worldName) {
        CompoundTag worldTag = ((FlattenedWorldStorage)this.levelStorage).getWorldTag(worldName);

        List<Triple<ModID, Integer, Integer>> updateList = NbtHelper.getUpdateList(worldTag);

        List<ModID> list = new ArrayList<>();

        for (Triple<ModID, Integer, Integer> triple : updateList) {
            if (triple.getLeft().equals(MODID) && triple.getRight().equals(CURRENT_VERSION)) {
                return string;
            }

            list.add(triple.getLeft());
        }

        return list.stream().map(ModID::toString).collect(Collectors.joining(", "));
    }
}
