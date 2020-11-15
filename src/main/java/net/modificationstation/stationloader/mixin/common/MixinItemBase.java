package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.event.item.ItemNameSet;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.IsEffectiveOn;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
import net.modificationstation.stationloader.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationloader.api.common.item.StrengthOnMeta;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ItemBase.class)
public class MixinItemBase implements EffectiveOnMeta, StrengthOnMeta {

    @Shadow public boolean isEffectiveOn(BlockBase tile) { return false; }

    @Shadow public float getStrengthOnBlock(ItemInstance item, BlockBase tile) { return 0; }

    public MixinItemBase(int i) {}

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void onStatic(CallbackInfo ci) {
        specialCases = new HashMap<>();
        Map<String, String> minecraft = new HashMap<>();
        minecraft.put("clay", "clay_ball");
        specialCases.put("minecraft", minecraft);
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(ILnet/minecraft/item/tool/ToolMaterial;)Lnet/minecraft/item/tool/Shovel;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeItemRegister(CallbackInfo ci) {
        ModelRegister.EVENT.getInvoker().registerModels(ModelRegister.Type.ITEMS);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;onItemsRegistered()V", shift = At.Shift.BEFORE))
    private static void afterItemRegister(CallbackInfo ci) {
        GeneralFactory.INSTANCE.addFactory(ItemBase.class, (args) -> ItemBase.class.cast(new MixinItemBase((int) args[0])));
        ModIDEvent<ItemRegister> event = ItemRegister.EVENT;
        ItemRegister invoker = event.getInvoker();
        event.setCurrentListener(invoker);
        invoker.registerItems();
        event.setCurrentListener(null);
    }

    @ModifyVariable(method = "setName(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;", at = @At("HEAD"))
    private String getName(String name) {
        name = ItemNameSet.EVENT.getInvoker().getName((ItemBase) (Object) this, name);
        Map<String, Map<String, Integer>> map = ModIDRegistry.item;
        String modid = "minecraft";
        String itemName = name;
        String[] strings = name == null ? new String[0] : name.split(":");
        if (strings.length > 1 && FabricLoader.getInstance().getModContainer(strings[0]).isPresent()) {
            modid = strings[0];
            itemName = itemName.substring(modid.length() + 1);
        }
        itemName = specialCases.containsKey(modid) ? specialCases.get(modid).getOrDefault(itemName, itemName) : itemName;
        if (!map.containsKey(modid))
            map.put(modid, new HashMap<>());
        Map<String, Integer> itemEntries = map.get(modid);
        if (actualName != null)
            itemEntries.remove(actualName);
        actualName = itemName;
        itemEntries.put(itemName, ((ItemBase) (Object) this).id);
        return name;
    }

    @Override
    public boolean isEffectiveOn(BlockBase tile, int meta) {
        boolean ret = isEffectiveOn(tile);
        if (this instanceof ToolLevel) {
            AtomicBoolean effective = new AtomicBoolean(ret);
            IsEffectiveOn.EVENT.getInvoker().isEffectiveOn((ToolLevel) this, tile, meta, effective);
            ret = effective.get();
        }
        return ret;
    }

    @Override
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta) {
        return getStrengthOnBlock(item, tile);
    }

    private String actualName;

    private static Map<String, Map<String, String>> specialCases;
}
