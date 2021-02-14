package net.modificationstation.stationapi.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.model.ModelRegister;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.block.BlockMiningLevel;
import net.modificationstation.stationapi.api.common.event.item.ItemNameSet;
import net.modificationstation.stationapi.api.common.event.item.ItemRegister;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationapi.api.common.item.StrengthOnMeta;
import net.modificationstation.stationapi.api.common.item.tool.OverrideIsEffectiveOn;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public class MixinItemBase implements EffectiveOnMeta, StrengthOnMeta {

    public MixinItemBase(int i) {
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(ILnet/minecraft/item/tool/ToolMaterial;)Lnet/minecraft/item/tool/Shovel;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeItemRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ModelRegister(ModelRegister.Type.ITEMS));
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;setupItemStats()V", shift = At.Shift.BEFORE))
    private static void afterItemRegister(CallbackInfo ci) {
        GeneralFactory.INSTANCE.addFactory(ItemBase.class, (args) -> ItemBase.class.cast(new MixinItemBase((int) args[0])));
        StationAPI.EVENT_BUS.post(new ItemRegister());
    }

    @Shadow
    public boolean isEffectiveOn(BlockBase tile) {
        return false;
    }

    @Shadow
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile) {
        return 0;
    }

    @ModifyVariable(method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;", at = @At("HEAD"))
    private String getName(String name) {
        return StationAPI.EVENT_BUS.post(new ItemNameSet((ItemBase) (Object) this, name)).newName;
    }

    @Override
    public boolean isEffectiveOn(BlockBase tile, int meta, ItemInstance itemInstance) {
        boolean effective = isEffectiveOn(tile);
        if (this instanceof ToolLevel) {
            effective =
                    ((BlockMiningLevel) tile).getToolTypes(meta, itemInstance) != null &&
                    ((BlockMiningLevel) tile).getToolTypes(meta, itemInstance).stream().anyMatch(entry -> entry != null && entry.isInstance(itemInstance.getType())) &&
                            ((ToolLevel) this).getToolLevel() >= ((BlockMiningLevel) tile).getBlockLevel(meta, itemInstance);
            if (this instanceof OverrideIsEffectiveOn) {
                effective = ((OverrideIsEffectiveOn) this).overrideIsEffectiveOn((ToolLevel) this, tile, meta, effective);
            }
        }
        return effective;
    }

    @Override
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta) {
        if (
            item.getType() instanceof ToolLevel &&
            ((BlockMiningLevel) tile).getBlockLevel(meta, item) <= ((ToolLevel) item.getType()).getToolLevel() &&
            ((BlockMiningLevel) tile).getBlockLevel(meta, item) != -1 &&
            ((BlockMiningLevel) tile).getToolTypes(meta, item) != null &&
            ((BlockMiningLevel) tile).getToolTypes(meta, item).stream().anyMatch((toolLevel) -> toolLevel != null && toolLevel.isInstance(item.getType()))
        ) {
            return ((ToolLevel) item.getType()).getMaterial().getMiningSpeed();
        }
        else {
            return getStrengthOnBlock(item, tile);
        }
    }
}
