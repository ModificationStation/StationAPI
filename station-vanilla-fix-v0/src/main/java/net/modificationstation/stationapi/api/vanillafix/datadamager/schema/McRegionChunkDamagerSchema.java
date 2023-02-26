package net.modificationstation.stationapi.api.vanillafix.datadamager.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.Map;
import java.util.function.Supplier;

public class McRegionChunkDamagerSchema extends Schema {

    public McRegionChunkDamagerSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        super.registerTypes(schema, entityTypes, blockEntityTypes);
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
    }
}
