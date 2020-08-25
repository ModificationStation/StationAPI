package net.modificationstation.stationloader.impl.common.entity.player;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Sign;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationloader.api.common.entity.player.ClientPlayerAccessor;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            AbstractClientPlayer, PlayerBase, EnumStatus, NBTTagCompound,
//            Entity, Block, Material, ItemStack,
//            TileEntitySign, IInventory, TileEntityFurnace, TileEntityDispenser

public class PlayerAPI {

    public static List playerBaseClasses = new ArrayList();

    public PlayerAPI() {
    }

    public static void RegisterPlayerBase(Class pb) {
        playerBaseClasses.add(pb);
    }

    public static PlayerHandler getPlayerBase(AbstractClientPlayer player, Class pb) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (pb.isInstance(((ClientPlayerAccessor) player).getPlayerBases().get(i))) {
                return ((ClientPlayerAccessor) player).getPlayerBases().get(i);
            }
        }

        return null;
    }

    public static boolean onLivingUpdate(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).onLivingUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean respawn(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).respawn()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean moveFlying(AbstractClientPlayer player, float x, float y, float z) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).moveFlying(x, y, z)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean updatePlayerActionState(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).updatePlayerActionState()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean handleKeyPress(AbstractClientPlayer player, int j, boolean flag) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).handleKeyPress(j, flag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean writeEntityToNBT(AbstractClientPlayer player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).writeEntityBaseToNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean readEntityFromNBT(AbstractClientPlayer player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).readEntityBaseFromNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onExitGUI(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).onExitGUI()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean setEntityDead(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).setEntityBaseDead()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onDeath(AbstractClientPlayer player, EntityBase killer) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).onDeath(killer)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean attackEntityFrom(AbstractClientPlayer player, EntityBase attacker, int damage) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).attackEntityBaseFrom(attacker, damage)) {
                override = true;
            }
        }

        return override;
    }

    public static double getDistanceSq(AbstractClientPlayer player, double d, double d1, double d2, double answer) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            answer = ((ClientPlayerAccessor) player).getPlayerBases().get(i).getDistanceSq(d, d1, d2, answer);
        }

        return answer;
    }

    public static boolean isInWater(AbstractClientPlayer player, boolean inWater) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            inWater = ((ClientPlayerAccessor) player).getPlayerBases().get(i).isInWater(inWater);
        }

        return inWater;
    }

    public static boolean canTriggerWalking(AbstractClientPlayer player, boolean canTrigger) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            canTrigger = ((ClientPlayerAccessor) player).getPlayerBases().get(i).canTriggerWalking(canTrigger);
        }

        return canTrigger;
    }

    public static boolean heal(AbstractClientPlayer player, int j) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).heal(j)) {
                override = true;
            }
        }

        return override;
    }

    public static int getPlayerArmorValue(AbstractClientPlayer player, int armor) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            armor = ((ClientPlayerAccessor) player).getPlayerBases().get(i).getPlayerArmorValue(armor);
        }

        return armor;
    }

    public static float getCurrentPlayerStrVsBlock(AbstractClientPlayer player, BlockBase block, float f) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            f = ((ClientPlayerAccessor) player).getPlayerBases().get(i).getCurrentPlayerStrVsBlock(block, f);
        }

        return f;
    }

    public static boolean moveEntity(AbstractClientPlayer player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).moveEntityBase(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static SleepStatus sleepInBedAt(AbstractClientPlayer player, int x, int y, int z) {
        SleepStatus status = null;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            status = ((ClientPlayerAccessor) player).getPlayerBases().get(i).sleepInBedAt(x, y, z, status);
        }

        return status;
    }

    public static float getEntityBrightness(AbstractClientPlayer player, float f, float brightness) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            f = ((ClientPlayerAccessor) player).getPlayerBases().get(i).getEntityBaseBrightness(f, brightness);
        }

        return f;
    }

    public static boolean pushOutOfBlocks(AbstractClientPlayer player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).pushOutOfBlocks(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onUpdate(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).onUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static void afterUpdate(AbstractClientPlayer player) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            ((ClientPlayerAccessor) player).getPlayerBases().get(i).afterUpdate();
        }

    }

    public static boolean moveEntityWithHeading(AbstractClientPlayer player, float f, float f1) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).moveEntityBaseWithHeading(f, f1)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean isOnLadder(AbstractClientPlayer player, boolean onLadder) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            onLadder = ((ClientPlayerAccessor) player).getPlayerBases().get(i).isOnLadder(onLadder);
        }

        return onLadder;
    }

    public static boolean isInsideOfMaterial(AbstractClientPlayer player, Material material, boolean inMaterial) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            inMaterial = ((ClientPlayerAccessor) player).getPlayerBases().get(i).isInsideOfMaterial(material, inMaterial);
        }

        return inMaterial;
    }

    public static boolean isSneaking(AbstractClientPlayer player, boolean sneaking) {
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            sneaking = ((ClientPlayerAccessor) player).getPlayerBases().get(i).isSneaking(sneaking);
        }

        return sneaking;
    }

    public static boolean dropCurrentItem(AbstractClientPlayer player) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).dropCurrentItem()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean dropPlayerItem(AbstractClientPlayer player, ItemInstance itemstack) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).dropPlayerItem(itemstack)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIEditSign(AbstractClientPlayer player, Sign sign) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).displayGUIEditSign(sign)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIChest(AbstractClientPlayer player, InventoryBase inventory) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).displayGUIChest(inventory)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayWorkbenchGUI(AbstractClientPlayer player, int i, int j, int k) {
        boolean override = false;
        for (int n = 0; n < ((ClientPlayerAccessor) player).getPlayerBases().size(); n++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(n).displayWorkbenchGUI(i, j, k)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIFurnace(AbstractClientPlayer player, TileEntityFurnace furnace) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).displayGUIFurnace(furnace)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIDispenser(AbstractClientPlayer player, TileEntityDispenser dispenser) {
        boolean override = false;
        for (int i = 0; i < ((ClientPlayerAccessor) player).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) player).getPlayerBases().get(i).displayGUIDispenser(dispenser)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean sendChatMessage(AbstractClientPlayer AbstractClientPlayer, String s) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).sendChatMessage(s)) {
                flag = true;
            }
        }

        return flag;
    }

    public static String getHurtSound(AbstractClientPlayer AbstractClientPlayer) {
        String result = null;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            String baseResult = ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).getHurtSound(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean canHarvestBlock(AbstractClientPlayer AbstractClientPlayer, BlockBase block) {
        Boolean result = null;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            Boolean baseResult = ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).canHarvestBlock(block, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean fall(AbstractClientPlayer AbstractClientPlayer, float f) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).fall(f)) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean jump(AbstractClientPlayer AbstractClientPlayer) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).jump()) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean damageEntity(AbstractClientPlayer AbstractClientPlayer, int i1) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).damageEntityBase(i1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Double getDistanceSqToEntity(AbstractClientPlayer AbstractClientPlayer, EntityBase entity) {
        Double result = null;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            Double baseResult = ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).getDistanceSqToEntityBase(entity, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean attackTargetEntityWithCurrentItem(AbstractClientPlayer AbstractClientPlayer, EntityBase entity) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).attackTargetEntityBaseWithCurrentItem(entity)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Boolean handleWaterMovement(AbstractClientPlayer AbstractClientPlayer) {
        Boolean result = null;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            Boolean baseResult = ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).handleWaterMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean handleLavaMovement(AbstractClientPlayer AbstractClientPlayer) {
        Boolean result = null;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            Boolean baseResult = ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).handleLavaMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean dropPlayerItemWithRandomChoice(AbstractClientPlayer AbstractClientPlayer, ItemInstance itemstack, boolean flag1) {
        boolean flag = false;
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            if (((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static void beforeUpdate(AbstractClientPlayer AbstractClientPlayer) {
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).beforeUpdate();
        }

    }

    public static void beforeMoveEntity(AbstractClientPlayer AbstractClientPlayer, double d, double d1, double d2) {
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).beforeMoveEntityBase(d, d1, d2);
        }

    }

    public static void afterMoveEntity(AbstractClientPlayer AbstractClientPlayer, double d, double d1, double d2) {
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).afterMoveEntityBase(d, d1, d2);
        }

    }

    public static void beforeSleepInBedAt(AbstractClientPlayer AbstractClientPlayer, int i1, int j, int k) {
        for (int i = 0; i < ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().size(); i++) {
            ((ClientPlayerAccessor) AbstractClientPlayer).getPlayerBases().get(i).beforeSleepInBedAt(i1, j, k);
        }

    }

}
