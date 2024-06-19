package net.modificationstation.stationapi.api.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class EntityEffect<E extends Entity> {
	protected static final int INFINITY_TICKS = -10;
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
	
	public boolean isInfinity() {
		return ticks == INFINITY_TICKS;
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
