package net.modificationstation.stationapi.impl.resource.loader;

import net.modificationstation.stationapi.impl.resource.ResourcePackSource;

public class BuiltinModResourcePackSource implements ResourcePackSource {
    private final String modId;

    public BuiltinModResourcePackSource(String modId) {
        this.modId = modId;
    }

    @Override
    public boolean canBeEnabledLater() {
        return true;
    }

//  @Override
//  public Text decorate(Text packName) {
//      return Text.translatable("pack.nameAndSource", packName, Text.translatable("pack.source.builtinMod", modId)).formatted(Formatting.GRAY);
//  }

    @Override
    public String decorate(String packName) {
        return "fixText" + modId;
    }
}
