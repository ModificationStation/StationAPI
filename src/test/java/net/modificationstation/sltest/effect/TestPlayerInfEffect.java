package net.modificationstation.sltest.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;

public class TestPlayerInfEffect extends EntityEffect<TestPlayerInfEffect> {
    public static final EntityEffectType<TestPlayerInfEffect> TYPE = EntityEffectType
            .builder(TestPlayerInfEffect::new).build();

    public TestPlayerInfEffect(Entity entity, int ticks) {
        super(entity, ticks);
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

    @Override
    public EntityEffectType<TestPlayerInfEffect> getType() {
        return TYPE;
    }
}
