package net.modificationstation.sltest.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;

public class TestPlayerEffect extends EntityEffect<TestPlayerEffect> {
    public static final EntityEffectType<TestPlayerEffect> TYPE = EntityEffectType
            .builder(TestPlayerEffect::new).build();

    private int originalHealth;
    
    public TestPlayerEffect(Entity entity, int ticks) {
        super(entity, ticks);
        if (!(entity instanceof PlayerEntity)) {
            throw new RuntimeException("Effect can be applied only on player");
        }
    }
    
    @Override
    public void onAdded(boolean appliedNow) {
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
    protected void writeNbt(NbtCompound tag) {
        tag.putInt("original_health", originalHealth);
    }
    
    @Override
    protected void readNbt(NbtCompound tag) {
        originalHealth = tag.getInt("original_health");
    }

    @Override
    public EntityEffectType<TestPlayerEffect> getType() {
        return TYPE;
    }
}
