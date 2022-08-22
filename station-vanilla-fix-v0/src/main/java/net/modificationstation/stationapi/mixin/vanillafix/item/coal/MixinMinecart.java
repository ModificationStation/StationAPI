package net.modificationstation.stationapi.mixin.vanillafix.item.coal;

import net.minecraft.entity.Minecart;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.vanillafix.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecart.class)
public class MixinMinecart {

    @Redirect(
            method = "interact(Lnet/minecraft/entity/player/PlayerBase;)Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;id:I"
            )
    )
    private int redirectItemId(ItemBase instance, PlayerBase arg) {
        return ((StationItem) instance).getRegistryEntry().isIn(ItemTags.COALS) ? arg.inventory.getHeldItem().itemId : -1;
    }
}
