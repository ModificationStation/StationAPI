package net.modificationstation.stationapi.mixin.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.impl.block.BlockBrightness;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.ToIntFunction;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements StationBlock {
    @Shadow public abstract BlockBase setTranslationKey(String string);
    @Shadow @Final public int id;
    
    @Unique private ToIntFunction<BlockState> emittanceProvider = state -> BlockBase.EMITTANCE[state.getBlock().id];
    
    @Override
    public BlockBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public BlockBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
    
    @Override
    public BlockBase setEmittance(ToIntFunction<BlockState> provider) {
        emittanceProvider = provider;
        
        // Need for proper functionality of LevelMixin
        BlockBase.EMITTANCE[id] = 15;
        
        return BlockBase.class.cast(this);
    }
    
    @Override
    public int getEmittance(BlockState state) {
        return emittanceProvider.applyAsInt(state);
    }
    
    @Environment(value= EnvType.CLIENT)
    @ModifyArg(method = "getBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/BlockView;method_1784(IIII)F"), index = 3)
    private int getStateBrightness(int original) {
        return BlockBrightness.light;
    }
}
