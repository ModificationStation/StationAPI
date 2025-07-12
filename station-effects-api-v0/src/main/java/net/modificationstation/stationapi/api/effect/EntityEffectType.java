package net.modificationstation.stationapi.api.effect;

import net.modificationstation.stationapi.api.registry.RegistryEntry;

public final class EntityEffectType<EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> {
    @SuppressWarnings("unchecked")
    public final RegistryEntry.Reference<EntityEffectType<EFFECT_INSTANCE>> registryEntry =
            (RegistryEntry.Reference<EntityEffectType<EFFECT_INSTANCE>>) (RegistryEntry.Reference<?>)
                    EntityEffectTypeRegistry.INSTANCE.createEntry(this);
    public final EntityEffect.Factory<EFFECT_INSTANCE> factory;

    private EntityEffectType(Builder<EFFECT_INSTANCE> builder) {
        factory = builder.factory;
    }

    public static <EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> Builder<EFFECT_INSTANCE> builder(
            EntityEffect.Factory<EFFECT_INSTANCE> factory
    ) {
        return new Builder<>(factory);
    }

    public static final class Builder<EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> {
        private final EntityEffect.Factory<EFFECT_INSTANCE> factory;

        private Builder(EntityEffect.Factory<EFFECT_INSTANCE> factory) {
            this.factory = factory;
        }

        public EntityEffectType<EFFECT_INSTANCE> build() {
            return new EntityEffectType<>(this);
        }
    }
}
