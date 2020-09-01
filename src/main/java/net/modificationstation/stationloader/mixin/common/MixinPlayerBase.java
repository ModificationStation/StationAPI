package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.Smoother;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationloader.api.common.event.entity.player.PlayerHandlerRegister;
import net.modificationstation.stationloader.impl.common.entity.player.PlayerAPI;
import net.modificationstation.stationloader.api.common.entity.player.HasPlayerHandlers;
import net.modificationstation.stationloader.mixin.common.accessor.PlayerBaseAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(PlayerBase.class)
public class MixinPlayerBase extends Living implements PlayerBaseAccessor, HasPlayerHandlers {

    @Shadow protected boolean sleeping;
    private List<PlayerHandler> playerBases;

    public MixinPlayerBase(Level world) {
        super(world);
    }

    @Override
    public List<PlayerHandler> getPlayerBases() {
        if (playerBases == null) {
            playerBases = new ArrayList<>();
            PlayerHandlerRegister.EVENT.getInvoker().registerPlayerHandlers(playerBases, (PlayerBase) (Object) this);
        }
        return playerBases;
    }

    @Inject(method = "tickHandSwing", at = @At("HEAD"), cancellable = true)
    private void onUpdatePlayerActionState(CallbackInfo ci) {
        if (PlayerAPI.updatePlayerActionState((PlayerBase) (Object) this)) {
            ci.cancel();
        }
    }

