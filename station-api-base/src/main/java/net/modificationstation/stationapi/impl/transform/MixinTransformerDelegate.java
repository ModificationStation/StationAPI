package net.modificationstation.stationapi.impl.transform;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.transformers.TreeTransformer;

@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class MixinTransformerDelegate<T extends TreeTransformer & IMixinTransformer> extends TreeTransformer implements IMixinTransformer {
    @Delegate(types = { TreeTransformer.class, IMixinTransformer.class }) T delegate;
}
