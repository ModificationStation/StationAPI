package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements StationBlock {

    @Shadow public abstract BlockBase setTranslationKey(String string);

    @Override
    public BlockBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public BlockBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
}
