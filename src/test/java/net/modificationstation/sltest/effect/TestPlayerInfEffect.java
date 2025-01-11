package net.modificationstation.sltest.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestPlayerInfEffect extends EntityEffect {
	public TestPlayerInfEffect(Identifier id, Entity entity, int ticks) {
		super(id, entity, ticks);
	}
	
	@Override
	public void onAdded() {}
	
	@Override
	public void onTick() {}
	
	@Override
	public void onRemoved() {}
	
	@Override
	protected void writeCustomData(NbtCompound tag) {}
	
	@Override
	protected void readCustomData(NbtCompound tag) {}
}
