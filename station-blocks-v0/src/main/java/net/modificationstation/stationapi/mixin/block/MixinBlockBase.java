package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements StationBlock {

    @Shadow @Final public int id;

    @Shadow public abstract BlockBase setTranslationKey(String string);

    @Unique
    private final RegistryEntry.Reference<BlockBase> stationapi_registryEntry = BlockRegistry.INSTANCE.createEntry(BlockBase.class.cast(this));

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String getTranslationKey(String name) {
        return StationAPI.EVENT_BUS.post(
                BlockEvent.TranslationKeyChanged.builder()
                        .block(BlockBase.class.cast(this))
                        .currentTranslationKey(name)
                        .build()
        ).currentTranslationKey;
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void afterBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockRegistryEvent());
    }

    @Override
    @Unique
    public int getLegacyID() {
        return id;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 256),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;",
                            opcode = Opcodes.PUTSTATIC,
                            shift = At.Shift.AFTER
                    )
            )
    )
    private static int getBlocksSize(int constant) {
        return BlockBase.BY_ID.length;
    }

    @Inject(
            method = "beforeDestroyedByExplosion(Lnet/minecraft/level/Level;IIIIF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/Level;rand:Ljava/util/Random;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void beforeDrop(Level level, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeDrop.builder()
                        .level(level)
                        .x(x).y(y).z(z)
                        .chance(chance)
                        .block(BlockBase.class.cast(this))
                        .build()
                ).isCanceled()
        ) ci.cancel();
    }

    @Override
    @Unique
    public RegistryEntry.Reference<BlockBase> getRegistryEntry() {
        return stationapi_registryEntry;
    }

    @Override
    public BlockBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public BlockBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
}
