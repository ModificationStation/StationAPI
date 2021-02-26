package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateLadder extends net.minecraft.block.Ladder implements IBlockTemplate<TemplateLadder> {

    public TemplateLadder(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texUVStart);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateLadder(int id, int texUVStart) {
        super(id, texUVStart);
    }

    @Override
    public TemplateLadder disableNotifyOnMetaDataChange() {
        return (TemplateLadder) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateLadder setSounds(BlockSounds sounds) {
        return (TemplateLadder) super.setSounds(sounds);
    }

    @Override
    public TemplateLadder setLightOpacity(int i) {
        return (TemplateLadder) super.setLightOpacity(i);
    }

    @Override
    public TemplateLadder setLightEmittance(float f) {
        return (TemplateLadder) super.setLightEmittance(f);
    }

    @Override
    public TemplateLadder setBlastResistance(float resistance) {
        return (TemplateLadder) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateLadder setHardness(float hardness) {
        return (TemplateLadder) super.setHardness(hardness);
    }

    @Override
    public TemplateLadder setUnbreakable() {
        return (TemplateLadder) super.setUnbreakable();
    }

    @Override
    public TemplateLadder setTicksRandomly(boolean ticksRandomly) {
        return (TemplateLadder) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateLadder setTranslationKey(String string) {
        return (TemplateLadder) super.setTranslationKey(string);
    }

    @Override
    public TemplateLadder disableStat() {
        return (TemplateLadder) super.disableStat();
    }
}
