package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.class_259;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.state.State;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.block.StationFlatteningBlockInternal;

import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractBlockState extends State<Block, BlockState> {
    private final boolean isAir;
    private final Material material;
    private final class_259 materialColor;
    private final boolean toolRequired;
    private final boolean opaque;
    private int luminance = -1;

    protected AbstractBlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
        super(block, propertyMap, mapCodec);
        this.isAir = block.material == Material.AIR;
        this.material = block.material;
        this.materialColor = block.material.field_973;
        this.toolRequired = !block.material.method_898();
        this.opaque = block.isOpaque();
    }

    public Block getBlock() {
        return this.owner;
    }

    public Material getMaterial() {
        return this.material;
    }

    /**
     * Returns the light level emitted by this block state.
     */
    public int getLuminance() {
        return luminance == -1 ?
                luminance = ((StationFlatteningBlockInternal) owner).stationapi_getLuminanceProvider().applyAsInt(asBlockState()) :
                luminance;
    }

    public boolean isAir() {
        return this.isAir;
    }

    public class_259 getTopMaterialColor(BlockView world, BlockPos pos) {
        return this.materialColor;
    }

    public float getHardness(BlockView world, BlockPos pos) {
        return getBlock().getHardness(asBlockState(), world, pos);
    }

    public float calcBlockBreakingDelta(PlayerEntity player, BlockView world, BlockPos pos) {
        return getBlock().calcBlockBreakingDelta(asBlockState(), player, world, pos);
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    public boolean canReplace(ItemPlacementContext context) {
        return this.getBlock().canReplace(this.asBlockState(), context);
    }

    public boolean isIn(TagKey<Block> tag) {
        return getBlock().getRegistryEntry().isIn(tag);
    }

    public boolean isIn(TagKey<Block> tag, Predicate<AbstractBlockState> predicate) {
        return this.isIn(tag) && predicate.test(this);
    }

    public boolean isIn(RegistryEntryList<Block> blocks) {
        return blocks.contains(getBlock().getRegistryEntry());
    }

    public Stream<TagKey<Block>> streamTags() {
        return getBlock().getRegistryEntry().streamTags();
    }

    public boolean isOf(Block block) {
        return this.getBlock() == block;
    }

    public boolean hasRandomTicks() {
        return Block.BLOCKS_RANDOM_TICK[getBlock().id];
    }

    @Environment(EnvType.CLIENT)
    public long getRenderingSeed(BlockPos pos) {
        return MathHelper.hashCode(pos.x, pos.y, pos.z);
    }

    protected abstract BlockState asBlockState();

    public boolean isToolRequired() {
        return this.toolRequired;
    }
}