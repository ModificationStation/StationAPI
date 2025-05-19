package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class TemplateDoorBlock extends DoorBlock implements BlockTemplate {
    public int topTextureId = 0;
    public int bottomTextureId = 0;

    public Item doorItem = null;

    public TemplateDoorBlock(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDoorBlock(int i, Material arg) {
        super(i, arg);
    }

    public boolean isTop(int meta) {
        return (meta & 8) != 0;
    }

    public int getTexture(int side, int meta) {
        if (side == 0 || side == 1) {
            return bottomTextureId;
        }

        if (!isTop(meta)) {
            return bottomTextureId;
        }

        int var3 = this.method_839(meta);
        if ((var3 == 0 || var3 == 2) ^ side <= 3) {
            return this.bottomTextureId;
        } else {
            return this.topTextureId;
        }
    }

    @Override
    public int getDroppedItemId(int blockMeta, Random random) {
        if (isTop(blockMeta)) {
            return 0;
        } else {
            return doorItem.id;
        }
    }
}
