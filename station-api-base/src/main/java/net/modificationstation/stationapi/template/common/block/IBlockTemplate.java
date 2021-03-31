package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface IBlockTemplate<T extends BlockBase> {

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((BlockBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }
}
