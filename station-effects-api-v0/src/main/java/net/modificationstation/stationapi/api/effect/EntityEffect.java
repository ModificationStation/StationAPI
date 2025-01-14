package net.modificationstation.stationapi.api.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class EntityEffect {
    protected static final int INFINITY_TICKS = -1;
    private final Identifier effectID;
    protected Entity entity;
    private int ticks;
    
    private final String nameTranslationKey;
    private final String descriptionTranslationKey;
    
    public EntityEffect(Identifier effectID, Entity entity, int ticks) {
        this.effectID = effectID;
        this.entity = entity;
        this.ticks = ticks;
        nameTranslationKey = "gui.stationapi.effect." + effectID.namespace + "." + effectID.path + ".name";
        descriptionTranslationKey = nameTranslationKey.substring(0, nameTranslationKey.length() - 4) + "desc";
    }
    
    /**
     * This method is called immediately when the effect is added.
     */
    public abstract void onAdded();
    
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
    
    public final Identifier getEffectID() {
        return effectID;
    }
    
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
                entity.removeEffect(effectID);
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
}
