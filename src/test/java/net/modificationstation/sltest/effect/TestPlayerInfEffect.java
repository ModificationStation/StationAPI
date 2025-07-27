package net.modificationstation.sltest.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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
    public void onAdded(boolean appliedNow) {}
    
    @Override
    public void onTick() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            entity.world.addParticle("heart", entity.x, entity.y + entity.standingEyeHeight + 1, entity.z, 0, 0.5, 0);
        }
    }
    
    @Override
    public void onRemoved() {}
    
    @Override
    protected void writeNbt(NbtCompound tag) {}
    
    @Override
    protected void readNbt(NbtCompound tag) {}

    @Override
    public EntityEffectType<TestPlayerInfEffect> getType() {
        return TYPE;
    }
}
