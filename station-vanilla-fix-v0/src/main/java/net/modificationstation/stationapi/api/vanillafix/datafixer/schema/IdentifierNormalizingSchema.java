package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.modificationstation.stationapi.api.util.Identifier;

public class IdentifierNormalizingSchema extends Schema {
    public static final PrimitiveCodec<String> CODEC = new PrimitiveCodec<>() {

        @Override
        public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).map(IdentifierNormalizingSchema::normalize);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, String string) {
            return dynamicOps.createString(string);
        }

        public String toString() {
            return "NamespacedString";
        }
    };
    private static final Type<String> IDENTIFIER_TYPE = new Const.PrimitiveType<String>(CODEC);

    public IdentifierNormalizingSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    public static String normalize(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier != null) return identifier.toString();
        return id;
    }

    public static Type<String> getIdentifierType() {
        return IDENTIFIER_TYPE;
    }

    @Override
    public Type<?> getChoiceType(DSL.TypeReference type, String choiceName) {
        return super.getChoiceType(type, IdentifierNormalizingSchema.normalize(choiceName));
    }
}

