/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Holds, manages and provides access to the block/world related state
 * needed by fallback and mesh consumers.
 *
 * <p>Exception: per-block position offsets are tracked in {@link ChunkRenderInfo}
 * so they can be applied together with chunk offsets.
 */
public class BlockRenderInfo {
    private final BlockColors blockColorMap = StationRenderAPI.getBlockColors();
    public final Random random = new Random();
    public BlockView blockView;
    public BlockPos blockPos;
    public BlockState blockState;
    public long seed;
    boolean defaultAo;

    public final Supplier<Random> randomSupplier = () -> {
        final Random result = random;
        long seed = this.seed;

        if (seed == -1L) {
            seed = blockState.getRenderingSeed(blockPos);
            this.seed = seed;
        }

        result.setSeed(seed);
        return result;
    };

    public void setBlockView(BlockView blockView) {
        this.blockView = blockView;
    }

    public void prepareForBlock(BlockState blockState, BlockPos blockPos, boolean modelAO) {
        this.blockPos = blockPos;
        this.blockState = blockState;
        // in the unlikely case seed actually matches this, we'll simply retrieve it more than one
        seed = -1L;
        defaultAo = modelAO && Minecraft.method_2148() && blockState.getLuminance() == 0;
    }

    public void release() {
        blockPos = null;
        blockState = null;
    }

    int blockColour(int colorIndex) {
        return 0xFF000000 | blockColorMap.getColor(blockState, blockView, blockPos, colorIndex);
    }

    boolean shouldDrawFace(Direction face) {
        return true;
    }

    boolean shouldDrawQuad(MutableQuadViewImpl quad) {
        Direction cull = quad.cullFace();
        return cull == null || blockState.getBlock().isSideVisible(blockView, blockPos.x + cull.getOffsetX(), blockPos.y + cull.getOffsetY(), blockPos.z + cull.getOffsetZ(), cull.ordinal());
    }
}
