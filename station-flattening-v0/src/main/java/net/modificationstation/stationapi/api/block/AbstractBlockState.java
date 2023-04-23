package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.state.State;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractBlockState extends State<BlockBase, BlockState> {
    private final boolean isAir;
    private final Material material;
    private final MaterialColour materialColor;
    private final boolean toolRequired;
    private final boolean opaque;

    protected AbstractBlockState(BlockBase block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
        super(block, propertyMap, mapCodec);
        this.isAir = block.material == Material.AIR;
        this.material = block.material;
        this.materialColor = block.material.materialColour;
        this.toolRequired = !block.material.doesRequireTool();
        this.opaque = block.isFullOpaque();
    }

    public BlockBase getBlock() {
        return this.owner;
    }

    public Material getMaterial() {
        return this.material;
    }

    /**
     * Returns the light level emitted by this block state.
     */
    public int getLuminance() {
        return BlockBase.EMITTANCE[owner.id];
    }

    public boolean isAir() {
        return this.isAir;
    }

    public MaterialColour getTopMaterialColor(BlockView world, TilePos pos) {
        return this.materialColor;
    }

    public float getHardness(BlockView world, TilePos pos) {
        return getBlock().getHardness(asBlockState(), world, pos);
    }

    public float calcBlockBreakingDelta(PlayerBase player, BlockView world, TilePos pos) {
        return getBlock().calcBlockBreakingDelta(asBlockState(), player, world, pos);
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    public boolean canReplace(ItemPlacementContext context) {
        return this.getBlock().canReplace(this.asBlockState(), context);
    }

    public boolean isIn(TagKey<BlockBase> tag) {
        return getBlock().getRegistryEntry().isIn(tag);
    }

    public boolean isIn(TagKey<BlockBase> tag, Predicate<AbstractBlockState> predicate) {
        return this.isIn(tag) && predicate.test(this);
    }

    public boolean isIn(RegistryEntryList<BlockBase> blocks) {
        return blocks.contains(getBlock().getRegistryEntry());
    }

    public Stream<TagKey<BlockBase>> streamTags() {
        return getBlock().getRegistryEntry().streamTags();
    }

    public boolean isOf(BlockBase block) {
        return this.getBlock() == block;
    }

    public boolean hasRandomTicks() {
        return BlockBase.TICKS_RANDOMLY[getBlock().id];
    }

    @Environment(EnvType.CLIENT)
    public long getRenderingSeed(TilePos pos) {
        return MathHelper.hashCode(pos.x, pos.y, pos.z);
    }

    protected abstract BlockState asBlockState();

    public boolean isToolRequired() {
        return this.toolRequired;
    }
}