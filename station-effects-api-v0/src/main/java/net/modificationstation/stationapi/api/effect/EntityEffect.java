package net.modificationstation.stationapi.api.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class EntityEffect<E extends Entity> {
	private final Identifier effectID;
	protected E entity;
	protected int ticks;
	
	public EntityEffect(Identifier effectID, E entity) {
		this.effectID = effectID;
		this.entity = entity;
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
	
	public final void tick() {
		if (ticks-- <= 0) {
			onEnd();
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
