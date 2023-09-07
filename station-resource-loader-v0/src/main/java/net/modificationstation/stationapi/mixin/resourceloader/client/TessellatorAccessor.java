package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {
    @Invoker("<init>")
    static Tessellator stationapi_create(int size) {
        return Util.assertMixin();
    }
}
