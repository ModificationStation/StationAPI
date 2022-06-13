package net.modificationstation.stationapi.mixin.flattening;

import com.mojang.serialization.Codec;
import net.minecraft.level.Level;
import net.minecraft.level.LevelManager;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import net.modificationstation.stationapi.impl.level.chunk.PalettedContainer;
import net.modificationstation.stationapi.impl.level.chunk.StationHeigtmapProvider;
import net.modificationstation.stationapi.impl.nbt.NbtOps;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(LevelManager.class)
public class MixinLevelManager {

    @Unique
    private static final Codec<PalettedContainer<BlockState>> CODEC = PalettedContainer.createCodec(States.STATE_IDS, BlockState.CODEC, PalettedContainer.PaletteProvider.BLOCK_STATE, States.AIR.get());
    @Unique
    private static final String SECTIONS_TAG = of(MODID, "sections").toString();
    @Unique
    private static final String METADATA_KEY = "data";
    @Unique
    private static final String SKY_LIGHT_KEY = "sky_light";
    @Unique
    private static final String BLOCK_LIGHT_KEY = "block_light";
    @Unique
    private static final String HEIGHTMAP_KEY = "height_map";
    @Unique
    private static final String HEIGHT_KEY = "y";

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
        ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
        ListTag listTag = new ListTag();
        HeightLimitView heightLimitView = (HeightLimitView) level;
        for (int sectionY = heightLimitView.getBottomSectionCoord(); sectionY < heightLimitView.getTopSectionCoord() + 2; ++sectionY) {
            int index = heightLimitView.sectionCoordToIndex(sectionY);
            if (index < 0 || index >= sections.length) continue;
            ChunkSection section = sections[index];
            if (section != ChunkSection.EMPTY_SECTION) {
                CompoundTag sectionTag = new CompoundTag();
                sectionTag.put(HEIGHT_KEY, (byte)sectionY);
                sectionTag.put("block_states", CODEC.encodeStart(NbtOps.INSTANCE, section.getBlockStateContainer()).getOrThrow(false, LOGGER::error));
                sectionTag.put(METADATA_KEY, section.getMetadataArray().toTag());
                sectionTag.put(SKY_LIGHT_KEY, section.getLightArray(LightType.field_2757).toTag());
                sectionTag.put(BLOCK_LIGHT_KEY, section.getLightArray(LightType.field_2758).toTag());
                listTag.add(sectionTag);
            }
        }
        tag.put(SECTIONS_TAG, listTag);
        StationHeigtmapProvider provider = (StationHeigtmapProvider) chunk;
        tag.put(HEIGHTMAP_KEY, provider.getStoredHeightmap());
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
        ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
        if (chunkTag.containsKey(SECTIONS_TAG)) {
            ListTag listTag = chunkTag.getListTag(SECTIONS_TAG);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag sectionTag = (CompoundTag) listTag.get(i);
                int sectionY = sectionTag.getByte(HEIGHT_KEY);
                int index = ((HeightLimitView) level).sectionCoordToIndex(sectionY);
                if (index < 0 || index >= sections.length) continue;
                PalettedContainer<BlockState> blockStates = sectionTag.containsKey("block_states") ? CODEC.parse(NbtOps.INSTANCE, sectionTag.getCompoundTag("block_states")).promotePartial(errorMessage -> logRecoverableError(var2, var3, sectionY, errorMessage)).getOrThrow(false, LOGGER::error) : new PalettedContainer<>(States.STATE_IDS, States.AIR.get(), PalettedContainer.PaletteProvider.BLOCK_STATE);
                ChunkSection chunkSection = new ChunkSection(sectionY, blockStates);
                chunkSection.getMetadataArray().copyArray(sectionTag.getByteArray(METADATA_KEY));
                chunkSection.getLightArray(LightType.field_2757).copyArray(sectionTag.getByteArray(SKY_LIGHT_KEY));
                chunkSection.getLightArray(LightType.field_2758).copyArray(sectionTag.getByteArray(BLOCK_LIGHT_KEY));
                sections[index] = chunkSection;
            }
        }
        StationHeigtmapProvider provider = (StationHeigtmapProvider) chunk;
        provider.loadStoredHeightmap(chunkTag.getByteArray(HEIGHTMAP_KEY));
    }

    private static void logRecoverableError(int chunkX, int chunkZ, int y, String message) {
        LOGGER.error("Recoverable errors when loading section [" + chunkX + ", " + y + ", " + chunkZ + "]: " + message);
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
