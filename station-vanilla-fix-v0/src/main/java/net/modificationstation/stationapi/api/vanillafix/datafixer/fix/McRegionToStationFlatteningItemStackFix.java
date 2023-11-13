package net.modificationstation.stationapi.api.vanillafix.datafixer.fix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public  class McRegionToStationFlatteningItemStackFix extends DataFix {
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
                                        .set(STATION_ID, dynamic.createString(StationFlatteningItemStackSchema.lookupItem(value.asShort((short) 0))))
                        ).orElse(dynamic)
        );
    }
}

