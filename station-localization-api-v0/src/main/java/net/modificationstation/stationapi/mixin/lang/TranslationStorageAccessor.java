package net.modificationstation.stationapi.mixin.lang;

import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.*;

@Mixin(TranslationStorage.class)
public interface TranslationStorageAccessor {

    @Accessor
    Properties getTranslations();
}
