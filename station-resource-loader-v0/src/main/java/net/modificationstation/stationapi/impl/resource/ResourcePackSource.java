package net.modificationstation.stationapi.impl.resource;

import java.util.function.UnaryOperator;

public interface ResourcePackSource {
    UnaryOperator<String> NONE_SOURCE_TEXT_SUPPLIER = UnaryOperator.identity();
    ResourcePackSource NONE = ResourcePackSource.create(NONE_SOURCE_TEXT_SUPPLIER, true);
    ResourcePackSource BUILTIN = ResourcePackSource.create(ResourcePackSource.getSourceTextSupplier("pack.source.builtin"), true);
    ResourcePackSource FEATURE = ResourcePackSource.create(ResourcePackSource.getSourceTextSupplier("pack.source.feature"), false);
    ResourcePackSource WORLD = ResourcePackSource.create(ResourcePackSource.getSourceTextSupplier("pack.source.world"), true);
    ResourcePackSource SERVER = ResourcePackSource.create(ResourcePackSource.getSourceTextSupplier("pack.source.server"), true);

    String decorate(String var1);

    boolean canBeEnabledLater();

    static ResourcePackSource create(final UnaryOperator<String> sourceTextSupplier, final boolean canBeEnabledLater) {
        return new ResourcePackSource(){

            @Override
            public String decorate(String packName) {
                return sourceTextSupplier.apply(packName);
            }

            @Override
            public boolean canBeEnabledLater() {
                return canBeEnabledLater;
            }
        };
    }

    private static UnaryOperator<String> getSourceTextSupplier(String translationKey) {
//        MutableText text = Text.translatable(translationKey);
//        return name -> Text.translatable("pack.nameAndSource", name, text).formatted(Formatting.GRAY);
        return text -> "fixText";
    }
}

