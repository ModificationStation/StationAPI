package net.modificationstation.sltest.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestPlayerEffect extends EntityEffect {
	private int originalHealth;
	
	public TestPlayerEffect(Identifier id, Entity entity, int ticks) {
		super(id, entity, ticks);
		if (!(entity instanceof PlayerEntity)) {
			throw new RuntimeException("Effect can be applied only on player");
		}
	}
	
	@Override
	public void onAdded() {
		PlayerEntity player = (PlayerEntity) entity;
		originalHealth = player.health;
		player.health = 5;
	}
	
	@Override
	public void onTick() {}
	
	@Override
	public void onRemoved() {
		((PlayerEntity) entity).health = originalHealth;
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
