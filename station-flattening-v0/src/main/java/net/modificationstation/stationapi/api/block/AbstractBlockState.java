package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.context.BlockTagContext;
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
    public final Block block;
    private final boolean isAir;
    private final Material material;
    private final MapColor materialColor;
    private final boolean toolRequired;
    private final boolean opaque;
    private int luminance = -1;

    protected AbstractBlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
        super(block, propertyMap, mapCodec);
        this.block = block;
        this.isAir = block.material == Material.AIR;
        this.material = block.material;
        this.materialColor = block.material.mapColor;
        this.toolRequired = !block.material.isHandHarvestable();
        this.opaque = block.isOpaque();
    }

    public Block getBlock() {
        return this.block;
    }

    public Material getMaterial() {
        return this.material;
    }

    /**
     * Returns the light level emitted by this block state.
     */
    public int getLuminance() {
        return luminance == -1
                ? luminance = ((StationFlatteningBlockInternal) block).stationapi_getLuminanceProvider()
                        .applyAsInt(asBlockState())
                : luminance;
    }

    public boolean isAir() {
        return this.isAir;
    }

    public MapColor getTopMaterialColor(BlockView world, BlockPos pos) {
        return this.materialColor;
    }

    public float getHardness(BlockView world, BlockPos pos) {
        return block.getHardness(asBlockState(), world, pos);
    }

    public float calcBlockBreakingDelta(PlayerEntity player, BlockView world, BlockPos pos) {
        return block.calcBlockBreakingDelta(asBlockState(), player, world, pos);
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    public void onStateReplaced(World world, BlockPos pos, BlockState state) {
        this.block.onStateReplaced(this.asBlockState(), world, pos, state);
    }

    public boolean canReplace(ItemPlacementContext context) {
        return this.block.canReplace(this.asBlockState(), context);
    }

    /**
     * @deprecated Use {@link #isIn(TagKey, BlockTagContext, Predicate)} instead.
     * Relying on tag checks without a {@link BlockTagContext} can lead to broken behavior if the tag contains conditions
     * that require contextual information (such as the block's world position, metadata, or the actor interacting with it).
     */
    @Deprecated
    public boolean isIn(TagKey<Block> tag, Predicate<AbstractBlockState> predicate) {
        return isIn(tag) && predicate.test(this);
    }

    /**
     * @deprecated Use {@link #isIn(TagKey, BlockTagContext)} instead.
     * Relying on tag checks without a {@link BlockTagContext} can lead to broken behavior if the tag contains conditions
     * that require contextual information (such as the block's world position, metadata, or the actor interacting with it).
     */
    @Deprecated
    public boolean isIn(TagKey<Block> tag) {
        return isIn(tag, BlockTagContext.DEFAULT);
    }

    public boolean isIn(TagKey<Block> tag, BlockTagContext context, Predicate<AbstractBlockState> predicate) {
        return isIn(tag, context) && predicate.test(this);
    }

    public boolean isIn(TagKey<Block> tag, BlockTagContext context) {
        return block.getRegistryEntry().isIn(tag, context);
    }

    public boolean isIn (TagKey<Block> tag, World world, int x, int y, int z) {
        return isIn(tag, BlockTagContext.of(world, x, y, z));
    }

    public boolean isIn(TagKey<Block> tag, World world, int x, int y, int z, Predicate<AbstractBlockState> predicate) {
        return isIn(tag, BlockTagContext.of(world, x, y, z)) && predicate.test(this);
    }

    /**
     * @deprecated Use {@link #isIn(RegistryEntryList, BlockTagContext)} instead.
     * Relying on list checks without a {@link BlockTagContext} can lead to broken behavior if the underlying entries
     * have contextual conditions.
     */
    @Deprecated
    public boolean isIn(RegistryEntryList<Block> blocks) {
        return isIn(blocks, BlockTagContext.DEFAULT);
    }

    public boolean isIn(RegistryEntryList<Block> blocks, BlockTagContext context) {
        return blocks.contains(block.getRegistryEntry(), context);
    }

    public Stream<TagKey<Block>> streamTags() {
        return streamTags(BlockTagContext.BYPASSED);
    }

    public Stream<TagKey<Block>> streamTags(BlockTagContext context) {
        return block.getRegistryEntry().streamTags(context);
    }

    public boolean isOf(Block block) {
        return this.block == block;
    }

    public boolean hasRandomTicks() {
        return Block.BLOCKS_RANDOM_TICK[block.id];
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