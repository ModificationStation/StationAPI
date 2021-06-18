package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFire extends net.minecraft.block.Fire implements IBlockTemplate<TemplateFire> {

    public TemplateFire(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), texture);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateFire(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateFire disableNotifyOnMetaDataChange() {
        return (TemplateFire) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateFire setSounds(BlockSounds sounds) {
        return (TemplateFire) super.setSounds(sounds);
    }

    @Override
    public TemplateFire setLightOpacity(int i) {
        return (TemplateFire) super.setLightOpacity(i);
    }

    @Override
    public TemplateFire setLightEmittance(float f) {
        return (TemplateFire) super.setLightEmittance(f);
    }

    @Override
    public TemplateFire setBlastResistance(float resistance) {
        return (TemplateFire) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateFire setHardness(float hardness) {
        return (TemplateFire) super.setHardness(hardness);
    }

    @Override
    public TemplateFire setUnbreakable() {
        return (TemplateFire) super.setUnbreakable();
    }

    @Override
    public TemplateFire setTicksRandomly(boolean ticksRandomly) {
        return (TemplateFire) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateFire setTranslationKey(String string) {
        return (TemplateFire) super.setTranslationKey(string);
    }

    @Override
    public TemplateFire disableStat() {
        return (TemplateFire) super.disableStat();
    }
}
