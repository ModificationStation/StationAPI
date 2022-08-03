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
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (pb.isInstance(((PlayerHandlerContainer) player).getPlayerHandlers().get(i))) {
                return ((PlayerHandlerContainer) player).getPlayerHandlers().get(i);
            }
        }
        return null;
    }

    public static boolean onLivingUpdate(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onLivingUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean respawn(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).respawn()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean moveFlying(PlayerBase player, float x, float y, float z) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveFlying(x, y, z)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean updatePlayerActionState(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).updatePlayerActionState()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean handleKeyPress(PlayerBase player, int j, boolean flag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).handleKeyPress(j, flag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean writeEntityToNBT(PlayerBase player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).writeEntityBaseToNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean readEntityFromNBT(PlayerBase player, CompoundTag tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).readEntityBaseFromNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onExitGUI(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onExitGUI()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean setEntityDead(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).setEntityBaseDead()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onDeath(PlayerBase player, EntityBase killer) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onDeath(killer)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean attackEntityFrom(PlayerBase player, EntityBase attacker, int damage) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).attackEntityBaseFrom(attacker, damage)) {
                override = true;
            }
        }

        return override;
    }

    public static double getDistanceSq(PlayerBase player, double d, double d1, double d2, double answer) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            answer = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getDistanceSq(d, d1, d2, answer);
        }

        return answer;
    }

    public static boolean isInWater(PlayerBase player, boolean inWater) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            inWater = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isInWater(inWater);
        }

        return inWater;
    }

    public static boolean canTriggerWalking(PlayerBase player, boolean canTrigger) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            canTrigger = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).canTriggerWalking(canTrigger);
        }

        return canTrigger;
    }

    public static boolean heal(PlayerBase player, int j) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).heal(j)) {
                override = true;
            }
        }

        return override;
    }

    public static int getPlayerArmorValue(PlayerBase player, int armor) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            armor = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getPlayerArmorValue(armor);
        }

        return armor;
    }

    public static float getCurrentPlayerStrVsBlock(PlayerBase player, BlockBase block, float f) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getCurrentPlayerStrVsBlock(block, f);
        }

        return f;
    }

    public static boolean moveEntity(PlayerBase player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveEntityBase(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static SleepStatus sleepInBedAt(PlayerBase player, int x, int y, int z) {
        SleepStatus status = null;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            status = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).sleepInBedAt(x, y, z, status);
        }

        return status;
    }

    public static float getEntityBrightness(PlayerBase player, float f, float brightness) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getEntityBaseBrightness(f, brightness);
        }

        return f;
    }

    public static boolean pushOutOfBlocks(PlayerBase player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).pushOutOfBlocks(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onUpdate(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static void afterUpdate(PlayerBase player) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).afterUpdate();
        }

    }

    public static boolean moveEntityWithHeading(PlayerBase player, float f, float f1) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveEntityBaseWithHeading(f, f1)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean isOnLadder(PlayerBase player, boolean onLadder) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            onLadder = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isOnLadder(onLadder);
        }

        return onLadder;
    }

    public static boolean isInsideOfMaterial(PlayerBase player, Material material, boolean inMaterial) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            inMaterial = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isInsideOfMaterial(material, inMaterial);
        }

        return inMaterial;
    }

    public static boolean isSneaking(PlayerBase player, boolean sneaking) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            sneaking = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isSneaking(sneaking);
        }

        return sneaking;
    }

    public static boolean dropCurrentItem(PlayerBase player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).dropCurrentItem()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean dropPlayerItem(PlayerBase player, ItemInstance itemstack) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).dropPlayerItem(itemstack)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIEditSign(PlayerBase player, Sign sign) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIEditSign(sign)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIChest(PlayerBase player, InventoryBase inventory) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIChest(inventory)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayWorkbenchGUI(PlayerBase player, int i, int j, int k) {
        boolean override = false;
        for (int n = 0; n < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); n++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(n).displayWorkbenchGUI(i, j, k)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIFurnace(PlayerBase player, TileEntityFurnace furnace) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIFurnace(furnace)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIDispenser(PlayerBase player, TileEntityDispenser dispenser) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIDispenser(dispenser)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean sendChatMessage(PlayerBase PlayerBase, String s) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).sendChatMessage(s)) {
                flag = true;
            }
        }

        return flag;
    }

    public static String getHurtSound(PlayerBase PlayerBase) {
        String result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            String baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).getHurtSound(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean canHarvestBlock(PlayerBase PlayerBase, BlockBase block) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).canHarvestBlock(block, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean fall(PlayerBase PlayerBase, float f) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).fall(f)) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean jump(PlayerBase PlayerBase) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).jump()) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean damageEntity(PlayerBase PlayerBase, int i1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).damageEntityBase(i1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Double getDistanceSqToEntity(PlayerBase PlayerBase, EntityBase entity) {
        Double result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Double baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).getDistanceSqToEntityBase(entity, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean attackTargetEntityWithCurrentItem(PlayerBase PlayerBase, EntityBase entity) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).attackTargetEntityBaseWithCurrentItem(entity)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Boolean handleWaterMovement(PlayerBase PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).handleWaterMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean handleLavaMovement(PlayerBase PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).handleLavaMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean dropPlayerItemWithRandomChoice(PlayerBase PlayerBase, ItemInstance itemstack, boolean flag1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static void beforeUpdate(PlayerBase PlayerBase) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeUpdate();
        }

    }

    public static void beforeMoveEntity(PlayerBase PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeMoveEntityBase(d, d1, d2);
        }

    }

    public static void afterMoveEntity(PlayerBase PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).afterMoveEntityBase(d, d1, d2);
        }

    }

    public static void beforeSleepInBedAt(PlayerBase PlayerBase, int i1, int j, int k) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeSleepInBedAt(i1, j, k);
        }

    }

}
