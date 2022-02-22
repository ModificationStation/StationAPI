package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_556;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(class_556.class)
@Environment(EnvType.CLIENT)
public class Mixinclass_556 {

    @Unique
    private final ArsenicOverlayRenderer arsenic_plugin = new ArsenicOverlayRenderer((class_556) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void method_1862(Living entity, ItemInstance item) {
        arsenic_plugin.renderItem3D(entity, item);
    }
}
