package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.player.PlayerBaseSuper;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.item.ArmorUtil;
import net.modificationstation.stationapi.api.item.CustomArmorValue;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class CustomArmorValuesImpl {
    @EventListener
    private static void calcArmorDamageReduce(PlayerEvent.HandlerRegister event) {
        event.playerHandlers.add(new ArmorHandler(event.player));
    }

    private record ArmorHandler(PlayerEntity player) implements PlayerHandler {
        @Override
        public boolean damageEntityBase(int initialDamage) {
            double damageAmount = initialDamage;
            ItemStack[] armor = player.inventory.armor;

            for (int i = 0; i < armor.length; i++) {
                ItemStack armorInstance = armor[i];
                // This solution is not exact with vanilla, but is WAY better than previous solutions which weren't even close to vanilla.
                if (armorInstance != null) {
                    if (armorInstance.getItem() instanceof CustomArmorValue armorValue) {
                        double damageNegated = armorValue.modifyDamageDealt(player, i, initialDamage, damageAmount);
                        damageAmount -= damageNegated;
                    } else if (armorInstance.getItem() instanceof ArmorItem) {
                        damageAmount -= ArmorUtil.getVanillaArmorReduction(armorInstance);
                        armorInstance.damage(initialDamage, null);
                        if (armorInstance.count <= 0) {
                            armor[i] = null;
                        }
                    }
                    if (damageAmount < 0) {
                        damageAmount = 0;
                    }
                }
            }
            ((PlayerBaseSuper) player).superDamageEntity((int) damageAmount);
            return true;
        }
    }
}
