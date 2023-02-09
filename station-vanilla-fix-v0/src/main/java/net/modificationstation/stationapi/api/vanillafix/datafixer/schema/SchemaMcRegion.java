package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SchemaMcRegion extends Schema {

    private final boolean hasParent;

    public SchemaMcRegion(int versionKey, Schema parent) {
        super(versionKey, parent);
        hasParent = this.parent != null;
    }

    public static void targetItems(Schema schema, Map<String, Supplier<TypeTemplate>> map, String id) {
        schema.register(map, id, () -> DSL.optionalFields("Items", DSL.list(TypeReferences.ITEM_STACK.in(schema))));
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = hasParent ? super.registerEntities(schema) : new HashMap<>();
        schema.register(map, "Item", name -> DSL.optionalFields("Item", TypeReferences.ITEM_STACK.in(schema)));
        targetItems(schema, map, "Minecart");
        return map;
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = hasParent ? super.registerEntities(schema) : new HashMap<>();
        targetItems(schema, map, "Chest");
        targetItems(schema, map, "Trap");
        targetItems(schema, map, "Furnace");
        return map;
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        if (hasParent) super.registerTypes(schema, entityTypes, blockEntityTypes);
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
