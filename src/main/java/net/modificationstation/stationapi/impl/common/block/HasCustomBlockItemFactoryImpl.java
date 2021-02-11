package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.*;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.IntFunction;

/**
 * {@link IHasCustomBlockItemFactory} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public class HasCustomBlockItemFactoryImpl implements BlockItemFactoryProvider {

    /**
     * Processes {@link HasCustomBlockItemFactory} annotation if present via {@link BlockItemFactoryProvider} hook.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return new or current factory.
     */
    @Override
    public IntFunction<PlaceableTileEntity> getBlockItemFactory(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
        if (block instanceof IHasCustomBlockItemFactory)
            currentFactory = ((IHasCustomBlockItemFactory) block).getBlockItemFactory();
        Class<? extends BlockBase> blockClass = block.getClass();
        if (blockClass.isAnnotationPresent(HasCustomBlockItemFactory.class)) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                //noinspection unchecked
                currentFactory = (IntFunction<PlaceableTileEntity>) LambdaMetafactory.metafactory(
                        lookup,
                        "apply",
                        MethodType.methodType(IntFunction.class),
                        MethodType.methodType(Object.class, int.class),
                        lookup.findConstructor(blockClass.getAnnotation(HasCustomBlockItemFactory.class).value(), MethodType.methodType(void.class, int.class)),
                        MethodType.methodType(PlaceableTileEntity.class, int.class)
                ).getTarget().invokeExact();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return currentFactory;
    }
}
