package net.modificationstation.stationapi.api.common;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import org.jetbrains.annotations.NotNull;

@Getter
public class Library {

    @NotNull
    private final String url = Entrypoint.getNull();
    @NotNull
    private final String name = Entrypoint.getNull();
}
