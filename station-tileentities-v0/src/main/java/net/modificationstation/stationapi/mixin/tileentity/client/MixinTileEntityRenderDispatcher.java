package net.modificationstation.stationapi.mixin.tileentity.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.tileentity.TileEntityRendererRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinTileEntityRenderDispatcher {

    @SuppressWarnings("unchecked")
    @Redirect(method = "<init>()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 2))
    private <K, V> V initCustomRenderers(Map<K, V> map, K key, V value){
        V ret = map.put(key, value);
        StationAPI.EVENT_BUS.post(TileEntityRendererRegisterEvent.builder().renderers((Map<Class<? extends BlockEntity>, BlockEntityRenderer>) map).build());
        return ret;
    }
}
