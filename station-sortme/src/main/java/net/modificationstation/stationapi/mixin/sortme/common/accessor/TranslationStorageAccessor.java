package net.modificationstation.stationapi.mixin.sortme.common.accessor;

import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TranslationStorage.class)
public interface TranslationStorageAccessor {

    @Accessor
    Properties getTranslations();
}
