package net.modificationstation.stationapi.impl.entity.player;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Sign;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHandlerContainer;

public class PlayerAPI {

    public static PlayerHandler getPlayerHandler(PlayerBase player, Class<? extends PlayerHandler> pb) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (pb.isInstance(((PlayerHandlerContainer) player).getPlayerBases().get(i))) {
                return ((PlayerHandlerContainer) player).getPlayerBases().get(i);
            }
        }
        return null;
    }

    public static boolean onLivingUpdate(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).onLivingUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean respawn(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).respawn()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean moveFlying(PlayerBase player, float x, float y, float z) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).moveFlying(x, y, z)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean updatePlayerActionState(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).updatePlayerActionState()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean handleKeyPress(PlayerBase player, int j, boolean flag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).handleKeyPress(j, flag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean writeEntityToNBT(PlayerBase player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).writeEntityBaseToNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean readEntityFromNBT(PlayerBase player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).readEntityBaseFromNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onExitGUI(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).onExitGUI()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean setEntityDead(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).setEntityBaseDead()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onDeath(PlayerBase player, EntityBase killer) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).onDeath(killer)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean attackEntityFrom(PlayerBase player, EntityBase attacker, int damage) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).attackEntityBaseFrom(attacker, damage)) {
                override = true;
            }
        }

        return override;
    }

    public static double getDistanceSq(PlayerBase player, double d, double d1, double d2, double answer) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            answer = ((PlayerHandlerContainer) player).getPlayerBases().get(i).getDistanceSq(d, d1, d2, answer);
        }

        return answer;
    }

    public static boolean isInWater(PlayerBase player, boolean inWater) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            inWater = ((PlayerHandlerContainer) player).getPlayerBases().get(i).isInWater(inWater);
        }

        return inWater;
    }

    public static boolean canTriggerWalking(PlayerBase player, boolean canTrigger) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            canTrigger = ((PlayerHandlerContainer) player).getPlayerBases().get(i).canTriggerWalking(canTrigger);
        }

        return canTrigger;
    }

    public static boolean heal(PlayerBase player, int j) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).heal(j)) {
                override = true;
            }
        }

        return override;
    }

    public static int getPlayerArmorValue(PlayerBase player, int armor) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            armor = ((PlayerHandlerContainer) player).getPlayerBases().get(i).getPlayerArmorValue(armor);
        }

        return armor;
    }

    public static float getCurrentPlayerStrVsBlock(PlayerBase player, BlockBase block, float f) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerBases().get(i).getCurrentPlayerStrVsBlock(block, f);
        }

        return f;
    }

    public static boolean moveEntity(PlayerBase player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).moveEntityBase(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static SleepStatus sleepInBedAt(PlayerBase player, int x, int y, int z) {
        SleepStatus status = null;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            status = ((PlayerHandlerContainer) player).getPlayerBases().get(i).sleepInBedAt(x, y, z, status);
        }

        return status;
    }

    public static float getEntityBrightness(PlayerBase player, float f, float brightness) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerBases().get(i).getEntityBaseBrightness(f, brightness);
        }

        return f;
    }

    public static boolean pushOutOfBlocks(PlayerBase player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).pushOutOfBlocks(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onUpdate(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).onUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static void afterUpdate(PlayerBase player) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            ((PlayerHandlerContainer) player).getPlayerBases().get(i).afterUpdate();
        }

    }

    public static boolean moveEntityWithHeading(PlayerBase player, float f, float f1) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).moveEntityBaseWithHeading(f, f1)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean isOnLadder(PlayerBase player, boolean onLadder) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            onLadder = ((PlayerHandlerContainer) player).getPlayerBases().get(i).isOnLadder(onLadder);
        }

        return onLadder;
    }

    public static boolean isInsideOfMaterial(PlayerBase player, Material material, boolean inMaterial) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            inMaterial = ((PlayerHandlerContainer) player).getPlayerBases().get(i).isInsideOfMaterial(material, inMaterial);
        }

        return inMaterial;
    }

    public static boolean isSneaking(PlayerBase player, boolean sneaking) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            sneaking = ((PlayerHandlerContainer) player).getPlayerBases().get(i).isSneaking(sneaking);
        }

        return sneaking;
    }

    public static boolean dropCurrentItem(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).dropCurrentItem()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean dropPlayerItem(PlayerBase player, ItemInstance itemstack) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).dropPlayerItem(itemstack)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIEditSign(PlayerBase player, Sign sign) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).displayGUIEditSign(sign)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIChest(PlayerBase player, InventoryBase inventory) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).displayGUIChest(inventory)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayWorkbenchGUI(PlayerBase player, int i, int j, int k) {
        boolean override = false;
        for (int n = 0; n < ((PlayerHandlerContainer) player).getPlayerBases().size(); n++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(n).displayWorkbenchGUI(i, j, k)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIFurnace(PlayerBase player, TileEntityFurnace furnace) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).displayGUIFurnace(furnace)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIDispenser(PlayerBase player, TileEntityDispenser dispenser) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerBases().get(i).displayGUIDispenser(dispenser)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean sendChatMessage(PlayerBase PlayerBase, String s) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).sendChatMessage(s)) {
                flag = true;
            }
        }

        return flag;
    }

    public static String getHurtSound(PlayerBase PlayerBase) {
        String result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            String baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).getHurtSound(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean canHarvestBlock(PlayerBase PlayerBase, BlockBase block) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).canHarvestBlock(block, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean fall(PlayerBase PlayerBase, float f) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).fall(f)) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean jump(PlayerBase PlayerBase) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).jump()) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean damageEntity(PlayerBase PlayerBase, int i1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).damageEntityBase(i1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Double getDistanceSqToEntity(PlayerBase PlayerBase, EntityBase entity) {
        Double result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            Double baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).getDistanceSqToEntityBase(entity, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean attackTargetEntityWithCurrentItem(PlayerBase PlayerBase, EntityBase entity) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).attackTargetEntityBaseWithCurrentItem(entity)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Boolean handleWaterMovement(PlayerBase PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).handleWaterMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean handleLavaMovement(PlayerBase PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).handleLavaMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean dropPlayerItemWithRandomChoice(PlayerBase PlayerBase, ItemInstance itemstack, boolean flag1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static void beforeUpdate(PlayerBase PlayerBase) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).beforeUpdate();
        }

    }

    public static void beforeMoveEntity(PlayerBase PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).beforeMoveEntityBase(d, d1, d2);
        }

    }

    public static void afterMoveEntity(PlayerBase PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).afterMoveEntityBase(d, d1, d2);
        }

    }

    public static void beforeSleepInBedAt(PlayerBase PlayerBase, int i1, int j, int k) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerBases().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerBases().get(i).beforeSleepInBedAt(i1, j, k);
        }

    }

}
