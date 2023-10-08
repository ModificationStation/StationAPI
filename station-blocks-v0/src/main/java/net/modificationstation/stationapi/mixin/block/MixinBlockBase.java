package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.MiningLevels;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.event.block.MiningLevelRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements StationBlock {

    @Unique
    private int miningLevel;

    @Shadow public abstract BlockBase setTranslationKey(String string);

    @Override
    public BlockBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public BlockBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }

    @Override
    public BlockBase setMiningLevel(Identifier identifier) {
        this.miningLevel = MiningLevels.getMiningLevel(identifier);
        return BlockBase.class.cast(this);
    }

    @Override
    public BlockBase setMiningLevel(String id) {
        this.miningLevel = MiningLevels.getMiningLevel(id);
        return BlockBase.class.cast(this);
    }

    /**
     * Not Recommended to use
     */
    @Unique
    public BlockBase setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
        return BlockBase.class.cast(this);
    }

    @Override
    public int getMiningLevel() {
        return miningLevel;
    }

    @Inject(method = "<clinit>",at = @At("HEAD"))
    private static void registerMiningLevels(CallbackInfo ci){
        StationAPI.EVENT_BUS.post(MiningLevelRegisterEvent.builder().build());
    }
}
