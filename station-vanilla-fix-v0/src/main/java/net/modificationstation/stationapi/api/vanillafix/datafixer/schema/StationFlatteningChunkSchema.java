package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.world.FlattenedWorldManager.SECTIONS;

public class StationFlatteningChunkSchema extends Schema {
    public StationFlatteningChunkSchema(int versionKey, Schema parent) {
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
                                DSL.list(TypeReferences.BLOCK_ENTITY.in(schema)),
                                SECTIONS,
                                DSL.list(DSL.optionalFields("block_states", DSL.optionalFields("palette", DSL.list(TypeReferences.BLOCK_STATE.in(schema)))))
                        )
                )
        );
        schema.registerType(
                false,
                TypeReferences.BLOCK_STATE,
                DSL::remainder
        );
    }
}
