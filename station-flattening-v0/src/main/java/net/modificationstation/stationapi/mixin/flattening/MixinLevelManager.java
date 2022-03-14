package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.level.LevelManager;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import net.modificationstation.stationapi.impl.level.chunk.StationHeigtmapProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(LevelManager.class)
public class MixinLevelManager {
    @Unique
    private static final String SECTIONS_TAG = of(MODID, "sections").toString();
    @Unique
    private static final String HEIGHTMAP_KEY = "HeightMap";
    @Unique
    private static final String HEIGHT_KEY = "Y";

    @ModifyConstant(
            method = "getChunk(Lnet/minecraft/level/Level;II)Lnet/minecraft/level/chunk/Chunk;",
            constant = @Constant(stringValue = "Blocks")
    )
    private String getBlocksTag(String constant) {
        return SECTIONS_TAG;
    }

    @Redirect(
            method = "method_1480(Lnet/minecraft/level/chunk/Chunk;Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
                    ordinal = 0
            )
    )
    private static void stopSavingBlocks(CompoundTag instance, String item, byte[] bytes) {}

    @Inject(
            method = "method_1480(Lnet/minecraft/level/chunk/Chunk;Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private static void saveStationData(Chunk chunk, Level level, CompoundTag tag, CallbackInfo ci) {
        ChunkSection[] sections = ChunkSectionsAccessor.class.cast(chunk).getSections();
        ListTag listTag = new ListTag();
        for(int i = 0; i < sections.length; ++i) {
            ChunkSection section = sections[i];
            if (section != ChunkSection.EMPTY_SECTION) {
                listTag.add(section.toNBT());
            }
        }
        tag.put(SECTIONS_TAG, listTag);
        StationHeigtmapProvider provider = StationHeigtmapProvider.class.cast(chunk);
        tag.put(HEIGHTMAP_KEY, provider.getStoredHeightmap());
    }

    @Redirect(
            method = "method_1479(Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;getByteArray(Ljava/lang/String;)[B",
                    ordinal = 0
            )
    )
    private static byte[] stopLoadingBlocks(CompoundTag instance, String s) {
        return null;
    }

    @Inject(
            method = "method_1479(Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void loadStationData(Level level, CompoundTag chunkTag, CallbackInfoReturnable<Chunk> info, int var2, int var3, Chunk chunk) {
        ChunkSection[] sections = ChunkSectionsAccessor.class.cast(chunk).getSections();
        if (chunkTag.containsKey(SECTIONS_TAG)) {
            ListTag listTag = chunkTag.getListTag(SECTIONS_TAG);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag sectionTag = CompoundTag.class.cast(listTag.get(i));
                int sectionY = sectionTag.getByte(HEIGHT_KEY);
                if (sectionY < 0 || sectionY >= sections.length) continue;
                ChunkSection chunkSection = new ChunkSection(sectionY << 4);
                chunkSection.fromNBT(chunkTag, sectionTag);
                sections[sectionY] = chunkSection;
            }
        }
        StationHeigtmapProvider provider = StationHeigtmapProvider.class.cast(chunk);
        provider.loadStoredHeightmap(chunkTag.getByteArray(HEIGHTMAP_KEY));
    }

    @Redirect(
            method = "method_1479(Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
                    args = "array=length"
            )
    )
    private static int getTileLength(byte[] array) {
        return '耀';
    }
    
    @Redirect(
        method = "method_1480",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
            ordinal = 4
        )
    )
    private static void disableHeightmapSaving(CompoundTag compoundTag, String key, byte[] item) {}
    
    @Redirect(
        method = "method_1479",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;getByteArray(Ljava/lang/String;)[B",
            ordinal = 4
        )
    )
    private static byte[] stopLoadingHeightmap(CompoundTag instance, String s) {
        return null;
    }
    
    @Redirect(
        method = "method_1480",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
            ordinal = 1
        )
    )
    private static void disableMetaSaving(CompoundTag compoundTag, String key, byte[] item) {}
    
    @Redirect(
        method = "method_1480",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
            ordinal = 2
        )
    )
    private static void disableSkyLightSaving(CompoundTag compoundTag, String key, byte[] item) {}
    
    @Redirect(
        method = "method_1480",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
            ordinal = 3
        )
    )
    private static void disableBlockLightSaving(CompoundTag compoundTag, String key, byte[] item) {}
    
    @Redirect(
        method = "method_1479",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;getByteArray(Ljava/lang/String;)[B",
            ordinal = 1
        )
    )
    private static byte[] stopLoadingMeta(CompoundTag instance, String s) {
        return null;
    }
    
    @Redirect(
        method = "method_1479",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;getByteArray(Ljava/lang/String;)[B",
            ordinal = 2
        )
    )
    private static byte[] stopLoadingSkyLight(CompoundTag instance, String s) {
        return null;
    }
    
    @Redirect(
        method = "method_1479",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/io/CompoundTag;getByteArray(Ljava/lang/String;)[B",
            ordinal = 3
        )
    )
    private static byte[] stopLoadingBlockLight(CompoundTag instance, String s) {
        return null;
    }
}
