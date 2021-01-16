package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Workbench extends net.minecraft.block.Workbench {
    
    public Workbench(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Workbench(int i) {
        super(i);
    }

    @Override
    public Workbench disableNotifyOnMetaDataChange() {
        return (Workbench) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Workbench sounds(BlockSounds sounds) {
        return (Workbench) super.sounds(sounds);
    }

    @Override
    public Workbench setLightOpacity(int i) {
        return (Workbench) super.setLightOpacity(i);
    }

    @Override
    public Workbench setLightEmittance(float f) {
        return (Workbench) super.setLightEmittance(f);
    }

    @Override
    public Workbench setBlastResistance(float resistance) {
        return (Workbench) super.setBlastResistance(resistance);
    }

    @Override
    public Workbench setHardness(float hardness) {
        return (Workbench) super.setHardness(hardness);
    }

    @Override
    public Workbench setUnbreakable() {
        return (Workbench) super.setUnbreakable();
    }

    @Override
    public Workbench setTicksRandomly(boolean ticksRandomly) {
        return (Workbench) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Workbench setName(String string) {
        return (Workbench) super.setName(string);
    }

    @Override
    public Workbench disableStat() {
        return (Workbench) super.disableStat();
    }
}
