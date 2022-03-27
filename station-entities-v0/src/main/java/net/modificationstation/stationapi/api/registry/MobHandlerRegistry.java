package net.modificationstation.stationapi.api.registry;

import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class MobHandlerRegistry extends Registry<Function<Level, Living>> {

    public static final MobHandlerRegistry INSTANCE = new MobHandlerRegistry(Identifier.of(MODID, "mob_handlers"));

    /**
     * Default registry constructor.
     *
     * @param identifier registry's identifier.
     */
    private MobHandlerRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }
}
