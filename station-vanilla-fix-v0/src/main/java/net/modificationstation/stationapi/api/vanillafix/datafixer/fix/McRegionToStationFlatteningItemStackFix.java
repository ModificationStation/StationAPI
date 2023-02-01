package net.modificationstation.stationapi.api.vanillafix.datafixer.fix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public abstract class McRegionToStationFlatteningItemStackFix extends DataFix {

    private final String name;

    public McRegionToStationFlatteningItemStackFix(Schema outputSchema, String name) {
        super(outputSchema, true);
        this.name = name;
    }

    @Override
    public TypeRewriteRule makeRule() {
        return writeFixAndRead(
                name,
                getInputSchema().getType(TypeReferences.ITEM_STACK),
                getOutputSchema().getType(TypeReferences.ITEM_STACK),
                dynamic ->
                        dynamic.get("id").result().<Dynamic<?>>map(
                                value -> dynamic
                                        .remove("id")
                                        .set(STATION_ID, dynamic.createString(rename(value.asInt(0))))
                        ).orElse(dynamic)
        );
    }

    protected abstract String rename(int id);

    public static DataFix create(Schema outputSchema, String name, final Int2ObjectFunction<String> rename) {
        return new McRegionToStationFlatteningItemStackFix(outputSchema, name){

            @Override
            protected String rename(int id) {
                return rename.get(id);
            }
        };
    }
}

