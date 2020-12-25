package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Sponge extends net.minecraft.block.Sponge {
    
    public Sponge(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sponge(int i) {
        super(i);
    }

    @Override
    public Sponge disableNotifyOnMetaDataChange() {
        return (Sponge) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Sponge sounds(BlockSounds sounds) {
        return (Sponge) super.sounds(sounds);
    }

    @Override
    public Sponge setLightOpacity(int i) {
        return (Sponge) super.setLightOpacity(i);
    }

    @Override
    public Sponge setLightEmittance(float f) {
        return (Sponge) super.setLightEmittance(f);
    }

    @Override
    public Sponge setBlastResistance(float resistance) {
        return (Sponge) super.setBlastResistance(resistance);
    }

    @Override
    public Sponge setHardness(float hardness) {
        return (Sponge) super.setHardness(hardness);
    }

    @Override
    public Sponge setUnbreakable() {
        return (Sponge) super.setUnbreakable();
    }

    @Override
    public Sponge setTicksRandomly(boolean ticksRandomly) {
        return (Sponge) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Sponge setName(String string) {
        return (Sponge) super.setName(string);
    }

    @Override
    public Sponge disableStat() {
        return (Sponge) super.disableStat();
    }
}
