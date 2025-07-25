package net.modificationstation.stationapi.impl.effect;

import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;

public interface StationEffectsEntityImpl {
    void stationapi_addEffect(EntityEffect<?> effect, boolean appliedNow);

    void stationapi_removeAllEffects();

    void stationapi_removeEffect(EntityEffectType<?> effectType);
}
