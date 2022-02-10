package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.*;

@Mixin(class_66.class)
public interface class_66Accessor {

    @Invoker
    void invokeMethod_306();

    @Accessor
    int getField_225();

    @Accessor
    void setField_227(boolean field_227);

    @Accessor
    List<TileEntityBase> getField_228();

    @Accessor
    static Tessellator getTesselator() {
        return Util.assertMixin();
    }
}
