package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.StationDimension;
import net.modificationstation.stationapi.impl.world.StationWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Dimension.class)
class DimensionMixin implements StationDimension {
    @Unique private static final String HEIGHT_KEY = "LevelHeight";
    @Unique private static final String BOTTOM_Y_KEY = "BottomY";
    @Unique private short height = 128;
    @Unique private short bottomY = 0;
    @Shadow public int id;

    @Shadow public World field_2173;

    @Inject(
            method = "method_1768",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/Dimension;method_1765()V",
                    shift = Shift.AFTER
            )
    )
    private void stationapi_onDimensionInit(World world, CallbackInfo info) {
        StationWorldProperties properties = (StationWorldProperties) world.method_262();
        Optional<Identifier> optional = DimensionRegistry.INSTANCE.getIdByLegacyId(this.id);
        if (optional.isPresent()) {
            Identifier id = optional.get();
            NbtCompound tag = properties.getDimensionTag(id);
            loadFromNBT(tag);
            saveToNBT(tag);
        }
        else {
            System.out.println("No key!");
        }
    }

    @Unique
    @Override
    public short getDefaultWorldHeight() {
        return 128;
    }

    @Unique
    @Override
    public short getActualWorldHeight() {
        return height;
    }

    @Unique
    @Override
    public short getActualBottomY() {
        return bottomY;
    }

    @Unique
    @Override
    @Deprecated
    public short getSectionCount() {
        return (short) field_2173.countVerticalSections();
    }

    @Unique
    public void loadFromNBT(NbtCompound tag) {
        height = tag.contains(HEIGHT_KEY) ? tag.getShort(HEIGHT_KEY) : getDefaultWorldHeight();
        bottomY = tag.contains(BOTTOM_Y_KEY) ? tag.getShort(BOTTOM_Y_KEY) : getDefaultBottomY();

        if (height <= 0) {
            height = 16;
        }
        if ((height & 15) != 0) {
            height = (short) (1 << (int) Math.ceil(Math.log(height) / Math.log(2)));
        }
    }

    @Unique
    public void saveToNBT(NbtCompound tag) {
        tag.putShort(HEIGHT_KEY, getActualWorldHeight());
        tag.putShort(BOTTOM_Y_KEY, getActualBottomY());
    }
}
