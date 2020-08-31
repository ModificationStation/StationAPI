package net.modificationstation.stationloader.api.common.entity.player;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Sign;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;

public interface PlayerHandler {

    default void playerInit() {
    }

    default boolean onLivingUpdate() {
        return false;
    }

    default boolean updatePlayerActionState() {
        return false;
    }

    default boolean handleKeyPress(int i, boolean flag) {
        return false;
    }

    default boolean writeEntityBaseToNBT(CompoundTag tag) {
        return false;
    }

    default boolean readEntityBaseFromNBT(CompoundTag tag) {
        return false;
    }

    default boolean setEntityBaseDead() {
        return false;
    }

    default boolean onDeath(EntityBase killer) {
        return false;
    }

    default boolean respawn() {
        return false;
    }

    default boolean attackEntityBaseFrom(EntityBase attacker, int damage) {
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

    default float getCurrentPlayerStrVsBlock(BlockBase block, float f) {
        return f;
    }

    default boolean moveFlying(float x, float y, float z) {
        return false;
    }

    default boolean moveEntityBase(double x, double y, double d) {
        return false;
    }

    default SleepStatus sleepInBedAt(int x, int y, int z, SleepStatus status) {
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

    default boolean dropPlayerItem(ItemInstance itemstack) {
        return false;
    }

    default boolean displayGUIEditSign(Sign sign) {
        return false;
    }

    default boolean displayGUIChest(InventoryBase iinventory) {
        return false;
    }

    default boolean displayWorkbenchGUI(int i, int j, int k) {
        return false;
    }

    default boolean displayGUIFurnace(TileEntityFurnace furnace) {
        return false;
    }

    default boolean displayGUIDispenser(TileEntityDispenser dispenser) {
        return false;
    }

    default boolean sendChatMessage(String s) {
        return false;
    }

    default String getHurtSound(String previous) {
        return null;
    }

    default Boolean canHarvestBlock(BlockBase block, Boolean previous) {
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

    default Double getDistanceSqToEntityBase(EntityBase EntityBase, Double previous) {
        return null;
    }

    default boolean attackTargetEntityBaseWithCurrentItem(EntityBase EntityBase) {
        return false;
    }

    default Boolean handleWaterMovement(Boolean previous) {
        return null;
    }

    default Boolean handleLavaMovement(Boolean previous) {
        return null;
    }

    default boolean dropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
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
