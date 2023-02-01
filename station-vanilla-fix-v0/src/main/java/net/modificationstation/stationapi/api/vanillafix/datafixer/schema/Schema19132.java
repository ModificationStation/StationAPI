package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class Schema19132 extends Schema {

    public Schema19132(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        return Collections.emptyMap();
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        schema.registerType(
                false,
                TypeReferences.PLAYER,
                () -> DSL.optionalFields(
                        "Inventory",
                        DSL.list(TypeReferences.ITEM_STACK.in(schema))
                )
        );
        schema.registerType(
                false,
                TypeReferences.CHUNK,
                () -> DSL.fields(
                        "Level",
                        DSL.optionalFields(
                                "Entities",
                                DSL.list(TypeReferences.ENTITY.in(schema)),
                                "TileEntities",
                                DSL.list(TypeReferences.BLOCK_ENTITY.in(schema))
                        )
                )
        );
        schema.registerType(
                true,
                TypeReferences.ENTITY,
                () -> DSL.taggedChoiceLazy(
                        "id",
                        DSL.string(),
                        entityTypes
                )
        );
        schema.registerType(
                true,
                TypeReferences.BLOCK_ENTITY,
                () -> DSL.taggedChoiceLazy(
                        "id",
                        DSL.string(),
                        blockEntityTypes
                )
        );
        schema.registerType(
                true,
                TypeReferences.ITEM_STACK,
                () -> DSL.fields(
                        "id",
                        DSL.constType(DSL.shortType())
                )
        );
    }
}
