package net.modificationstation.stationapi.api.util.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrashReportSectionBlockState {

    public static void addBlockInfo(CrashReportSection element, BlockView world, TilePos pos, @Nullable BlockState state) {
        if (state != null) {
            element.add("Block", state::toString);
        }
        element.add("Block location", () -> CrashReportSection.createPositionString(world, pos));
    }
}
