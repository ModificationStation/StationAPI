package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFarmland extends net.minecraft.block.Farmland implements IBlockTemplate<TemplateFarmland> {

    public TemplateFarmland(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateFarmland(int id) {
        super(id);
    }

    @Override
    public TemplateFarmland disableNotifyOnMetaDataChange() {
        return (TemplateFarmland) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateFarmland setSounds(BlockSounds sounds) {
        return (TemplateFarmland) super.setSounds(sounds);
    }

    @Override
    public TemplateFarmland setLightOpacity(int i) {
        return (TemplateFarmland) super.setLightOpacity(i);
    }

    @Override
    public TemplateFarmland setLightEmittance(float f) {
        return (TemplateFarmland) super.setLightEmittance(f);
    }

    @Override
    public TemplateFarmland setBlastResistance(float resistance) {
        return (TemplateFarmland) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateFarmland setHardness(float hardness) {
        return (TemplateFarmland) super.setHardness(hardness);
    }

    @Override
    public TemplateFarmland setUnbreakable() {
        return (TemplateFarmland) super.setUnbreakable();
    }

    @Override
    public TemplateFarmland setTicksRandomly(boolean ticksRandomly) {
        return (TemplateFarmland) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateFarmland setTranslationKey(String string) {
        return (TemplateFarmland) super.setTranslationKey(string);
    }

    @Override
    public TemplateFarmland disableStat() {
        return (TemplateFarmland) super.disableStat();
    }
}
