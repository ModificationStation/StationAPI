package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MatrixStack;

import java.util.Random;

public interface InputContext {

    BlockView blockView();

    MatrixStack matrixStack();

    Random random();


}
