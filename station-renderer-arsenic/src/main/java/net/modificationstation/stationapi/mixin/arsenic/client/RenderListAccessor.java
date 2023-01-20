package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.RenderList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderList.class)
public interface RenderListAccessor {

    @Accessor("field_2480")
    int stationapi$getField_2480();

    @Accessor("field_2481")
    int stationapi$getField_2481();

    @Accessor("field_2482")
    int stationapi$getField_2482();

    @Accessor("field_2483")
    float stationapi$getField_2483();

    @Accessor("field_2484")
    float stationapi$getField_2484();

    @Accessor("field_2485")
    float stationapi$getField_2485();
}
