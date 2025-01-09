package net.modificationstation.sltest.effect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestPlayerEffect extends EntityEffect<PlayerEntity> {
	private int originalHealth;
	
	public TestPlayerEffect(Identifier id, PlayerEntity entity) {
		super(id, entity);
		ticks = 100;
	}
	
	@Override
	public void onAdded() {
		originalHealth = entity.health;
		entity.health = 5;
	}
	
	@Override
	public void onTick() {}
	
	@Override
	public void onRemoved() {
		entity.health = originalHealth;
	}
	
	@Override
	protected void writeCustomData(NbtCompound tag) {
		tag.putInt("original_health", originalHealth);
	}
	
	@Override
	protected void readCustomData(NbtCompound tag) {
		originalHealth = tag.getInt("original_health");
	}
}
