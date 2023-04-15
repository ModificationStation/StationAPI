package net.modificationstation.stationapi.api.resource;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ResourceType {
    CLIENT_RESOURCES("assets"),
    SERVER_DATA("data");

    String directory;
}

