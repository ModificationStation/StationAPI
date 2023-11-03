package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Item.class)
public abstract class MixinItemBase implements StationItem {

    @Mutable
    @Shadow @Final public int id;

    @Shadow public abstract Item setTranslationKey(String string);

    @Shadow public abstract int getDurability();

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String getName(String name) {
        return StationAPI.EVENT_BUS.post(
                ItemEvent.TranslationKeyChanged.builder()
                        .item(Item.class.cast(this))
                        .translationKeyOverride(name)
                        .build()
        ).translationKeyOverride;
    }

    @Override
    public Item setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public Item setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }

    @Override
    @Unique
    public boolean preHit(ItemStack itemInstance, Entity otherEntity, PlayerEntity player) {
        return true;
    }

    @Override
    @Unique
    public boolean preMine(ItemStack itemInstance, BlockState blockState, int x, int y, int z, int side, PlayerEntity player) {
        return true;
    }

    @Override
    public int getDurability(ItemStack stack) {
        return getDurability();
    }
}
