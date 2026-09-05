package net.modificationstation.stationapi.api.block.context;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.tag.context.TagEvaluationContext;
import net.modificationstation.stationapi.api.util.context.Context;

public interface BlockTagContext extends BlockContext, TagEvaluationContext {
    BlockTagContext DEFAULT = of(TagEvaluationContext.DEFAULT);
    BlockTagContext BYPASSED = of(TagEvaluationContext.BYPASSED);

    static BlockTagContext of(BlockView view, BlockPos pos) {
        Context data = BlockContext.of(view, pos);
        interface BlockTagContextDelegate extends BlockTagContext, Delegate {}
        return (BlockTagContextDelegate) () -> data;
    }

    static BlockTagContext of(BlockView view, BlockPos pos, boolean ignoreTagConditions) {
        Context data = BlockContext.of(view, pos).with(TagEvaluationContext.of(ignoreTagConditions));
        interface BlockTagContextDelegate extends BlockTagContext, Delegate {}
        return (BlockTagContextDelegate) () -> data;
    }

    static BlockTagContext of(BlockView view, int x, int y, int z) {
        return of(view, new BlockPos(x, y, z));
    }

    static BlockTagContext of(BlockView view, int x, int y, int z, boolean ignoreTagConditions) {
        Context data = BlockContext.of(view, x, y, z).with(TagEvaluationContext.of(ignoreTagConditions));
        interface BlockTagContextDelegate extends BlockTagContext, Delegate {}
        return (BlockTagContextDelegate) () -> data;
    }

    static BlockTagContext of(int meta) {
        Context data = Context.of(BlockContext.BLOCK_METADATA_KEY, meta);
        interface BlockTagContextDelegate extends BlockTagContext, Delegate {}
        return (BlockTagContextDelegate) () -> data;
    }
    
    static BlockTagContext of(Context context) {
        if (context instanceof BlockTagContext b) return b;
        interface BlockTagContextDelegate extends BlockTagContext, Delegate {}
        return (BlockTagContextDelegate) () -> context;
    }

}
