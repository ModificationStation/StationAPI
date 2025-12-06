package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.GlAllocationUtils;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class ArsenicTextureManager {

    private final TextureManager self;
    private final TextureManagerAccessor access;

    public ArsenicTextureManager(TextureManager self) {
        this.self = self;
        access = (TextureManagerAccessor) self;
    }

    public void getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {
        switch (par1) {
            case "/terrain.png", "/gui/items.png" -> cir.setReturnValue(StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getGlId());
        }
    }

    public void ensureBufferCapacity(int expectedCapacity) {
        if (expectedCapacity > access.getImageBuffer().capacity())
            access.setImageBuffer(GlAllocationUtils.allocateByteBuffer(expectedCapacity));
    }
}
