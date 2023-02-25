package net.modificationstation.stationapi.api.vanillafix.datadamager.damage;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import java.util.function.ToIntFunction;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public abstract class StationFlatteningToMcRegionItemStackDamage extends DataFix {

    private final String name;

    public StationFlatteningToMcRegionItemStackDamage(Schema outputSchema, String name) {
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
                        dynamic.get(STATION_ID).result().<Dynamic<?>>map(
                                value -> dynamic
                                        .remove(STATION_ID)
                                        .set("id", dynamic.createShort((short) remap(value.asString(""))))
                        ).orElse(dynamic)
        );
    }

    protected abstract int remap(String id);

    public static DataFix create(Schema outputSchema, String name, final ToIntFunction<String> rename) {
        return new StationFlatteningToMcRegionItemStackDamage(outputSchema, name){

            @Override
            protected int remap(String id) {
                return rename.applyAsInt(id);
            }
        };
    }
}
