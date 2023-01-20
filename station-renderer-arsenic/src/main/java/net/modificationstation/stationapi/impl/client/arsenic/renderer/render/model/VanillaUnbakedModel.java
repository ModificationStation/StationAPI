package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.model;

import com.mojang.datafixers.util.Pair;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.mesh.MeshBuilder;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.ModelBakeSettings;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@SuppressWarnings("ClassCanBeRecord")
public class VanillaUnbakedModel implements UnbakedModel {

    private static final Identifier DEFAULT_BLOCK_MODEL = Identifier.of("block/block");

    private final Identifier itemId;

    public VanillaUnbakedModel(Identifier itemId) {
        this.itemId = itemId;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.singleton(DEFAULT_BLOCK_MODEL);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptySet();
    }

    @Override
    public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Renderer renderer = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer());
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        AtomicReference<Sprite> particle = new AtomicReference<>();
        particle.set(textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, MissingSprite.getMissingSpriteId())));

        OptionalInt optionalId = ItemRegistry.INSTANCE.getLegacyId(itemId);
        if (optionalId.isPresent()) {
            int id = optionalId.getAsInt();
            if (id < BlockRegistry.INSTANCE.getSize()) {
                return BlockRegistry.INSTANCE.getByLegacyId(id).map(block -> {
                    if (block.getRenderType() == 0) {
                        block.method_1605();
                        ExpandableAtlas atlas = Atlases.getTerrain();
                        particle.set(textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(0)).getId())));
                        emitter.square(Direction.DOWN, (float) block.minX, (float) block.minZ, (float) block.maxX, (float) block.maxZ, (float) block.minY);
                        emitter.cullFace(Direction.DOWN);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.DOWN.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                        emitter.square(Direction.UP, (float) block.minX, (float) block.minZ, (float) block.maxX, (float) block.maxZ, (float) (1 - block.maxY));
                        emitter.cullFace(Direction.UP);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.UP.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                        emitter.square(Direction.EAST, (float) block.minX, (float) block.minY, (float) block.maxX, (float) block.maxY, (float) block.minZ);
                        emitter.cullFace(Direction.EAST);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.EAST.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                        emitter.square(Direction.WEST, (float) block.minX, (float) block.minY, (float) block.maxX, (float) block.maxY, (float) (1 - block.maxZ));
                        emitter.cullFace(Direction.WEST);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.WEST.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                        emitter.square(Direction.NORTH, (float) block.minZ, (float) block.minY, (float) block.maxZ, (float) block.maxY, (float) block.minX);
                        emitter.cullFace(Direction.NORTH);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.NORTH.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                        emitter.square(Direction.SOUTH, (float) block.minZ, (float) block.minY, (float) block.maxZ, (float) block.maxY, (float) (1 - block.maxX));
                        emitter.cullFace(Direction.SOUTH);
                        emitter.spriteBake(0, textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, atlas.getTexture(block.getTextureForSide(Direction.SOUTH.ordinal())).getId())), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColour(0, -1, -1, -1, -1);
                        emitter.emit();
                    }
                    return new VanillaBakedModel(block, builder.build(), particle.get(), ((JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL)).getTransformations());
                }).orElse(null);
            }
        }
        return null;
    }
}
