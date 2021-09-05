package net.modificationstation.stationapi.mixin.tileentity;

import net.minecraft.client.render.entity.TileEntityRenderDispatcher;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRendererRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(TileEntityRenderDispatcher.class)
public class MixinTileEntityRenderDispatcher {
    @SuppressWarnings({"unchecked"})
    @Redirect(method = "<init>()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V initCustomRenderers(Map<K, V> map, K key, V value){
        V ret = map.put(key, value);
        StationAPI.EVENT_BUS.post(new TileEntityRendererRegisterEvent((Map<Class<? extends TileEntityBase>, TileEntityRenderer>) map));
        return ret;
    }
}
