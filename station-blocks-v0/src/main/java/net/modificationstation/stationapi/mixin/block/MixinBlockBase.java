package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class MixinBlockBase implements StationBlock {

    @Shadow public abstract Block setTranslationKey(String string);

    @Override
    public Block setTranslationKey(Namespace namespace, String translationKey) {
        return setTranslationKey(Identifier.of(namespace, translationKey).toString());
    }

    @Override
    public Block setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
}
