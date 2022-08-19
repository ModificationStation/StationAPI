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
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.state.State;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractBlockState extends State<BlockBase, BlockState> {
    private final int luminance;
    //      private final boolean hasSidedTransparency;
    private final boolean isAir;
    private final Material material;
    private final MaterialColour materialColor;
    private final float hardness;
    private final boolean toolRequired;
    private final boolean opaque;
//      private final AbstractBlock.ContextPredicate solidBlockPredicate;
//      private final AbstractBlock.ContextPredicate suffocationPredicate;
//      private final AbstractBlock.ContextPredicate blockVisionPredicate;
//      private final AbstractBlock.ContextPredicate postProcessPredicate;
//      private final AbstractBlock.ContextPredicate emissiveLightingPredicate;
//      @Nullable
//      protected AbstractBlock.AbstractBlockState.ShapeCache shapeCache;

    protected AbstractBlockState(BlockBase block, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> mapCodec) {
        super(block, propertyMap, mapCodec);
        this.luminance = BlockBase.EMITTANCE[block.id];
//         this.hasSidedTransparency = block.hasSidedTransparency(this.asBlockState());
        this.isAir = block.material == Material.AIR;
        this.material = block.material;
        this.materialColor = block.material.materialColour;
        this.hardness = block.getHardness();
        this.toolRequired = !block.material.doesRequireTool();
        this.opaque = block.isFullOpaque();
//         this.solidBlockPredicate = settings.solidBlockPredicate;
//         this.suffocationPredicate = settings.suffocationPredicate;
//         this.blockVisionPredicate = settings.blockVisionPredicate;
//         this.postProcessPredicate = settings.postProcessPredicate;
//         this.emissiveLightingPredicate = settings.emissiveLightingPredicate;
    }

//      public void initShapeCache() {
//         if (!this.getBlock().hasDynamicBounds()) {
//            this.shapeCache = new AbstractBlock.AbstractBlockState.ShapeCache(this.asBlockState());
//         }
//
//      }

    public BlockBase getBlock() {
        return this.owner;
    }

    public Material getMaterial() {
        return this.material;
    }

//      public boolean allowsSpawning(BlockView world, BlockPos pos, EntityType<?> type) {
//         return this.getBlock().settings.allowsSpawningPredicate.test(this.asBlockState(), world, pos, type);
//      }
//
//      public boolean isTranslucent(BlockView world, BlockPos pos) {
//         return this.shapeCache != null ? this.shapeCache.translucent : this.getBlock().isTranslucent(this.asBlockState(), world, pos);
//      }
//
//      public int getOpacity(BlockView world, BlockPos pos) {
//         return this.shapeCache != null ? this.shapeCache.lightSubtracted : this.getBlock().getOpacity(this.asBlockState(), world, pos);
//      }
//
//      public VoxelShape getCullingFace(BlockView world, BlockPos pos, Direction direction) {
//         return this.shapeCache != null && this.shapeCache.extrudedFaces != null ? this.shapeCache.extrudedFaces[direction.ordinal()] : VoxelShapes.extrudeFace(this.getCullingShape(world, pos), direction);
//      }
//
//      public VoxelShape getCullingShape(BlockView world, BlockPos pos) {
//         return this.getBlock().getCullingShape(this.asBlockState(), world, pos);
//      }
//
//      public boolean exceedsCube() {
//         return this.shapeCache == null || this.shapeCache.exceedsCube;
//      }
//
//      public boolean hasSidedTransparency() {
//         return this.hasSidedTransparency;
//      }

    /**
     * Returns the light level emitted by this block state.
     */
    public int getLuminance() {
        return this.luminance;
    }

    public boolean isAir() {
        return this.isAir;
    }

    public MaterialColour getTopMaterialColor(BlockView world, TilePos pos) {
        return this.materialColor;
    }

//      public BlockState rotate(BlockRotation rotation) {
//         return this.getBlock().rotate(this.asBlockState(), rotation);
//      }
//
//      public BlockState mirror(BlockMirror mirror) {
//         return this.getBlock().mirror(this.asBlockState(), mirror);
//      }
//
//      public BlockRenderType getRenderType() {
//         return this.getBlock().getRenderType(this.asBlockState());
//      }
//
//      @Environment(EnvType.CLIENT)
//      public boolean hasEmissiveLighting(BlockView world, BlockPos pos) {
//         return this.emissiveLightingPredicate.test(this.asBlockState(), world, pos);
//      }
//
//      @Environment(EnvType.CLIENT)
//      public float getAmbientOcclusionLightLevel(BlockView world, BlockPos pos) {
//         return this.getBlock().getAmbientOcclusionLightLevel(this.asBlockState(), world, pos);
//      }
//
//      public boolean isSolidBlock(BlockView world, BlockPos pos) {
//         return this.solidBlockPredicate.test(this.asBlockState(), world, pos);
//      }
//
//      public boolean emitsRedstonePower() {
//         return this.getBlock().emitsRedstonePower(this.asBlockState());
//      }
//
//      public int getWeakRedstonePower(BlockView world, BlockPos pos, Direction direction) {
//         return this.getBlock().getWeakRedstonePower(this.asBlockState(), world, pos, direction);
//      }
//
//      public boolean hasComparatorOutput() {
//         return this.getBlock().hasComparatorOutput(this.asBlockState());
//      }
//
//      public int getComparatorOutput(World world, BlockPos pos) {
//         return this.getBlock().getComparatorOutput(this.asBlockState(), world, pos);
//      }

    public float getHardness(BlockView world, TilePos pos) {
        return getBlock().getHardness(asBlockState(), world, pos);
    }

    public float calcBlockBreakingDelta(PlayerBase player, BlockView world, TilePos pos) {
        return getBlock().calcBlockBreakingDelta(asBlockState(), player, world, pos);
    }

//      public int getStrongRedstonePower(BlockView world, BlockPos pos, Direction direction) {
//         return this.getBlock().getStrongRedstonePower(this.asBlockState(), world, pos, direction);
//      }
//
//      public PistonBehavior getPistonBehavior() {
//         return this.getBlock().getPistonBehavior(this.asBlockState());
//      }
//
//      public boolean isOpaqueFullCube(BlockView world, BlockPos pos) {
//         if (this.shapeCache != null) {
//            return this.shapeCache.fullOpaque;
//         } else {
//            BlockState blockState = this.asBlockState();
//            return blockState.isOpaque() ? BlockBase.isShapeFullCube(blockState.getCullingShape(world, pos)) : false;
//         }
//      }

    public boolean isOpaque() {
        return this.opaque;
    }

//      @Environment(EnvType.CLIENT)
//      public boolean isSideInvisible(BlockState state, Direction direction) {
//         return this.getBlock().isSideInvisible(this.asBlockState(), state, direction);
//      }
//
//      public VoxelShape getOutlineShape(BlockView world, BlockPos pos) {
//         return this.getOutlineShape(world, pos, ShapeContext.absent());
//      }
//
//      public VoxelShape getOutlineShape(BlockView world, BlockPos pos, ShapeContext context) {
//         return this.getBlock().getOutlineShape(this.asBlockState(), world, pos, context);
//      }
//
//      public VoxelShape getCollisionShape(BlockView world, BlockPos pos) {
//         return this.shapeCache != null ? this.shapeCache.collisionShape : this.getCollisionShape(world, pos, ShapeContext.absent());
//      }
//
//      public VoxelShape getCollisionShape(BlockView world, BlockPos pos, ShapeContext context) {
//         return this.getBlock().getCollisionShape(this.asBlockState(), world, pos, context);
//      }
//
//      public VoxelShape getSidesShape(BlockView world, BlockPos pos) {
//         return this.getBlock().getSidesShape(this.asBlockState(), world, pos);
//      }
//
//      public VoxelShape getVisualShape(BlockView world, BlockPos pos, ShapeContext context) {
//         return this.getBlock().getVisualShape(this.asBlockState(), world, pos, context);
//      }
//
//      public VoxelShape getRaycastShape(BlockView world, BlockPos pos) {
//         return this.getBlock().getRaycastShape(this.asBlockState(), world, pos);
//      }
//
//      public final boolean hasSolidTopSurface(BlockView world, BlockPos pos, Entity entity) {
//         return this.hasSolidTopSurface(world, pos, entity, Direction.UP);
//      }
//
//      public final boolean hasSolidTopSurface(BlockView world, BlockPos pos, Entity entity, Direction direction) {
//         return BlockBase.isFaceFullSquare(this.getCollisionShape(world, pos, ShapeContext.of(entity)), direction);
//      }
//
//      public Vec3d getModelOffset(BlockView world, BlockPos pos) {
//         AbstractBlock.OffsetType offsetType = this.getBlock().getOffsetType();
//         if (offsetType == AbstractBlock.OffsetType.NONE) {
//            return Vec3d.ZERO;
//         } else {
//            long l = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
//            return new Vec3d(((double)((float)(l & 15L) / 15.0F) - 0.5D) * 0.5D, offsetType == AbstractBlock.OffsetType.XYZ ? ((double)((float)(l >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double)((float)(l >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
//         }
//      }
//
//      public boolean onSyncedBlockEvent(World world, BlockPos pos, int type, int data) {
//         return this.getBlock().onSyncedBlockEvent(this.asBlockState(), world, pos, type, data);
//      }
//
//      public void neighborUpdate(World world, BlockPos pos, BlockBase block, BlockPos posFrom, boolean notify) {
//         this.getBlock().neighborUpdate(this.asBlockState(), world, pos, block, posFrom, notify);
//      }
//
//      public final void updateNeighbors(WorldAccess world, BlockPos pos, int flags) {
//         this.updateNeighbors(world, pos, flags, 512);
//      }
//
//      public final void updateNeighbors(WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
//         this.getBlock();
//         BlockPos.Mutable mutable = new BlockPos.Mutable();
//         Direction[] var6 = AbstractBlock.FACINGS;
//         int var7 = var6.length;
//
//         for(int var8 = 0; var8 < var7; ++var8) {
//            Direction direction = var6[var8];
//            mutable.set(pos, direction);
//            BlockState blockState = world.getBlockState(mutable);
//            BlockState blockState2 = blockState.getStateForNeighborUpdate(direction.getOpposite(), this.asBlockState(), world, mutable, pos);
//            BlockBase.replace(blockState, blockState2, world, mutable, flags, maxUpdateDepth);
//         }
//
//      }
//
//      public final void prepare(WorldAccess world, BlockPos pos, int flags) {
//         this.prepare(world, pos, flags, 512);
//      }
//
//      public void prepare(WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
//         this.getBlock().prepare(this.asBlockState(), world, pos, flags, maxUpdateDepth);
//      }
//
//      public void onBlockAdded(World world, BlockPos pos, BlockState state, boolean notify) {
//         this.getBlock().onBlockAdded(this.asBlockState(), world, pos, state, notify);
//      }
//
//      public void onStateReplaced(World world, BlockPos pos, BlockState state, boolean moved) {
//         this.getBlock().onStateReplaced(this.asBlockState(), world, pos, state, moved);
//      }
//
//      public void scheduledTick(ServerWorld world, BlockPos pos, Random random) {
//         this.getBlock().scheduledTick(this.asBlockState(), world, pos, random);
//      }
//
//      public void randomTick(ServerWorld world, BlockPos pos, Random random) {
//         this.getBlock().randomTick(this.asBlockState(), world, pos, random);
//      }
//
//      public void onEntityCollision(World world, BlockPos pos, Entity entity) {
//         this.getBlock().onEntityCollision(this.asBlockState(), world, pos, entity);
//      }
//
//      public void onStacksDropped(ServerWorld world, BlockPos pos, ItemStack stack) {
//         this.getBlock().onStacksDropped(this.asBlockState(), world, pos, stack);
//      }
//
//      public List<ItemStack> getDroppedStacks(LootContext.Builder builder) {
//         return this.getBlock().getDroppedStacks(this.asBlockState(), builder);
//      }
//
//      public ActionResult onUse(World world, PlayerEntity player, Hand hand, BlockHitResult hit) {
//         return this.getBlock().onUse(this.asBlockState(), world, hit.getBlockPos(), player, hand, hit);
//      }
//
//      public void onBlockBreakStart(World world, BlockPos pos, PlayerEntity player) {
//         this.getBlock().onBlockBreakStart(this.asBlockState(), world, pos, player);
//      }
//
//      public boolean shouldSuffocate(BlockView world, BlockPos pos) {
//         return this.suffocationPredicate.test(this.asBlockState(), world, pos);
//      }
//
//      @Environment(EnvType.CLIENT)
//      public boolean shouldBlockVision(BlockView world, BlockPos pos) {
//         return this.blockVisionPredicate.test(this.asBlockState(), world, pos);
//      }
//
//      public BlockState getStateForNeighborUpdate(Direction direction, BlockState state, WorldAccess world, BlockPos pos, BlockPos fromPos) {
//         return this.getBlock().getStateForNeighborUpdate(this.asBlockState(), direction, state, world, pos, fromPos);
//      }
//
//      public boolean canPathfindThrough(BlockView world, BlockPos pos, NavigationType type) {
//         return this.getBlock().canPathfindThrough(this.asBlockState(), world, pos, type);
//      }
//
//      public boolean canReplace(ItemPlacementContext context) {
//         return this.getBlock().canReplace(this.asBlockState(), context);
//      }
//
//      public boolean canBucketPlace(Fluid fluid) {
//         return this.getBlock().canBucketPlace(this.asBlockState(), fluid);
//      }
//
//      public boolean canPlaceAt(WorldView world, BlockPos pos) {
//         return this.getBlock().canPlaceAt(this.asBlockState(), world, pos);
//      }
//
//      public boolean shouldPostProcess(BlockView world, BlockPos pos) {
//         return this.postProcessPredicate.test(this.asBlockState(), world, pos);
//      }
//
//      @Nullable
//      public NamedScreenHandlerFactory createScreenHandlerFactory(World world, BlockPos pos) {
//         return this.getBlock().createScreenHandlerFactory(this.asBlockState(), world, pos);
//      }

    public boolean isIn(TagKey<BlockBase> tag) {
        return BlockRegistry.INSTANCE.getEntry(BlockRegistry.INSTANCE.getKey(getBlock()).orElseThrow()).orElseThrow().isIn(tag);
    }

    public boolean isIn(TagKey<BlockBase> tag, Predicate<AbstractBlockState> predicate) {
        return this.isIn(tag) && predicate.test(this);
    }

    public boolean isIn(RegistryEntryList<BlockBase> blocks) {
        return blocks.contains(BlockRegistry.INSTANCE.getEntry(BlockRegistry.INSTANCE.getKey(getBlock()).orElseThrow()).orElseThrow());
    }

    public Stream<TagKey<BlockBase>> streamTags() {
        return BlockRegistry.INSTANCE.getEntry(BlockRegistry.INSTANCE.getKey(getBlock()).orElseThrow()).orElseThrow().streamTags();
    }

//      public boolean isOf(BlockBase block) {
//         return this.getBlock().is(block);
//      }
//
//      public FluidState getFluidState() {
//         return this.getBlock().getFluidState(this.asBlockState());
//      }

    public boolean hasRandomTicks() {
        return BlockBase.TICKS_RANDOMLY[getBlock().id];
    }

    @Environment(EnvType.CLIENT)
    public long getRenderingSeed(TilePos pos) {
        return MathHelper.hashCode(pos.x, pos.y, pos.z);
    }
//
//      public BlockSoundGroup getSoundGroup() {
//         return this.getBlock().getSoundGroup(this.asBlockState());
//      }
//
//      public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
//         this.getBlock().onProjectileHit(world, state, hit, projectile);
//      }
//
//      public boolean isSideSolidFullSquare(BlockView world, BlockPos pos, Direction direction) {
//         return this.isSideSolid(world, pos, direction, SideShapeType.FULL);
//      }
//
//      public boolean isSideSolid(BlockView world, BlockPos pos, Direction direction, SideShapeType shapeType) {
//         return this.shapeCache != null ? this.shapeCache.isSideSolid(direction, shapeType) : shapeType.matches(this.asBlockState(), world, pos, direction);
//      }
//
//      public boolean isFullCube(BlockView world, BlockPos pos) {
//         return this.shapeCache != null ? this.shapeCache.isFullCube : BlockBase.isShapeFullCube(this.getCollisionShape(world, pos));
//      }

    protected abstract BlockState asBlockState();

    public boolean isToolRequired() {
        return this.toolRequired;
    }
}