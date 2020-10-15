package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.common.block.BlockManager;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase {

    @Shadow @Final public static BlockBase[] BY_ID;

    @Shadow public abstract String getName();

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void beforeClinit(CallbackInfo ci) {
        ModIDRegistry.registries.put(BlockBase.class, new HashMap<>());
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(II)Lnet/minecraft/block/Stone;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeBlockRegister(CallbackInfo ci) {
        ModelRegister.EVENT.getInvoker().registerModels(ModelRegister.Type.BLOCKS);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void afterBlockRegister(CallbackInfo ci) {
        BlockRegister.EVENT.getInvoker().registerBlocks();
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/item/PlaceableTileEntity;"))
    private static PlaceableTileEntity getBlockItem(int blockID) {
        return BlockManager.INSTANCE.getBlockItem(BY_ID[blockID + BY_ID.length]);
    }

    @ModifyVariable(method = "setName(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;", at = @At("HEAD"))
    private String getName(String name) {
        String ret = BlockNameSet.EVENT.getInvoker().getName((BlockBase) (Object) this, name);
        Map<String, Map<String, Integer>> map = ModIDRegistry.registries.get(BlockBase.class);
        String modid = "minecraft";
        String blockName = ret;
        String[] strings = ret == null ? new String[0] : ret.split(":");
        if (strings.length > 1 && FabricLoader.getInstance().getModContainer(strings[0]).isPresent()) {
            modid = strings[0];
            blockName = blockName.substring(modid.length() + 1);
        }
        if (!map.containsKey(modid))
            map.put(modid, new HashMap<>());
        Map<String, Integer> blockEntries = map.get(modid);
        String oldRawName = getName();
        if (oldRawName != null)
            blockEntries.remove(oldRawName.substring("tile.".length()));
        blockEntries.put(blockName, ((BlockBase) (Object) this).id);
        return ret;
    }
}
