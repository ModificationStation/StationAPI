package net.modificationstation.stationapi.api.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class EntityEffect<E extends Entity> {
	protected static final int INFINITY_TICKS = -10;
	private final Identifier effectID;
	protected E entity;
	protected int ticks;
	
	@Environment(EnvType.CLIENT)
	private String nameTranslationKey;
	
	@Environment(EnvType.CLIENT)
	private String descriptionTranslationKey;
	
	public EntityEffect(Identifier effectID, E entity) {
		this.effectID = effectID;
		this.entity = entity;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			nameTranslationKey = "gui.stationapi.effect." + effectID.namespace + "." + effectID.path + ".name";
			descriptionTranslationKey = nameTranslationKey.substring(0, nameTranslationKey.length() - 4) + "desc";
		}
	}
	
	public abstract void onStart();
	
	public abstract void process();
	
	public abstract void onEnd();
	
	protected abstract void writeCustomData(NbtCompound tag);
	
	protected abstract void readCustomData(NbtCompound tag);
	
	public Identifier getEffectID() {
		return effectID;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public boolean isInfinity() {
		return ticks == INFINITY_TICKS;
	}
	
	@Environment(EnvType.CLIENT)
	public String getName() {
		return I18n.getTranslation(nameTranslationKey, nameTranslationKey);
	}
	
	@Environment(EnvType.CLIENT)
	public String getDescription() {
		return I18n.getTranslation(descriptionTranslationKey, descriptionTranslationKey);
	}
	
	@Environment(EnvType.CLIENT)
	public final void setTicks(int ticks) {
		this.ticks = ticks;
	}
	
	public final void tick() {
		if (!isInfinity() && ticks-- <= 0) {
			entity.removeEffect(effectID);
			return;
		}
		process();
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
