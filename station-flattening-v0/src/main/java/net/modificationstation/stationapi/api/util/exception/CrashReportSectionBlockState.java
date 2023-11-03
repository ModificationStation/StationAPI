package net.modificationstation.stationapi.api.util.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrashReportSectionBlockState {

    public static void addBlockInfo(CrashReportSection element, BlockView world, BlockPos pos, @Nullable BlockState state) {
        if (state != null) {
            element.add("Block", state::toString);
        }
        element.add("Block location", () -> CrashReportSection.createPositionString(world, pos));
    }
}
