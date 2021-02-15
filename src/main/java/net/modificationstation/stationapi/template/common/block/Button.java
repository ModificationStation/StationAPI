package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Button extends net.minecraft.block.Button implements IBlockTemplate<Button> {

    public Button(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Button(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Button disableNotifyOnMetaDataChange() {
        return (Button) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Button setSounds(BlockSounds sounds) {
        return (Button) super.setSounds(sounds);
    }

    @Override
    public Button setLightOpacity(int i) {
        return (Button) super.setLightOpacity(i);
    }

    @Override
    public Button setLightEmittance(float f) {
        return (Button) super.setLightEmittance(f);
    }

    @Override
    public Button setBlastResistance(float resistance) {
        return (Button) super.setBlastResistance(resistance);
    }

    @Override
    public Button setHardness(float hardness) {
        return (Button) super.setHardness(hardness);
    }

    @Override
    public Button setUnbreakable() {
        return (Button) super.setUnbreakable();
    }

    @Override
    public Button setTicksRandomly(boolean ticksRandomly) {
        return (Button) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Button setTranslationKey(String string) {
        return (Button) super.setTranslationKey(string);
    }

    @Override
    public Button disableStat() {
        return (Button) super.disableStat();
    }
}
