package net.modificationstation.stationapi.impl.entity.player;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.class_141;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHandlerContainer;

public class PlayerAPI {

    public static PlayerHandler getPlayerHandler(PlayerEntity player, Class<? extends PlayerHandler> pb) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (pb.isInstance(((PlayerHandlerContainer) player).getPlayerHandlers().get(i))) {
                return ((PlayerHandlerContainer) player).getPlayerHandlers().get(i);
            }
        }
        return null;
    }

    public static boolean onLivingUpdate(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onLivingUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean respawn(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).respawn()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean moveFlying(PlayerEntity player, float x, float y, float z) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveFlying(x, y, z)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean updatePlayerActionState(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).updatePlayerActionState()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean handleKeyPress(PlayerEntity player, int j, boolean flag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).handleKeyPress(j, flag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean writeEntityToNBT(PlayerEntity player, NbtCompound tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).writeEntityBaseToNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean readEntityFromNBT(PlayerEntity player, NbtCompound tag) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).readEntityBaseFromNBT(tag)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onExitGUI(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onExitGUI()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean setEntityDead(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).setEntityBaseDead()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onDeath(PlayerEntity player, Entity killer) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onDeath(killer)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean attackEntityFrom(PlayerEntity player, Entity attacker, int damage) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).attackEntityBaseFrom(attacker, damage)) {
                override = true;
            }
        }

        return override;
    }

    public static double getDistanceSq(PlayerEntity player, double d, double d1, double d2, double answer) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            answer = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getDistanceSq(d, d1, d2, answer);
        }

        return answer;
    }

    public static boolean isInWater(PlayerEntity player, boolean inWater) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            inWater = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isInWater(inWater);
        }

        return inWater;
    }

    public static boolean canTriggerWalking(PlayerEntity player, boolean canTrigger) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            canTrigger = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).canTriggerWalking(canTrigger);
        }

        return canTrigger;
    }

    public static boolean heal(PlayerEntity player, int j) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).heal(j)) {
                override = true;
            }
        }

        return override;
    }

    public static int getPlayerArmorValue(PlayerEntity player, int armor) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            armor = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getPlayerArmorValue(armor);
        }

        return armor;
    }

    public static float getCurrentPlayerStrVsBlock(PlayerEntity player, Block block, float f) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getCurrentPlayerStrVsBlock(block, f);
        }

        return f;
    }

    public static boolean moveEntity(PlayerEntity player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveEntityBase(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static class_141 sleepInBedAt(PlayerEntity player, int x, int y, int z) {
        class_141 status = null;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            status = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).sleepInBedAt(x, y, z, status);
        }

        return status;
    }

    public static float getEntityBrightness(PlayerEntity player, float f, float brightness) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            f = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).getEntityBaseBrightness(f, brightness);
        }

        return f;
    }

    public static boolean pushOutOfBlocks(PlayerEntity player, double d, double d1, double d2) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).pushOutOfBlocks(d, d1, d2)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean onUpdate(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).onUpdate()) {
                override = true;
            }
        }

        return override;
    }

    public static void afterUpdate(PlayerEntity player) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).afterUpdate();
        }

    }

    public static boolean moveEntityWithHeading(PlayerEntity player, float f, float f1) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).moveEntityBaseWithHeading(f, f1)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean isOnLadder(PlayerEntity player, boolean onLadder) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            onLadder = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isOnLadder(onLadder);
        }

        return onLadder;
    }

    public static boolean isInsideOfMaterial(PlayerEntity player, Material material, boolean inMaterial) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            inMaterial = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isInsideOfMaterial(material, inMaterial);
        }

        return inMaterial;
    }

    public static boolean isSneaking(PlayerEntity player, boolean sneaking) {
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            sneaking = ((PlayerHandlerContainer) player).getPlayerHandlers().get(i).isSneaking(sneaking);
        }

        return sneaking;
    }

    public static boolean dropCurrentItem(PlayerEntity player) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).dropCurrentItem()) {
                override = true;
            }
        }

        return override;
    }

    public static boolean dropPlayerItem(PlayerEntity player, ItemStack itemstack) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).dropPlayerItem(itemstack)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIEditSign(PlayerEntity player, SignBlock sign) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIEditSign(sign)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIChest(PlayerEntity player, Inventory inventory) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIChest(inventory)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayWorkbenchGUI(PlayerEntity player, int i, int j, int k) {
        boolean override = false;
        for (int n = 0; n < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); n++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(n).displayWorkbenchGUI(i, j, k)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIFurnace(PlayerEntity player, FurnaceBlockEntity furnace) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIFurnace(furnace)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean displayGUIDispenser(PlayerEntity player, DispenserBlockEntity dispenser) {
        boolean override = false;
        for (int i = 0; i < ((PlayerHandlerContainer) player).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) player).getPlayerHandlers().get(i).displayGUIDispenser(dispenser)) {
                override = true;
            }
        }

        return override;
    }

    public static boolean sendChatMessage(PlayerEntity PlayerBase, String s) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).sendChatMessage(s)) {
                flag = true;
            }
        }

        return flag;
    }

    public static String getHurtSound(PlayerEntity PlayerBase) {
        String result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            String baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).getHurtSound(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean canHarvestBlock(PlayerEntity PlayerBase, Block block) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).canHarvestBlock(block, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean fall(PlayerEntity PlayerBase, float f) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).fall(f)) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean jump(PlayerEntity PlayerBase) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).jump()) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean damageEntity(PlayerEntity PlayerBase, int i1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).damageEntityBase(i1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Double getDistanceSqToEntity(PlayerEntity PlayerBase, Entity entity) {
        Double result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Double baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).getDistanceSqToEntityBase(entity, result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean attackTargetEntityWithCurrentItem(PlayerEntity PlayerBase, Entity entity) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).attackTargetEntityBaseWithCurrentItem(entity)) {
                flag = true;
            }
        }

        return flag;
    }

    public static Boolean handleWaterMovement(PlayerEntity PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).handleWaterMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static Boolean handleLavaMovement(PlayerEntity PlayerBase) {
        Boolean result = null;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            Boolean baseResult = ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).handleLavaMovement(result);
            if (baseResult != null) {
                result = baseResult;
            }
        }

        return result;
    }

    public static boolean dropPlayerItemWithRandomChoice(PlayerEntity PlayerBase, ItemStack itemstack, boolean flag1) {
        boolean flag = false;
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            if (((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
                flag = true;
            }
        }

        return flag;
    }

    public static void beforeUpdate(PlayerEntity PlayerBase) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeUpdate();
        }

    }

    public static void beforeMoveEntity(PlayerEntity PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeMoveEntityBase(d, d1, d2);
        }

    }

    public static void afterMoveEntity(PlayerEntity PlayerBase, double d, double d1, double d2) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).afterMoveEntityBase(d, d1, d2);
        }

    }

    public static void beforeSleepInBedAt(PlayerEntity PlayerBase, int i1, int j, int k) {
        for (int i = 0; i < ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().size(); i++) {
            ((PlayerHandlerContainer) PlayerBase).getPlayerHandlers().get(i).beforeSleepInBedAt(i1, j, k);
        }

    }

}
