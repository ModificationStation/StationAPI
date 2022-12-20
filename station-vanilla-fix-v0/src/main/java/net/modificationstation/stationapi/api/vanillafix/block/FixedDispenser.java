package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.projectile.Arrow;
import net.minecraft.entity.projectile.Egg;
import net.minecraft.entity.projectile.Snowball;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class FixedDispenser extends FixedBlockWithEntity {

    private final Random rand = new Random();

    public FixedDispenser(Identifier identifier) {
        super(identifier, Material.STONE);
        setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public int getTickrate() {
        return 4;
    }

    @Override
    public int getDropId(int i, Random random) {
        return Blocks.DISPENSER.id;
    }

    @Override
    public boolean canUse(Level arg, int i, int j, int k, PlayerBase arg2) {
        if (!arg.isServerSide) {
            TileEntityDispenser var6 = (TileEntityDispenser) arg.getTileEntity(i, j, k);
            arg2.openDispenserScreen(var6);
        }
        return true;
    }

    private void dispense(Level arg, int i, int j, int k, Random random) {
        Direction var6 = ((BlockStateView) arg).getBlockState(i, j, k).get(HORIZONTAL_FACING);
        byte var9 = 0;
        byte var10 = 0;
        switch (var6) {
            case WEST -> var10 = 1;
            case EAST -> var10 = -1;
            case SOUTH -> var9 = 1;
            default -> var9 = -1;
        }
        TileEntityDispenser var11 = (TileEntityDispenser)arg.getTileEntity(i, j, k);
        ItemInstance var12 = var11.getItemToDispense();
        double var13 = (double)i + (double)var9 * 0.6 + 0.5;
        double var15 = (double)j + 0.5;
        double var17 = (double)k + (double)var10 * 0.6 + 0.5;
        if (var12 == null) {
            arg.playLevelEvent(1001, i, j, k, 0);
        } else {
            if (var12.itemId == ItemBase.arrow.id) {
                Arrow var19 = new Arrow(arg, var13, var15, var17);
                var19.method_1291(var9, 0.10000000149011612, var10, 1.1F, 6.0F);
                var19.spawnedByPlayer = true;
                arg.spawnEntity(var19);
                arg.playLevelEvent(1002, i, j, k, 0);
            } else if (var12.itemId == ItemBase.egg.id) {
                Egg var22 = new Egg(arg, var13, var15, var17);
                var22.method_1682(var9, 0.10000000149011612, var10, 1.1F, 6.0F);
                arg.spawnEntity(var22);
                arg.playLevelEvent(1002, i, j, k, 0);
            } else if (var12.itemId == ItemBase.snowball.id) {
                Snowball var23 = new Snowball(arg, var13, var15, var17);
                var23.method_1656(var9, 0.10000000149011612, var10, 1.1F, 6.0F);
                arg.spawnEntity(var23);
                arg.playLevelEvent(1002, i, j, k, 0);
            } else {
                Item var24 = new Item(arg, var13, var15 - 0.3, var17, var12);
                double var20 = random.nextDouble() * 0.1 + 0.2;
                var24.velocityX = (double)var9 * var20;
                var24.velocityY = 0.20000000298023224;
                var24.velocityZ = (double)var10 * var20;
                var24.velocityX += random.nextGaussian() * 0.007499999832361937 * 6.0;
                var24.velocityY += random.nextGaussian() * 0.007499999832361937 * 6.0;
                var24.velocityZ += random.nextGaussian() * 0.007499999832361937 * 6.0;
                arg.spawnEntity(var24);
                arg.playLevelEvent(1000, i, j, k, 0);
            }
            arg.playLevelEvent(2000, i, j, k, var9 + 1 + (var10 + 1) * 3);
        }
    }

    @Override
    public void onAdjacentBlockUpdate(Level arg, int i, int j, int k, int l) {
        if (l > 0 && BlockBase.BY_ID[l].getEmitsRedstonePower()) {
            boolean var6 = arg.hasRedstonePower(i, j, k) || arg.hasRedstonePower(i, j + 1, k);
            if (var6) {
                arg.method_216(i, j, k, this.id, this.getTickrate());
            }
        }
    }

    @Override
    public void onScheduledTick(Level arg, int i, int j, int k, Random random) {
        if (arg.hasRedstonePower(i, j, k) || arg.hasRedstonePower(i, j + 1, k)) {
            this.dispense(arg, i, j, k, random);
        }
    }

    @Override
    protected TileEntityBase createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDispenser();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(HORIZONTAL_FACING, switch (MathHelper.floor((context.getPlayer().yaw * 4D / 360D) + 0.5D) & 3) {
            case 0 -> Direction.EAST;
            case 1 -> Direction.SOUTH;
            case 2 -> Direction.WEST;
            case 3 -> Direction.NORTH;
            default -> throw new IllegalStateException("Unexpected value: " + (MathHelper.floor((context.getPlayer().yaw * 4D / 360D) + 0.5D) & 3));
        });
    }

    public void onBlockRemoved(Level world, int x, int y, int z) {
        if (!((BlockStateView) world).getBlockState(x, y, z).isOf(this)) {
            TileEntityDispenser var5 = (TileEntityDispenser) world.getTileEntity(x, y, z);

            for (int var6 = 0; var6 < var5.getInventorySize(); ++var6) {
                ItemInstance var7 = var5.getInventoryItem(var6);
                if (var7 != null) {
                    float var8 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var9 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var10 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var7.count > 0) {
                        int var11 = this.rand.nextInt(21) + 10;
                        if (var11 > var7.count) {
                            var11 = var7.count;
                        }

                        var7.count -= var11;
                        Item var12 = new Item(world, (float) x + var8, (float) y + var9, (float) z + var10, new ItemInstance(var7.itemId, var11, var7.getDamage()));
                        float var13 = 0.05F;
                        var12.velocityX = (float) this.rand.nextGaussian() * var13;
                        var12.velocityY = (float) this.rand.nextGaussian() * var13 + 0.2F;
                        var12.velocityZ = (float) this.rand.nextGaussian() * var13;
                        world.spawnEntity(var12);
                    }
                }
            }
        }

        super.onBlockRemoved(world, x, y, z);
    }
}
