package net.modificationstation.stationapi.api.vanillafix.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.Optional;
import java.util.function.IntFunction;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public abstract class McRegionToStationFlatteningItemStackFix extends DataFix {

    private final String name;

    public McRegionToStationFlatteningItemStackFix(Schema outputSchema, String name) {
        super(outputSchema, true);
        this.name = name;
    }

    @Override
    public TypeRewriteRule makeRule() {
        OpticFinder<Short> idFinder = DSL.fieldFinder("id", DSL.shortType());
        Type<?> newType = getOutputSchema().getType(TypeReferences.ITEM_STACK);
        return fixTypeEverywhereTyped(
                name,
                getInputSchema().getType(TypeReferences.ITEM_STACK),
                typed -> {
                    Optional<Short> id = typed.getOptional(idFinder);
                    if (id.isEmpty()) return typed;
                    Dynamic<?> dynamic = typed.get(DSL.remainderFinder());
                    return typed.set(DSL.remainderFinder(), dynamic.set(STATION_ID, dynamic.createString(remap(id.get()))).remove("id"));
                }
        );
//        return writeFixAndRead(
//                name,
//                getInputSchema().getType(TypeReferences.ITEM_STACK),
//                getOutputSchema().getType(TypeReferences.ITEM_STACK),
//                dynamic ->
//                        dynamic.get("id").result().<Dynamic<?>>map(
//                                value -> dynamic
//                                        .remove("id")
//                                        .set(STATION_ID, dynamic.createString(remap(value.asInt(0))))
//                        ).orElse(dynamic)
//        );
    }

    protected abstract String remap(int id);

    public static DataFix create(Schema outputSchema, String name, final IntFunction<String> remap) {
        return new McRegionToStationFlatteningItemStackFix(outputSchema, name){

            @Override
            protected String remap(int id) {
                return remap.apply(id);
            }
        };
    }
}

