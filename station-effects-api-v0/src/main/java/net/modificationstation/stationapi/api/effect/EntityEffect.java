package net.modificationstation.stationapi.api.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
        var effectId =  getType().registryEntry.registryKey().getValue();
        nameTranslationKey = "gui.stationapi.effect." + effectId.namespace + "." + effectId.path + ".name";
        descriptionTranslationKey = nameTranslationKey.substring(0, nameTranslationKey.length() - 4) + "desc";
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
     * @param tag effect data root tag
     */
    protected abstract void writeCustomData(NbtCompound tag);
    
    /**
     * Allows to read any custom data from the tag storage.
     * @param tag effect data root tag
     */
    protected abstract void readCustomData(NbtCompound tag);

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
     * Get translated effect name, client side only.
     */
    @Environment(EnvType.CLIENT)
    public final String getName() {
        return I18n.getTranslation(nameTranslationKey, nameTranslationKey);
    }
    
    /**
     * Get translated effect description, client side only.
     */
    @Environment(EnvType.CLIENT)
    public final String getDescription() {
        return I18n.getTranslation(descriptionTranslationKey, descriptionTranslationKey);
    }
    
    /**
     * Set how much ticks effect will stay. Used only by packets.
     */
    @Environment(EnvType.CLIENT)
    public final void setTicks(int ticks) {
        this.ticks = ticks;
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
    
    public final NbtCompound write() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("ticks", ticks);
        writeCustomData(tag);
        return tag;
    }
    
    public final void read(NbtCompound tag) {
        ticks = tag.getInt("ticks");
        readCustomData(tag);
    }

    @FunctionalInterface
    public interface Factory<EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> {
        EFFECT_INSTANCE create(Entity entity, int ticks);
    }
}
