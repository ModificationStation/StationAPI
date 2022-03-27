package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.item.ItemStrengthOnBlockEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import java.util.ArrayList;
import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ToolEffectivenessImplV1 {

    public static final List<Identifier> VANILLA_TOOLS = new ArrayList<>();

    @EventListener
    private static void getItems(ItemRegistryEvent event) {
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.shears));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.woodAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.woodPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.woodShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.woodSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.stoneAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.stonePickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.stoneShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.stoneSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.ironAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.ironPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.ironShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.ironSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.diamondAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.diamondPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.diamondShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.diamondSword));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.goldAxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.goldPickaxe));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.goldShovel));
        VANILLA_TOOLS.add(ItemRegistry.INSTANCE.getIdentifier(ItemBase.goldSword));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void isEffective(IsItemEffectiveOnBlockEvent event) {
        event.effective = event.effective || (event.itemInstance.getType() instanceof ToolLevel toolLevel && ((BlockToolLogic) event.block).getToolTagEffectiveness().stream().anyMatch(identifierIntegerBiTuple -> TagRegistry.INSTANCE.get(identifierIntegerBiTuple.one()).map(predicates -> predicates.stream().anyMatch(tagEntry -> tagEntry.predicate.test(event.itemInstance) && identifierIntegerBiTuple.two() <= toolLevel.getMaterial(event.itemInstance).getMiningLevel())).orElse(false)));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void getStrength(ItemStrengthOnBlockEvent event) {
        if (!(VANILLA_TOOLS.contains(ItemRegistry.INSTANCE.getIdentifier(event.itemInstance.getType())) && BlockRegistry.INSTANCE.getIdentifier(event.block).modID.toString().equals("minecraft")) && event.itemInstance.getType() instanceof ToolLevel toolLevel && ((BlockToolLogic) event.block).getToolTagEffectiveness().stream().anyMatch(identifierIntegerBiTuple -> TagRegistry.INSTANCE.get(identifierIntegerBiTuple.one()).map(predicates -> predicates.stream().anyMatch(tagEntry -> tagEntry.predicate.test(event.itemInstance))).orElse(false))) {
            event.strength = toolLevel.getMaterial(event.itemInstance).getMiningSpeed();
        }
    }
}
