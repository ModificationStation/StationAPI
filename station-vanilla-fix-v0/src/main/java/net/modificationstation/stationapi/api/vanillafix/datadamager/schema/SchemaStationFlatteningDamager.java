package net.modificationstation.stationapi.api.vanillafix.datadamager.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.IdentifierNormalizingSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager.SECTIONS;
import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class SchemaStationFlatteningDamager extends Schema {

    public SchemaStationFlatteningDamager(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    public static void targetItems(Schema schema, Map<String, Supplier<TypeTemplate>> map, String id) {
        schema.register(map, id, () -> DSL.optionalFields("Items", DSL.list(TypeReferences.ITEM_STACK.in(schema))));
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = new HashMap<>();
        schema.register(map, "Item", name -> DSL.optionalFields("Item", TypeReferences.ITEM_STACK.in(schema)));
        targetItems(schema, map, "Minecart");
        return map;
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = new HashMap<>();
        targetItems(schema, map, "Chest");
        targetItems(schema, map, "Trap");
        targetItems(schema, map, "Furnace");
        return map;
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        schema.registerType(
                false,
                TypeReferences.LEVEL,
                DSL::remainder
        );
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
                                DSL.list(TypeReferences.BLOCK_ENTITY.in(schema)),
                                SECTIONS,
                                DSL.list(DSL.optionalFields("block_states", DSL.optionalFields("palette", DSL.list(TypeReferences.BLOCK_STATE.in(schema)))))
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
                        STATION_ID,
                        TypeReferences.ITEM_NAME.in(schema)
                )
        );
        schema.registerType(
                false,
                TypeReferences.ITEM_NAME,
                () -> DSL.constType(IdentifierNormalizingSchema.getIdentifierType())
        );
        schema.registerType(
                false,
                TypeReferences.BLOCK_STATE,
                DSL::remainder
        );
    }
}
