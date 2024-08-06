package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationFlatteningBlock;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.sync.trackers.*;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.impl.block.BlockBrightness;
import net.modificationstation.stationapi.impl.block.BlockDropListImpl;
import net.modificationstation.stationapi.impl.block.StationFlatteningBlockInternal;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.ToIntFunction;

@Mixin(Block.class)
abstract class BlockMixin implements StationFlatteningBlock, StationFlatteningBlockInternal {
    @Shadow public abstract void dropStacks(World arg, int i, int j, int k, int l, float f);

    @Shadow protected abstract void dropStack(World arg, int i, int j, int k, ItemStack arg2);

    @Shadow public abstract void afterBreak(World arg, PlayerEntity arg2, int i, int j, int k, int l);

    @Shadow public abstract void dropStacks(World arg, int i, int j, int k, int l);

    @Shadow public abstract float getHardness();

    @Shadow @Final public Material material;

    @Shadow public abstract void onPlaced(World arg, int i, int j, int k);

    @Mutable
    @Shadow @Final public static Block[] BLOCKS;

    @Mutable
    @Shadow @Final public static boolean[] BLOCKS_RANDOM_TICK;

    @Mutable
    @Shadow @Final public static boolean[] BLOCKS_OPAQUE;

    @Mutable
    @Shadow @Final public static boolean[] BLOCKS_WITH_ENTITY;

    @Mutable
    @Shadow @Final public static int[] BLOCKS_LIGHT_OPACITY;

    @Mutable
    @Shadow @Final public static boolean[] BLOCKS_ALLOW_VISION;

    @Mutable
    @Shadow @Final public static int[] BLOCKS_LIGHT_LUMINANCE;

    @Mutable
    @Shadow @Final public static boolean[] BLOCKS_IGNORE_META_UPDATE;

    @Mutable
    @Shadow @Final public int id;

    @Unique
    private RegistryEntry.Reference<Block> stationapi_registryEntry;

    @Override
    @Unique
    public RegistryEntry.Reference<Block> getRegistryEntry() {
        return stationapi_registryEntry;
    }

