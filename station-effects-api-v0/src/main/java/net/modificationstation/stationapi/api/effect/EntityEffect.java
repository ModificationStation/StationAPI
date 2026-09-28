package net.modificationstation.stationapi.api.effect;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public abstract class EntityEffect<THIS extends EntityEffect<THIS>> {
    public static final int INFINITY_TICKS = -1;
    protected Entity entity;
    private int ticks;

    private final String nameTranslationKey;
    private final String descriptionTranslationKey;

    protected EntityEffect(Entity entity, int ticks) {
        this.entity = entity;
        this.ticks = ticks;
        this.nameTranslationKey = getType().getTranslationKey();
        this.descriptionTranslationKey = getType().getDescriptionTranslationKey();
    }
    
    /**
     * This method is called immediately when the effect is added.
     *
     * @param appliedNow whether the effect was just inflicted on the entity
     *                   or synchronized from the server later.
     */
    public abstract void onAdded(boolean appliedNow);

    /**
     * This method is called on each entity tick.
     */
    public abstract void onTick();

    /**
     * This method is called immediately when the effect is removed.
     */
    public abstract void onRemoved();

    /**
     * Allows to write any custom data to the tag storage.
     *
     * @param tag effect data root tag
     */
    protected abstract void writeNbt(NbtCompound tag);

    /**
     * Allows to read any custom data from the tag storage.
     *
     * @param tag effect data root tag
     */
    protected abstract void readNbt(NbtCompound tag);

    public abstract EntityEffectType<THIS> getType();

    /**
     * Get remaining effect ticks.
     */
    public final int getTicks() {
        return ticks;
    }

    /**
     * Check if effect is infinite.
     */
    public final boolean isInfinite() {
        return ticks == INFINITY_TICKS;
    }

    /**
     * Get the translation key for the name of the effect.
     */
    public final String getTranslationKey() {
        return this.nameTranslationKey;
    }

    /**
     * Get the translation key for the description of the effect.
     */
    public final String getDescriptionTranslationKey() {
        return this.descriptionTranslationKey;
    }

    /**
     * Get translated effect name.
     */
    public final String getTranslatedName() {
        return I18n.getTranslation(nameTranslationKey, nameTranslationKey);
    }

    /**
     * Get translated effect description.
     */
    public final String getTranslatedDescription() {
        return I18n.getTranslation(descriptionTranslationKey, descriptionTranslationKey);
    }

    public final void tick() {
        onTick();
        if (!isInfinite()) {
            ticks--;
            if (ticks == 0) {
                entity.removeEffect(getType());
            }
        }
    }

    public final void write(NbtCompound nbt) {
        nbt.putInt("ticks", ticks);
        writeNbt(nbt);
    }

    public final void read(NbtCompound nbt) {
        ticks = nbt.getInt("ticks");
        readNbt(nbt);
    }

    @FunctionalInterface
    public interface Factory<EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> {
        EFFECT_INSTANCE create(Entity entity, int ticks);
    }
}
