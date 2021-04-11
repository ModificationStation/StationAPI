package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

public interface IBlockTemplate<T extends BlockBase> {

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((BlockBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }
}
