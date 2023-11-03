package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.player.PlayerBaseSuper;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.item.ArmourUtils;
import net.modificationstation.stationapi.api.item.CustomArmourValue;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class CustomArmourValuesImpl {
    @EventListener
    private static void calcArmourDamageReduce(PlayerEvent.HandlerRegister event) {
        event.playerHandlers.add(new ArmourHandler(event.player));
    }

    private record ArmourHandler(PlayerEntity player) implements PlayerHandler {
        @Override
        public boolean damageEntityBase(int initialDamage) {
            double damageAmount = initialDamage;
            ItemStack[] armour = player.inventory.armor;

            for (int i = 0; i < armour.length; i++) {
                ItemStack armourInstance = armour[i];
                // This solution is not exact with vanilla, but is WAY better than previous solutions which weren't even close to vanilla.
                if (armourInstance != null) {
                    if (armourInstance.getItem() instanceof CustomArmourValue armor) {
                        double damageNegated = armor.modifyDamageDealt(player, i, initialDamage, damageAmount);
                        damageAmount -= damageNegated;
                    } else if (armourInstance.getItem() instanceof ArmorItem) {
                        damageAmount -= ArmourUtils.getVanillaArmourReduction(armourInstance);
                        armourInstance.damage(initialDamage, null);
                        if (armourInstance.count <= 0) {
                            armour[i] = null;
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
