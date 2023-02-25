package net.modificationstation.stationapi.api.vanillafix.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TaggedChoice;

import java.util.Locale;

public class ChoiceTypesFix extends DataFix {
    private final String name;
    private final DSL.TypeReference types;

    public ChoiceTypesFix(Schema outputSchema, String name, DSL.TypeReference types) {
        super(outputSchema, true);
        this.name = name;
        this.types = types;
    }

    @Override
    public TypeRewriteRule makeRule() {
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType = this.getInputSchema().findChoiceType(this.types);
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType2 = this.getOutputSchema().findChoiceType(this.types);
        return this.fixChoiceTypes(this.name, taggedChoiceType, taggedChoiceType2);
    }

    protected final <K> TypeRewriteRule fixChoiceTypes(String name, TaggedChoice.TaggedChoiceType<K> inputChoiceType, TaggedChoice.TaggedChoiceType<?> outputChoiceType) {
        if (inputChoiceType.getKeyType() != outputChoiceType.getKeyType()) {
            throw new IllegalStateException("Could not inject: key type is not the same");
        }
        //noinspection unchecked
        TaggedChoice.TaggedChoiceType<K> taggedChoiceType = (TaggedChoice.TaggedChoiceType<K>) outputChoiceType;
        return this.fixTypeEverywhere(name, inputChoiceType, taggedChoiceType, dynamicOps -> pair -> {
            if (!taggedChoiceType.hasType(pair.getFirst()))
                throw new IllegalArgumentException(String.format(Locale.ROOT, "Unknown type %s in %s ", pair.getFirst(), this.types));
            return pair;
        });
    }
}