package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.class_214;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.lwjgl.opengl.GL11;

public class StationTextureManager {

    private final TextureManager textureManager;
    private final TextureManagerAccessor textureManagerAccessor;

    public StationTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
        textureManagerAccessor = (TextureManagerAccessor) textureManager;
    }

    public void tick() {
        for(int var1 = 0; var1 < textureManagerAccessor.getTextureBinders().size(); ++var1) {
            TextureBinder var2 = textureManagerAccessor.getTextureBinders().get(var1);
            var2.render3d = textureManagerAccessor.getGameOptions().anaglyph3d;
            var2.update();
            textureManagerAccessor.getField_1250().clear();
            if (var2.grid.length != textureManagerAccessor.getField_1250().capacity())
                textureManagerAccessor.setField_1250(class_214.method_744(var2.grid.length));
            textureManagerAccessor.getField_1250().put(var2.grid);
            textureManagerAccessor.getField_1250().position(0).limit(var2.grid.length);
            var2.bindTexture(textureManager);

            if (var2 instanceof StaticReferenceProvider) {
                Atlas.Texture staticReference = ((StaticReferenceProvider) var2).getStaticReference();
                for (int var3 = 0; var3 < var2.textureSize; ++var3)
                    for (int var4 = 0; var4 < var2.textureSize; ++var4)
                        GL11.glTexSubImage2D(3553, 0, staticReference.getX() + var3 * staticReference.getWidth(), staticReference.getY() + var4 * staticReference.getHeight(), staticReference.getWidth(), staticReference.getHeight(), 6408, 5121, textureManagerAccessor.getField_1250());
            } else {
                for (int var3 = 0; var3 < var2.textureSize; ++var3)
                    for (int var4 = 0; var4 < var2.textureSize; ++var4)
                        GL11.glTexSubImage2D(3553, 0, var2.index % 16 * 16 + var3 * 16, var2.index / 16 * 16 + var4 * 16, 16, 16, 6408, 5121, textureManagerAccessor.getField_1250());
            }
        }
    }
}