    /*@Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
    private void onHandleKeyPress(int i, boolean b, CallbackInfo ci) {
        if (PlayerAPI.handleKeyPress((PlayerBase) (Object) this, i, b)) {
            ci.cancel();
        }
    }*/

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"), cancellable = true)
    private void onWriteEntityToNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.writeEntityToNBT((PlayerBase) (Object) this, nbtTagCompound)) {
            ci.cancel();
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"), cancellable = true)
    private void onReadEntityFromNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.readEntityFromNBT((PlayerBase) (Object) this, nbtTagCompound)) {
            ci.cancel();
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
    private void onCloseScreen(CallbackInfo ci) {
        if (PlayerAPI.onExitGUI((PlayerBase) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "openChestScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIChest(InventoryBase iInventory, CallbackInfo ci) {
        if (PlayerAPI.displayGUIChest((PlayerBase) (Object) this, iInventory)) {
            ci.cancel();
        }
    }

    @Inject(method = "openCraftingScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayWorkbenchGUI(int i, int i1, int i2, CallbackInfo ci) {
        if (PlayerAPI.displayWorkbenchGUI((PlayerBase) (Object) this, i, i1, i2)) {
            ci.cancel();
        }
    }

    @Inject(method = "openFurnaceScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIFurnace(TileEntityFurnace tileEntityFurnace, CallbackInfo ci) {
        if (PlayerAPI.displayGUIFurnace((PlayerBase) (Object) this, tileEntityFurnace)) {
            ci.cancel();
        }
    }

    @Inject(method = "openDispenserScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIDispenser(TileEntityDispenser tileEntityDispenser, CallbackInfo ci) {
        if (PlayerAPI.displayGUIDispenser((PlayerBase) (Object) this, tileEntityDispenser)) {
            ci.cancel();
        }
    }

    /*@Inject(method = "method_491", at = @At("HEAD"), cancellable = true)
    private void onOnItemPickup(EntityBase entity, int i, CallbackInfo ci) {
        if (PlayerAPI.onItemPickup((EntityPlayerSP) (Object) this, entity, i)) {
            ci.cancel();
        }
    }*/

//    @Override
//    public void setPosition(double v, double v1, double v2) {
//        if (PlayerAPI.moveEntity((PlayerBase) (Object) this, v, v1, v2)) {
//            return;
//        }
//        super.setPosition(v, v1, v2);
//    }


    /*@Redirect(method = "method_141", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;method_687()I"))
    private int redirectGetPlayerArmorValue(PlayerInventory inventoryPlayer) {
        return PlayerAPI.getPlayerArmorValue((PlayerBase) (Object) this, inventoryPlayer.method_687());
    }*/

//    @Inject(method = "method_1373", at = @At("RETURN"), cancellable = true)
//    private void isSneaking(CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(PlayerAPI.isSneaking((PlayerBase) (Object) this, cir.getReturnValue()));
//    }

    /*@Override
    public float getStrengh(BlockBase block) {
        return PlayerAPI.getCurrentPlayerStrVsBlock((PlayerBase) (Object) this, block, super.getStrengh(block));
    }*/

//    @Override
//    public void method_1392(int i) {
//        if (!PlayerAPI.heal(((PlayerBase) (Object) this), i)) {
//            super.method_1392(i);
//        }
//    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
    private void onRespawnPlayer(CallbackInfo ci) {
        if (PlayerAPI.respawn((PlayerBase) (Object) this)) {
            ci.cancel();
        }
    }

    /*@Inject(method = "method_1372", at = @At("HEAD"), cancellable = true)
    private void injectPushOutOfBlocks(double v, double v1, double v2, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerAPI.pushOutOfBlocks((PlayerBase) (Object) this, v, v1, v2)) {
            cir.setReturnValue(false);
        }
    }*/

    @Inject(method = "updateDespawnCounter", at = @At("HEAD"), cancellable = true)
    private void dumbName(CallbackInfo ci) {
        if (PlayerAPI.onLivingUpdate((PlayerBase) (Object) this)) {
            ci.cancel();
        }
    }

//    @Override
//    public void tick() {
//        PlayerAPI.beforeUpdate((PlayerBase) (Object) this);
//        if (!PlayerAPI.onUpdate((PlayerBase) (Object) this)) {
//            super.tick();
//        }
//        PlayerAPI.afterUpdate((PlayerBase) (Object) this);
//    }

//    public void superMoveFlying(float f, float f1, float f2) {
//        super.movementInputToVelocity(f, f1, f2);
//    }

//    @Override
//    public void move(double v, double v1, double v2) {
//        PlayerAPI.beforeMoveEntity((PlayerBase) (Object) this, v, v1, v2);
//        if (!PlayerAPI.moveEntity((PlayerBase) (Object) this, v, v1, v2)) {
//            super.move(v, v1, v2);
//        }
//        PlayerAPI.afterMoveEntity((PlayerBase) (Object) this, v, v1, v2);
//    }

    @Inject(method = "trySleep(III)Lnet/minecraft/util/SleepStatus;", at = @At("HEAD"))
    public void trySleep(int i, int i1, int i2, CallbackInfoReturnable<SleepStatus> cir) {
        PlayerAPI.beforeSleepInBedAt((PlayerBase) (Object) this, i, i1, i2);
        SleepStatus enumstatus = PlayerAPI.sleepInBedAt((PlayerBase) (Object) this, i, i1, i2);
        if (enumstatus != null)
            cir.setReturnValue(enumstatus);
    }

//    public void doFall(float fallDist) {
//        super.handleFallDamage(fallDist);
//    }

    public float getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(float f) {
        fallDistance = f;
    }

    public boolean getSleeping() {
        return sleeping;
    }

    public boolean getJumping() {
        return jumping;
    }

    public void doJump() {
        jump();
    }

    public Random getRandom() {
        return rand;
    }

    public void setYSize(float f) {
        standingEyeHeight = f;
    }

//    @Override
//    public void travel(float v, float v1) {
//        if (!PlayerAPI.moveEntityWithHeading((PlayerBase) (Object) this, v, v1)) {
//            super.travel(v, v1);
//        }
//    }
//
//    @Override
//    public boolean method_932() {
//        return PlayerAPI.isOnLadder((PlayerBase) (Object) this, super.method_932());
//    }

    public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
        setMoveStrafing(newMoveStrafing);
        setMoveForward(newMoveForward);
        setIsJumping(newIsJumping);
    }


//    @Override
//    public boolean isInFluid(Material material) {
//        return PlayerAPI.isInsideOfMaterial((PlayerBase) (Object) this, material, super.isInFluid(material));
//    }

    /*@Override
    public void dropSelectedItem() {
        if (!PlayerAPI.dropCurrentItem((PlayerBase) (Object) this)) {
            super.dropSelectedItem();
        }
    }*/

    /*@Override
    public void dropItem(ItemInstance itemStack) {
        if (!PlayerAPI.dropPlayerItem((PlayerBase) (Object) this, itemStack)) {
            super.dropItem(itemStack);
        }
    }*/

//    public boolean superIsInsideOfMaterial(Material material) {
//        return super.isInFluid(material);
//    }
//
//    public float superGetEntityBrightness(float f) {
//        return super.getBrightnessAtEyes(f);
//    }

    public void sendChatMessage(String s) {
        PlayerAPI.sendChatMessage((PlayerBase) (Object) this, s);
    }

//    @Override
//    public String getHurtSound() {
//        String hurtSound = PlayerAPI.getHurtSound((PlayerBase) (Object) this);
//        if (hurtSound != null) {
//            return hurtSound;
//        }
//        return super.getHurtSound();
//    }

//    public String superGetHurtSound() {
//        return super.getHurtSound();
//    }

    /*public float superGetCurrentPlayerStrVsBlock(BlockBase block) {
        return super.getStrengh(block);
    }*/


//    @Override
//    public boolean canRemoveBlock(BlockBase block) {
//        Boolean canHarvestBlock = PlayerAPI.canHarvestBlock((PlayerBase) (Object) this, block);
//        if (canHarvestBlock != null) {
//            return canHarvestBlock;
//        }
//        return super.canRemoveBlock(block);
//    }

//    @Override
//    public void handleFallDamage(float f) {
//        if (!PlayerAPI.fall((PlayerBase) (Object) this, f)) {
//            super.handleFallDamage(f);
//        }
//    }
//
//    public void superFall(float f) {
//        super.handleFallDamage(f);
//    }
//
//    @Override
//    public void jump() {
//        if (!PlayerAPI.jump((PlayerBase) (Object) this)) {
//            super.jump();
//        }
//    }

    public void superJump() {
        super.jump();
    }

//    @Override
//    public void applyDamage(int i) {
//        if (!PlayerAPI.damageEntity((PlayerBase) (Object) this, i)) {
//            super.applyDamage(i);
//        }
//    }

//    @Override
//    public void method_494() {
//
//    }

    public void superDamageEntity(int i) {
        super.applyDamage(i);
    }

//    @Override
//    public double method_1352(EntityBase entity) {
//        Double result = PlayerAPI.getDistanceSqToEntity((PlayerBase) (Object) this, entity);
//        if (result != null) {
//            return result;
//        }
//        return super.method_1352(entity);
//    }

    public double superGetDistanceSqToEntity(EntityBase entity) {
        return super.method_1352(entity);
    }

//    public void attackTargetEntityWithCurrentItem(EntityBase entity) {
//        if (!PlayerAPI.attackTargetEntityWithCurrentItem((PlayerBase) (Object) this, entity)) {
//            super.attack(entity);
//        }
//    }
//
//    public void superAttackTargetEntityWithCurrentItem(EntityBase entity) {
//        super.attack(entity);
//    }

//    @Override
//    public boolean method_1393() {
//        Boolean result = PlayerAPI.handleWaterMovement((PlayerBase) (Object) this);
//        if (result != null) {
//            return result;
//        }
//        return super.method_1393();
//    }

    public boolean superHandleWaterMovement() {
        return super.method_1393();
    }

//    @Override
//    public boolean method_1335() {
//        Boolean result = PlayerAPI.handleLavaMovement((PlayerBase) (Object) this);
//        if (result != null) {
//            return result;
//        }
//        return super.method_1335();
//    }

    public boolean superHandleLavaMovement() {
        return super.method_1335();
    }

//    public void dropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
//        if (!PlayerAPI.dropPlayerItemWithRandomChoice((PlayerBase) (Object) this, itemstack, flag)) {
//            super.dropItem(itemstack, flag);
//        }
//    }
//
//    public void superDropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
//        super.dropItem(itemstack, flag);
//    }

    public void superUpdatePlayerActionState() {
        super.tickHandSwing();
    }

    public void setMoveForward(float value) {
        this.field_1060 = value;
    }

    public void setMoveStrafing(float value) {
        this.field_1029 = value;
    }

    public void setIsJumping(boolean value) {
        this.jumping = value;
    }
}
