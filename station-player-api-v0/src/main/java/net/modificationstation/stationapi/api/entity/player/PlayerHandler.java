package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.class_141;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface PlayerHandler {

    default boolean onLivingUpdate() {
        return false;
    }

    default boolean updatePlayerActionState() {
        return false;
    }

    default boolean handleKeyPress(int i, boolean flag) {
        return false;
    }

    default boolean writeEntityBaseToNBT(NbtCompound tag) {
        return false;
    }

    default boolean readEntityBaseFromNBT(NbtCompound tag) {
        return false;
    }

    default boolean setEntityBaseDead() {
        return false;
    }

    default boolean onDeath(Entity killer) {
        return false;
    }

    default boolean respawn() {
        return false;
    }

    default boolean attackEntityBaseFrom(Entity attacker, int damage) {
        return false;
    }

    default double getDistanceSq(double d, double d1, double d2, double answer) {
        return answer;
    }

    default boolean isInWater(boolean inWater) {
        return inWater;
    }

    default boolean onExitGUI() {
        return false;
    }

    default boolean heal(int i) {
        return false;
    }

    default boolean canTriggerWalking(boolean canTrigger) {
        return canTrigger;
    }

    default int getPlayerArmorValue(int armor) {
        return armor;
    }

    default float getCurrentPlayerStrVsBlock(Block block, float f) {
        return f;
    }

    default boolean moveFlying(float x, float y, float z) {
        return false;
    }

    default boolean moveEntityBase(double x, double y, double d) {
        return false;
    }

    default class_141 sleepInBedAt(int x, int y, int z, class_141 status) {
        return status;
    }

    default float getEntityBaseBrightness(float f, float brightness) {
        return brightness;
    }

    default boolean pushOutOfBlocks(double x, double y, double d) {
        return false;
    }

    default boolean onUpdate() {
        return false;
    }

    default void afterUpdate() {
    }

    default boolean moveEntityBaseWithHeading(float f, float f1) {
        return false;
    }

    default boolean isOnLadder(boolean onLadder) {
        return onLadder;
    }

    default boolean isInsideOfMaterial(Material material, boolean inMaterial) {
        return inMaterial;
    }

    default boolean isSneaking(boolean sneaking) {
        return sneaking;
    }

    default boolean dropCurrentItem() {
        return false;
    }

    default boolean dropPlayerItem(ItemStack itemstack) {
        return false;
    }

    default boolean displayGUIEditSign(SignBlock sign) {
        return false;
    }

    default boolean displayGUIChest(Inventory iinventory) {
        return false;
    }

    default boolean displayWorkbenchGUI(int i, int j, int k) {
        return false;
    }

    default boolean displayGUIFurnace(FurnaceBlockEntity furnace) {
        return false;
    }

    default boolean displayGUIDispenser(DispenserBlockEntity dispenser) {
        return false;
    }

    default boolean sendChatMessage(String s) {
        return false;
    }

    default String getHurtSound(String previous) {
        return null;
    }

    default Boolean canHarvestBlock(Block block, Boolean previous) {
        return null;
    }

    default boolean fall(float f) {
        return false;
    }

    default boolean jump() {
        return false;
    }

    default boolean damageEntityBase(int i) {
        return false;
    }

    default Double getDistanceSqToEntityBase(Entity EntityBase, Double previous) {
        return null;
    }

    default boolean attackTargetEntityBaseWithCurrentItem(Entity EntityBase) {
        return false;
    }

    default Boolean handleWaterMovement(Boolean previous) {
        return null;
    }

    default Boolean handleLavaMovement(Boolean previous) {
        return null;
    }

    default boolean dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
        return false;
    }

    default void beforeUpdate() {
    }

    default void beforeMoveEntityBase(double d3, double d4, double d5) {
    }

    default void afterMoveEntityBase(double d3, double d4, double d5) {
    }

    default void beforeSleepInBedAt(int l, int i1, int j1) {
    }
}
