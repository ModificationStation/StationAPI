package net.modificationstation.stationapi.impl.level.storage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.modificationstation.stationapi.impl.level.dimension.StationFlatteningDimensionFile;

import java.io.File;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class StationFlatteningWorldStorage extends McRegionLevelStorage {

    public static final String ITEM_NBT = MODID.id("item_nbt").toString();
//    public static final Codec<CompoundTag> COMPOUND_TAG_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
//        AbstractTag nbtElement = dynamic.convert(NbtOps.INSTANCE).getValue();
//        if (nbtElement instanceof CompoundTag tag) {
//            return DataResult.success(tag);
//        }
//        return DataResult.error("Not a compound tag: " + nbtElement);
//    }, nbt -> new Dynamic<>(NbtOps.INSTANCE, nbt));
//    @SuppressWarnings("DataFlowIssue")
//    public static final Codec<ItemInstance> ITEM_STACK_CODEC = RecordCodecBuilder.create(
//            instance -> instance.group(
//                    ItemRegistry.INSTANCE.getCodec().fieldOf(STATION_ID).forGetter(ItemInstance::getType),
//                    Codec.BYTE.fieldOf("Count").forGetter(stack -> (byte) stack.count),
//                    Codec.SHORT.fieldOf("Damage").forGetter(stack -> (short) stack.getDamage()),
//                    COMPOUND_TAG_CODEC.optionalFieldOf(ITEM_NBT).forGetter(stack -> Optional.ofNullable(StationNBT.class.cast(stack).getStationNBT()))
//            ).apply(instance, (item, count, damage, tagOptional) -> {
//                ItemInstance itemStack = new ItemInstance(item, count, damage);
//                tagOptional.ifPresent(tag -> StationNBTSetter.cast(itemStack).setStationNBT(tag));
//                return itemStack;
//            })
//    );

    public StationFlatteningWorldStorage(File file) {
        super(file);
//        DataFixers.init();
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String getLevelFormat() {
        return "Modded " + super.getLevelFormat();
    }

    @Override
    public DimensionData createDimensionFile(String string, boolean bl) {
        return new StationFlatteningDimensionFile(this.path, string, bl);
    }
}
