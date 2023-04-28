package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
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
import net.modificationstation.stationapi.impl.block.BlockDropListImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements StationFlatteningBlock {

    @Shadow public abstract void beforeDestroyedByExplosion(Level arg, int i, int j, int k, int l, float f);

    @Shadow protected abstract void drop(Level arg, int i, int j, int k, ItemInstance arg2);

    @Shadow public abstract void afterBreak(Level arg, PlayerBase arg2, int i, int j, int k, int l);

    @Shadow public abstract void drop(Level arg, int i, int j, int k, int l);

    @Shadow public abstract float getHardness();

    @Shadow @Final public Material material;

    @Shadow public abstract void onBlockPlaced(Level arg, int i, int j, int k);

    @Mutable
    @Shadow @Final public static BlockBase[] BY_ID;

    @Mutable
    @Shadow @Final public static boolean[] TICKS_RANDOMLY;

    @Mutable
    @Shadow @Final public static boolean[] FULL_OPAQUE;

    @Mutable
    @Shadow @Final public static boolean[] HAS_TILE_ENTITY;

    @Mutable
    @Shadow @Final public static int[] LIGHT_OPACITY;

    @Mutable
    @Shadow @Final public static boolean[] ALLOWS_GRASS_UNDER;

    @Mutable
    @Shadow @Final public static int[] EMITTANCE;

    @Mutable
    @Shadow @Final public static boolean[] NO_NOTIFY_ON_META_CHANGE;

    @Mutable
    @Shadow @Final public int id;

    @Unique
    private RegistryEntry.Reference<BlockBase> stationapi_registryEntry;

    @Override
    @Unique
    public RegistryEntry.Reference<BlockBase> getRegistryEntry() {
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
                    target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void afterBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockRegistryEvent());
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(
                    intValue = 256,
                    ordinal = 15
            )
    )
    private static int getBlocksSize(int constant) {
        return BlockBase.BY_ID.length;
    }

    private ItemBase cachedBlockItem;

    @Override
    public ItemBase asItem() {
        //noinspection SuspiciousMethodCalls
        return cachedBlockItem == null ? (cachedBlockItem = ItemBase.BLOCK_ITEMS.get(this)) : cachedBlockItem;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/block/material/Material;)V",
            at = @At("RETURN")
    )
    private void onInit(int material, Material par2, CallbackInfo ci) {
        StateManager.Builder<BlockBase, BlockState> builder = new StateManager.Builder<>(BlockBase.class.cast(this));
        appendProperties(builder);
        stationapi_stateManager = builder.build(BlockBase::getDefaultState, BlockState::new);
        setDefaultState(stationapi_stateManager.getDefaultState());
    }

    @Unique
    private StateManager<BlockBase, BlockState> stationapi_stateManager;
    @Unique
    private BlockState stationapi_defaultState;

    @Override
    @Unique
    public StateManager<BlockBase, BlockState> getStateManager() {
        return stationapi_stateManager;
    }

    @Override
    @Unique
    public final BlockState getDefaultState() {
        return stationapi_defaultState;
    }

    @Override
    @Unique
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {}

    @Override
    @Unique
    public void setDefaultState(BlockState defaultState) {
        this.stationapi_defaultState = defaultState;
    }

    @Inject(
            method = "beforeDestroyedByExplosion(Lnet/minecraft/level/Level;IIIIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dropWithAChanceInject(Level level, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (BlockDropListImpl.drop(level, x, y, z, level.getBlockState(x, y, z), meta, chance, this::drop, this)) ci.cancel();
    }

    @Override
    public void dropWithChance(Level level, int x, int y, int z, BlockState state, int meta, float chance) {
        if (!BlockDropListImpl.drop(level, x, y, z, state, meta, chance, this::drop, this)) beforeDestroyedByExplosion(level, x, y, z, meta, chance);
    }

    @Inject(
            method = "beforeDestroyedByExplosion(Lnet/minecraft/level/Level;IIIIF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/Level;rand:Ljava/util/Random;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void beforeDrop(Level level, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeDrop.builder()
                        .level(level)
                        .x(x).y(y).z(z)
                        .chance(chance)
                        .block(BlockBase.class.cast(this))
                        .build()
                ).isCanceled()
        ) ci.cancel();
    }

    @Override
    @Unique
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
        return null;
    }

    @Unique
    private boolean stationapi_afterBreak_argsPresent;
    @Unique
    private BlockState stationapi_afterBreak_state;

    @Override
    public void afterBreak(Level level, PlayerBase player, int x, int y, int z, BlockState state, int meta) {
        stationapi_afterBreak_state = state;
        stationapi_afterBreak_argsPresent = true;
        afterBreak(level, player, x, y, z, meta);
        stationapi_afterBreak_argsPresent = false;
        stationapi_afterBreak_state = null;
    }

    @Redirect(
            method = "afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;drop(Lnet/minecraft/level/Level;IIII)V"
            )
    )
    private void redirectDropToDropWithBlockState(BlockBase block, Level level, int x, int y, int z, int meta) {
        if (stationapi_afterBreak_argsPresent) drop(level, x, y, z, stationapi_afterBreak_state, meta);
        else drop(level, x, y, z, meta);
    }

    @Override
    public float getHardness(BlockState state, BlockView blockView, TilePos pos) {
        return getHardness();
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerBase player, BlockView world, TilePos pos) {
        float hardness = getHardness(state, world, pos);
        if (hardness < 0.0f) return 0.0f;
        if (!player.canHarvest(state)) return 1.0f / hardness / 100.0f;
        return player.getBlockBreakingSpeed(state) / hardness / 30.0f;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return material.isReplaceable() && (context.getStack() == null || !context.getStack().isOf(asItem()));
    }

    @Override
    public void onBlockPlaced(Level world, int x, int y, int z, BlockState replacedState) {
        onBlockPlaced(world, x, y, z);
    }

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String getTranslationKey(String name) {
        return StationAPI.EVENT_BUS.post(
                BlockEvent.TranslationKeyChanged.builder()
                        .block(BlockBase.class.cast(this))
                        .currentTranslationKey(name)
                        .build()
        ).currentTranslationKey;
    }

    @Inject(
            method = "<clinit>",
            at = @At("HEAD")
    )
    private static void setupRegistry(CallbackInfo ci) {
        BlockRegistry registry = BlockRegistry.INSTANCE;
        StateIdTracker.register(registry, STATE_IDS, block -> block.getStateManager().getStates());
        RemappableEntryArrayTracker.register(registry, () -> BY_ID, array -> BY_ID = array);
        BooleanArrayTracker.register(registry, () -> TICKS_RANDOMLY, array -> TICKS_RANDOMLY = array);
        BooleanArrayTracker.register(registry, () -> FULL_OPAQUE, array -> FULL_OPAQUE = array);
        BooleanArrayTracker.register(registry, () -> HAS_TILE_ENTITY, array -> HAS_TILE_ENTITY = array);
        IntArrayTracker.register(registry, () -> LIGHT_OPACITY, array -> LIGHT_OPACITY = array);
        BooleanArrayTracker.register(registry, () -> ALLOWS_GRASS_UNDER, array -> ALLOWS_GRASS_UNDER = array);
        IntArrayTracker.register(registry, () -> EMITTANCE, array -> EMITTANCE = array);
        BooleanArrayTracker.register(registry, () -> NO_NOTIFY_ON_META_CHANGE, array -> NO_NOTIFY_ON_META_CHANGE = array);
    }

    @ModifyVariable(
            method = "<init>(ILnet/minecraft/block/material/Material;)V",
            index = 1,
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=1",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            argsOnly = true
    )
    private int ensureCapacity(int rawId) {
        //noinspection DataFlowIssue
        rawId = (stationapi_registryEntry = BlockRegistry.INSTANCE.createReservedEntry(rawId, (BlockBase) (Object) this)).reservedRawId();
        // unfortunately, these arrays are accessed
        // too early for the trackers to resize them,
        // so we have to do it manually here
        if (ObjectArrayTracker.shouldGrow(BY_ID, rawId)) BY_ID = ObjectArrayTracker.grow(BY_ID, rawId);
        if (BooleanArrayTracker.shouldGrow(TICKS_RANDOMLY, rawId)) TICKS_RANDOMLY = BooleanArrayTracker.grow(TICKS_RANDOMLY, rawId);
        if (BooleanArrayTracker.shouldGrow(FULL_OPAQUE, rawId)) FULL_OPAQUE = BooleanArrayTracker.grow(FULL_OPAQUE, rawId);
        if (BooleanArrayTracker.shouldGrow(HAS_TILE_ENTITY, rawId)) HAS_TILE_ENTITY = BooleanArrayTracker.grow(HAS_TILE_ENTITY, rawId);
        if (IntArrayTracker.shouldGrow(LIGHT_OPACITY, rawId)) LIGHT_OPACITY = IntArrayTracker.grow(LIGHT_OPACITY, rawId);
        if (BooleanArrayTracker.shouldGrow(ALLOWS_GRASS_UNDER, rawId)) ALLOWS_GRASS_UNDER = BooleanArrayTracker.grow(ALLOWS_GRASS_UNDER, rawId);
        if (IntArrayTracker.shouldGrow(EMITTANCE, rawId)) EMITTANCE = IntArrayTracker.grow(EMITTANCE, rawId);
        if (BooleanArrayTracker.shouldGrow(NO_NOTIFY_ON_META_CHANGE, rawId)) NO_NOTIFY_ON_META_CHANGE = BooleanArrayTracker.grow(NO_NOTIFY_ON_META_CHANGE, rawId);
        return rawId;
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;byId:[Lnet/minecraft/item/ItemBase;",
                    args = "array=get"
            )
    )
    private static ItemBase onlyUnderShift(ItemBase[] array, int index) {
        return index < ItemRegistry.ID_SHIFT ? array[index] : null;
    }
}
