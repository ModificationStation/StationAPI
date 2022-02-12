package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.level.LevelManager;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
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
    private static void saveBlockStates(Chunk chunk, Level level, CompoundTag tag, CallbackInfo ci) {
        ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
        ListTag listTag = new ListTag();
        for(int i = 0; i < sections.length; ++i) {
            ChunkSection section = sections[i];
            if (section != ChunkSection.EMPTY_SECTION) {
                CompoundTag compoundTag7 = new CompoundTag();
                compoundTag7.put("Y", (byte) (i & 255));
                section.getContainer().write(compoundTag7, "Palette", "BlockStates");
                listTag.add(compoundTag7);
            }
        }
        tag.put(SECTIONS_TAG, listTag);
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
    private static void loadBlockState(Level arg, CompoundTag arg1, CallbackInfoReturnable<Chunk> cir, int var2, int var3, Chunk var4) {
        ChunkSection[] sections = ((ChunkSectionsAccessor) var4).getSections();
        if (arg1.containsKey(SECTIONS_TAG)) {
            ListTag listTag = arg1.getListTag(SECTIONS_TAG);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag section = (CompoundTag) listTag.get(i);
                int k = section.getByte("Y");
                if (section.containsKey("Palette") && section.containsKey("BlockStates")) {
                    ChunkSection chunkSection = new ChunkSection(k << 4);
                    byte[] bytes = section.getByteArray("BlockStates");
                    long[] longs = new long[bytes.length / 8];
                    for (int j = 0; j < longs.length; j++) {
                        int index = j * 8;
                        longs[j] =
                                ((long) Byte.toUnsignedInt(bytes[index]) << 56L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 1])) << 48L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 2])) << 40L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 3])) << 32L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 4])) << 24L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 5])) << 16L)
                                | (((long) Byte.toUnsignedInt(bytes[index + 6])) << 8L)
                                | ((long) Byte.toUnsignedInt(bytes[index + 7]));
                    }
                    chunkSection.getContainer().read(section.getListTag("Palette"), longs);
                    chunkSection.calculateCounts();
                    if (!chunkSection.isEmpty()) {
                        sections[k] = chunkSection;
                    }

//                    poiStorage.initForPalette(pos, chunkSection);
                }
            }
        }
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
}
