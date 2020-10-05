package net.modificationstation.stationloader.mixin.common;

import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityPiston;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;

@Mixin(TileEntityBase.class)
public abstract class MixinTileEntityBase {

    @Shadow
    private static void register(Class arg, String string) {
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method="<clinit>", at=@At(target = "Lnet/minecraft/tileentity/TileEntityBase;register(Ljava/lang/Class;Ljava/lang/String;)V", value = "INVOKE"))
    private static void registerModdedTileEntities(Class tileEntity, String id) {
        register(tileEntity, id);
        if (tileEntity == TileEntityPiston.class) {
            HashMap<String, Class> moddedTileEntities = new HashMap<>();
            TileEntityRegister.EVENT.getInvoker().registerTileEntities(moddedTileEntities);
            for (String moddedTileEntity : moddedTileEntities.keySet()) {
                register(moddedTileEntities.get(moddedTileEntity), moddedTileEntity);
            }
        }
    }
}
