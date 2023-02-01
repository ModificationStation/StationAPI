package net.modificationstation.stationapi.api.datafixer;

import com.mojang.datafixers.DSL;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypeReferences {

    public static final DSL.TypeReference LEVEL = () -> "level";
    public static final DSL.TypeReference PLAYER = () -> "player";
    public static final DSL.TypeReference CHUNK = () -> "chunk";
    public static final DSL.TypeReference BLOCK_ENTITY = () -> "block_entity";
    public static final DSL.TypeReference ITEM_STACK = () -> "item_stack";
    public static final DSL.TypeReference BLOCK_STATE = () -> "block_state";
    public static final DSL.TypeReference ENTITY = () -> "entity";
    public static final DSL.TypeReference ITEM_NAME = () -> "item_name";
}
