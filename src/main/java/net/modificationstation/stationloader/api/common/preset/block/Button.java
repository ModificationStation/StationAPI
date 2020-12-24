package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Button extends net.minecraft.block.Button {

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
    public Button sounds(BlockSounds sounds) {
        return (Button) super.sounds(sounds);
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
}