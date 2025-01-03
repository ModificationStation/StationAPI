package net.modificationstation.sltest.effect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestPlayerInfEffect extends EntityEffect<PlayerEntity> {
	public TestPlayerInfEffect(Identifier id, PlayerEntity entity) {
		super(id, entity);
		ticks = INFINITY_TICKS;
	}
	
	@Override
	public void onStart() {}
	
	@Override
	public void process() {}
	
	@Override
	public void onEnd() {}
	
	@Override
	protected void writeCustomData(NbtCompound tag) {}
	
	@Override
	protected void readCustomData(NbtCompound tag) {}
}
