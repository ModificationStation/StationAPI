package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.common.block.BlockManager;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.event.block.BlockNameSet;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
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
public class MixinBlockBase {

    public MixinBlockBase(int i, Material material) { }

    public MixinBlockBase(int i, int j, Material material) { }

    @Shadow @Final public static BlockBase[] BY_ID;

    @Shadow public String getName() {return null;}

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void onStatic(CallbackInfo ci) {
        specialCases = new HashMap<>();
        Map<String, String> minecraft = new HashMap<>();
        minecraft.put("brick", "brick_block");
        specialCases.put("minecraft", minecraft);
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "NEW", target = "(II)Lnet/minecraft/block/Stone;", ordinal = 0, shift = At.Shift.BEFORE))
    private static void beforeBlockRegister(CallbackInfo ci) {
        ModelRegister.EVENT.getInvoker().registerModels(ModelRegister.Type.BLOCKS);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void afterBlockRegister(CallbackInfo ci) {
        GeneralFactory.INSTANCE.addFactory(BlockBase.class, (args) ->
                args[0] instanceof Integer ?
                        args[1] instanceof Material ?
                                BlockBase.class.cast(new MixinBlockBase((int) args[0], (Material) args[1])) :
                                args[1] instanceof Integer && args[2] instanceof Material ?
                                        BlockBase.class.cast(new MixinBlockBase((int) args[0], (int) args[1], (Material) args[2])) :
                                        null :
                        null
        );
        ModIDEvent<BlockRegister> event = BlockRegister.EVENT;
        BlockRegister invoker = event.getInvoker();
        event.setCurrentListener(invoker);
        invoker.registerBlocks();
        event.setCurrentListener(null);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/item/PlaceableTileEntity;"))
    private static PlaceableTileEntity getBlockItem(int blockID) {
        return BlockManager.INSTANCE.getBlockItem(BY_ID[blockID + BY_ID.length]);
    }

    @ModifyVariable(method = "setName(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;", at = @At("HEAD"))
    private String getName(String name) {
        name = BlockNameSet.EVENT.getInvoker().getName((BlockBase) (Object) this, name);
        Map<String, Map<String, Integer>> map = ModIDRegistry.item;
        String modid = "minecraft";
        String blockName = name;
        String[] strings = name == null ? new String[0] : name.split(":");
        if (strings.length > 1 && FabricLoader.getInstance().getModContainer(strings[0]).isPresent()) {
            modid = strings[0];
            blockName = blockName.substring(modid.length() + 1);
        }
        blockName = specialCases.containsKey(modid) ? specialCases.get(modid).getOrDefault(blockName, blockName) : blockName;
        if (!map.containsKey(modid))
            map.put(modid, new HashMap<>());
        Map<String, Integer> blockEntries = map.get(modid);
        if (actualName != null)
            blockEntries.remove(actualName);
        actualName = blockName;
        blockEntries.put(blockName, ((BlockBase) (Object) this).id);
        return name;
    }

    private String actualName;

    private static Map<String, Map<String, String>> specialCases;
}