    @Override
    @Unique
    public final void setRawId(int rawId) {
        id = rawId;
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;TRAPDOOR:Lnet/minecraft/block/Block;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_afterBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockRegistryEvent());
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(
                    intValue = 256,
                    ordinal = 15
            )
    )
    private static int stationapi_getBlocksSize(int constant) {
        return Block.BLOCKS.length;
    }

    @Unique
    private Item cachedBlockItem;

    @Override
    @Unique
    public Item asItem() {
        //noinspection SuspiciousMethodCalls
        return cachedBlockItem == null ? (cachedBlockItem = Item.BLOCK_ITEMS.get(this)) : cachedBlockItem;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/block/Material;)V",
            at = @At("RETURN")
    )
    private void stationapi_onInit(int material, Material par2, CallbackInfo ci) {
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder<>(Block.class.cast(this));
        appendProperties(builder);
        stationapi_stateManager = builder.build(Block::getDefaultState, BlockState::new);
        setDefaultState(stationapi_stateManager.getDefaultState());
    }

    @Unique
    private StateManager<Block, BlockState> stationapi_stateManager;
    @Unique
    private BlockState stationapi_defaultState;

    @Override
    @Unique
    public StateManager<Block, BlockState> getStateManager() {
        return stationapi_stateManager;
    }

    @Override
    @Unique
    public final BlockState getDefaultState() {
        return stationapi_defaultState;
    }

    @Override
    @Unique
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {}

    @Override
    @Unique
    public void setDefaultState(BlockState defaultState) {
        this.stationapi_defaultState = defaultState;
    }

    @Inject(
            method = "dropStacks(Lnet/minecraft/world/World;IIIIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_dropWithAChanceInject(World world, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (BlockDropListImpl.drop(world, x, y, z, world.getBlockState(x, y, z), meta, chance, this::dropStack, this)) ci.cancel();
    }

    @Override
    @Unique
    public void dropWithChance(World world, int x, int y, int z, BlockState state, int meta, float chance) {
        if (!BlockDropListImpl.drop(world, x, y, z, state, meta, chance, this::dropStack, this)) dropStacks(world, x, y, z, meta, chance);
    }

    @Inject(
            method = "dropStacks(Lnet/minecraft/world/World;IIIIF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;field_214:Ljava/util/Random;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void stationapi_beforeDrop(World world, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeDrop.builder()
                        .world(world)
                        .x(x).y(y).z(z)
                        .chance(chance)
                        .block(Block.class.cast(this))
                        .build()
                ).isCanceled()
        ) ci.cancel();
    }

    @Override
    @Unique
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        return null;
    }

    @Unique
    private boolean stationapi_afterBreak_argsPresent;
    @Unique
    private BlockState stationapi_afterBreak_state;

    @Override
    @Unique
    public void afterBreak(World world, PlayerEntity player, int x, int y, int z, BlockState state, int meta) {
        stationapi_afterBreak_state = state;
        stationapi_afterBreak_argsPresent = true;
        afterBreak(world, player, x, y, z, meta);
        stationapi_afterBreak_argsPresent = false;
        stationapi_afterBreak_state = null;
    }

    @Redirect(
            method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;dropStacks(Lnet/minecraft/world/World;IIII)V"
            )
    )
    private void stationapi_redirectDropToDropWithBlockState(Block block, World world, int x, int y, int z, int meta) {
        if (stationapi_afterBreak_argsPresent) drop(world, x, y, z, stationapi_afterBreak_state, meta);
        else dropStacks(world, x, y, z, meta);
    }

    @Override
    @Unique
    public float getHardness(BlockState state, BlockView blockView, BlockPos pos) {
        return getHardness();
    }

    @Override
    @Unique
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        float hardness = getHardness(state, world, pos);
        if (hardness < 0.0f) return 0.0f;
        if (!player.canHarvest(state)) return 1.0f / hardness / 100.0f;
        return player.getBlockBreakingSpeed(state) / hardness / 30.0f;
    }

    @Override
    @Unique
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return material.method_896() && (context.getStack() == null || !context.getStack().isOf(asItem()));
    }

    @Override
    @Unique
    public void onBlockPlaced(World world, int x, int y, int z, BlockState replacedState) {
        onPlaced(world, x, y, z);
    }

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/block/Block;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String stationapi_getTranslationKey(String name) {
        return StationAPI.EVENT_BUS.post(
                BlockEvent.TranslationKeyChanged.builder()
                        .block(Block.class.cast(this))
                        .currentTranslationKey(name)
                        .build()
        ).currentTranslationKey;
    }

    @Inject(
            method = "<clinit>",
            at = @At("HEAD")
    )
    private static void stationapi_setupRegistry(CallbackInfo ci) {
        BlockRegistry registry = BlockRegistry.INSTANCE;
        StateIdTracker.register(registry, STATE_IDS, block -> block.getStateManager().getStates());
        RemappableEntryArrayTracker.register(registry, () -> BLOCKS, array -> BLOCKS = array);
        BooleanArrayTracker.register(registry, () -> BLOCKS_RANDOM_TICK, array -> BLOCKS_RANDOM_TICK = array);
        BooleanArrayTracker.register(registry, () -> BLOCKS_OPAQUE, array -> BLOCKS_OPAQUE = array);
        BooleanArrayTracker.register(registry, () -> BLOCKS_WITH_ENTITY, array -> BLOCKS_WITH_ENTITY = array);
        IntArrayTracker.register(registry, () -> BLOCKS_LIGHT_OPACITY, array -> BLOCKS_LIGHT_OPACITY = array);
        BooleanArrayTracker.register(registry, () -> BLOCKS_ALLOW_VISION, array -> BLOCKS_ALLOW_VISION = array);
        IntArrayTracker.register(registry, () -> BLOCKS_LIGHT_LUMINANCE, array -> BLOCKS_LIGHT_LUMINANCE = array);
        BooleanArrayTracker.register(registry, () -> BLOCKS_IGNORE_META_UPDATE, array -> BLOCKS_IGNORE_META_UPDATE = array);
    }

    @ModifyVariable(
            method = "<init>(ILnet/minecraft/block/Material;)V",
            index = 1,
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=1",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            argsOnly = true
    )
    private int stationapi_ensureCapacity(int rawId) {
        //noinspection DataFlowIssue
        rawId = (stationapi_registryEntry = BlockRegistry.INSTANCE.createReservedEntry(rawId, (Block) (Object) this)).reservedRawId();
        // unfortunately, these arrays are accessed
        // too early for the trackers to resize them,
        // so we have to do it manually here
        if (ObjectArrayTracker.shouldGrow(BLOCKS, rawId)) BLOCKS = ObjectArrayTracker.grow(BLOCKS, rawId);
        if (BooleanArrayTracker.shouldGrow(BLOCKS_RANDOM_TICK, rawId)) BLOCKS_RANDOM_TICK = BooleanArrayTracker.grow(BLOCKS_RANDOM_TICK, rawId);
        if (BooleanArrayTracker.shouldGrow(BLOCKS_OPAQUE, rawId)) BLOCKS_OPAQUE = BooleanArrayTracker.grow(BLOCKS_OPAQUE, rawId);
        if (BooleanArrayTracker.shouldGrow(BLOCKS_WITH_ENTITY, rawId)) BLOCKS_WITH_ENTITY = BooleanArrayTracker.grow(BLOCKS_WITH_ENTITY, rawId);
        if (IntArrayTracker.shouldGrow(BLOCKS_LIGHT_OPACITY, rawId)) BLOCKS_LIGHT_OPACITY = IntArrayTracker.grow(BLOCKS_LIGHT_OPACITY, rawId);
        if (BooleanArrayTracker.shouldGrow(BLOCKS_ALLOW_VISION, rawId)) BLOCKS_ALLOW_VISION = BooleanArrayTracker.grow(BLOCKS_ALLOW_VISION, rawId);
        if (IntArrayTracker.shouldGrow(BLOCKS_LIGHT_LUMINANCE, rawId)) BLOCKS_LIGHT_LUMINANCE = IntArrayTracker.grow(BLOCKS_LIGHT_LUMINANCE, rawId);
        if (BooleanArrayTracker.shouldGrow(BLOCKS_IGNORE_META_UPDATE, rawId)) BLOCKS_IGNORE_META_UPDATE = BooleanArrayTracker.grow(BLOCKS_IGNORE_META_UPDATE, rawId);
        return rawId;
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Item;ITEMS:[Lnet/minecraft/item/Item;",
                    args = "array=get"
            )
    )
    private static Item stationapi_onlyUnderShift(Item[] array, int index) {
        return index < ItemRegistry.ID_SHIFT ? array[index] : null;
    }

    @Unique private ToIntFunction<BlockState> stationapi_luminance = state -> Block.BLOCKS_LIGHT_LUMINANCE[state.getBlock().id];

    @Override
    @Unique
    public Block setLuminance(ToIntFunction<BlockState> provider) {
        stationapi_luminance = provider;

        // Need for proper functionality of LevelMixin
        Block.BLOCKS_LIGHT_LUMINANCE[id] = 15;

        return Block.class.cast(this);
    }

    @Override
    @Unique
    public ToIntFunction<BlockState> stationapi_getLuminanceProvider() {
        return stationapi_luminance;
    }

    @Environment(value= EnvType.CLIENT)
    @ModifyArg(
            method = "getLuminance",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/BlockView;method_1784(IIII)F"
            ),
            index = 3
    )
    private int stationapi_getStateBrightness(int original) {
        return BlockBrightness.light;
    }
    
    @Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
    private void stationapi_temporalStatFix(World world, PlayerEntity player, int x, int y, int z, int meta, CallbackInfo info) {
        if (id < Stats.MINE_BLOCK.length) return;
        this.dropStacks(world, x, y, z, meta);
        info.cancel();
    }
}
