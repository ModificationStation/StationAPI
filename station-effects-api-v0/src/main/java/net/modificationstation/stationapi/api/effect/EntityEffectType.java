package net.modificationstation.stationapi.api.effect;

import net.modificationstation.stationapi.api.registry.RegistryEntry;

/**
 * Static reference of an effect. Since {@link EntityEffect} is initialized per-entity, a separate class is required
 * for cases when an effect needs to be referenced in an entity-less context, such as
 * {@link net.minecraft.entity.Entity#addEffect(EntityEffectType, int)}
 *
 * @param <EFFECT_INSTANCE> the class of the effect's per-entity instance.
 */
public final class EntityEffectType<EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> {
    @SuppressWarnings("unchecked")
    public final RegistryEntry.Reference<EntityEffectType<EFFECT_INSTANCE>> registryEntry =
            (RegistryEntry.Reference<EntityEffectType<EFFECT_INSTANCE>>) (RegistryEntry.Reference<?>)
                    EntityEffectTypeRegistry.INSTANCE.createEntry(this);
    /**
     * The effect instance's factory which takes an {@link net.minecraft.entity.Entity} argument of the entity
     * the effect's been applied to, and an int argument defining the duration of the effect in ticks.
     */
    public final EntityEffect.Factory<EFFECT_INSTANCE> factory;

    private EntityEffectType(Builder<EFFECT_INSTANCE> builder) {
        factory = builder.factory;
    }

    /**
     * @param factory the effect instance's factory which takes an {@link net.minecraft.entity.Entity} argument
     *                of the entity the effect's been applied to, and an int argument defining the duration
     *                of the effect in ticks.
     * @return an effect type's builder instance.
     * @param <EFFECT_INSTANCE> the class of the effect's per-entity instance.
     */
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

        /**
         * @return the built entity type.
         */
        public EntityEffectType<EFFECT_INSTANCE> build() {
            return new EntityEffectType<>(this);
        }
    }
}
