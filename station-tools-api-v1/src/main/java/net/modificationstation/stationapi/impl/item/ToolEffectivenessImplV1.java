package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.IsItemSuitableForStateEvent;
import net.modificationstation.stationapi.api.event.item.ItemMiningSpeedMultiplierOnStateEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ToolEffectivenessImplV1 {

    public static final List<Identifier> VANILLA_TOOLS = new ArrayList<>();

    @EventListener
    private static void getItems(ItemRegistryEvent event) {
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.shears));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.woodAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.woodPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.woodShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.woodSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.stoneAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.stonePickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.stoneShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.stoneSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.ironAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.ironPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.ironShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.ironSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.diamondAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.diamondPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.diamondShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.diamondSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.goldAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.goldPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.goldShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(ItemBase.goldSword));
    }

    @EventListener
    private static void isEffective(IsItemSuitableForStateEvent event) {
        event.suitable = event.suitable || isSuitable(event.itemStack, event.state);
    }

    @EventListener
    private static void getStrength(ItemMiningSpeedMultiplierOnStateEvent event) {
        if (!(VANILLA_TOOLS.contains(ItemRegistry.INSTANCE.getId(event.itemStack.getType())) && Objects.requireNonNull(BlockRegistry.INSTANCE.getId(event.state.getBlock())).modID == ModID.MINECRAFT) && isSuitable(event.itemStack, event.state)) event.miningSpeedMultiplier = ((ToolLevel) event.itemStack.getType()).getMaterial(event.itemStack).getMiningSpeed();
    }

    private static boolean isSuitable(ItemInstance item, BlockState state) {
        return item.getType() instanceof ToolLevel toolLevel && state.isIn(toolLevel.getEffectiveBlocks(item)) && toolLevel.getMaterial(item).matches(state);
    }
}
