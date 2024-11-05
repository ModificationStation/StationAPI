package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.IsItemSuitableForStateEvent;
import net.modificationstation.stationapi.api.event.item.ItemMiningSpeedMultiplierOnStateEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.StationTool;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.LOW;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ToolEffectivenessImplV1 {
    public static final List<Identifier> VANILLA_TOOLS = new ArrayList<>();

    @EventListener(priority = LOW)
    private static void getItems(ItemRegistryEvent event) {
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.SHEARS));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.WOODEN_AXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.WOODEN_PICKAXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.WOODEN_SHOVEL));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.WOODEN_SWORD));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.STONE_HATCHET));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.STONE_PICKAXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.STONE_SHOVEL));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.STONE_SWORD));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.IRON_AXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.IRON_PICKAXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.IRON_SHOVEL));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.IRON_SWORD));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.DIAMOND_AXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.DIAMOND_PICKAXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.DIAMOND_SHOVEL));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.DIAMOND_SWORD));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.GOLDEN_AXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.GOLDEN_PICKAXE));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.GOLDEN_SHOVEL));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getId(Item.GOLDEN_SWORD));
    }

    @EventListener
    private static void isEffective(IsItemSuitableForStateEvent event) {
        // Disable custom tool logic if both the block and the tool are vanilla
        // This is done to preserve the vanilla mining speeds
        if (VANILLA_TOOLS.contains(ItemRegistry.INSTANCE.getId(event.itemStack.getItem()))
                && Objects.requireNonNull(BlockRegistry.INSTANCE.getId(event.state.getBlock())).namespace == Namespace.MINECRAFT) return;

        // Disable custom tool logic if the tool doesn't provide its tool type tag
        // This is done to allow tools to handle suitability and speed the vanilla way
        if (event.itemStack.getItem() instanceof StationTool stationTool
                && stationTool.getEffectiveBlocks(event.itemStack) == null) return;

        event.suitable = isSuitable(event.itemStack, event.state);
    }

    @EventListener
    private static void getStrength(ItemMiningSpeedMultiplierOnStateEvent event) {
        // Disable custom tool logic if both the block and the tool are vanilla
        // This is done to preserve the vanilla mining speeds
        if (VANILLA_TOOLS.contains(ItemRegistry.INSTANCE.getId(event.itemStack.getItem()))
                && Objects.requireNonNull(BlockRegistry.INSTANCE.getId(event.state.getBlock())).namespace == Namespace.MINECRAFT) return;

        // Disable custom tool logic if the tool doesn't provide its tool type tag
        // This is done to allow tools to handle suitability and speed the vanilla way
        if (event.itemStack.getItem() instanceof StationTool stationTool
                && stationTool.getEffectiveBlocks(event.itemStack) == null) return;

        if (!isSuitable(event.itemStack, event.state)) return;

        event.miningSpeedMultiplier = ((StationTool) event.itemStack.getItem()).getMaterial(event.itemStack).getMiningSpeedMultiplier();
    }

    private static boolean isSuitable(ItemStack item, BlockState state) {
        return item.getItem() instanceof StationTool stationTool
                && state.isIn(stationTool.getEffectiveBlocks(item))
                && ToolLevel.isSuitable(stationTool.getMaterial(item).getToolLevel(), state);
    }
}
