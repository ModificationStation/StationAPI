package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Bookshelf extends net.minecraft.block.Bookshelf {

    public Bookshelf(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Bookshelf(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Bookshelf disableNotifyOnMetaDataChange() {
        return (Bookshelf) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Bookshelf sounds(BlockSounds sounds) {
        return (Bookshelf) super.sounds(sounds);
    }

    @Override
    public Bookshelf setLightOpacity(int i) {
        return (Bookshelf) super.setLightOpacity(i);
    }

    @Override
    public Bookshelf setLightEmittance(float f) {
        return (Bookshelf) super.setLightEmittance(f);
    }

    @Override
    public Bookshelf setBlastResistance(float resistance) {
        return (Bookshelf) super.setBlastResistance(resistance);
    }

    @Override
    public Bookshelf setHardness(float hardness) {
        return (Bookshelf) super.setHardness(hardness);
    }

    @Override
    public Bookshelf setUnbreakable() {
        return (Bookshelf) super.setUnbreakable();
    }
}
