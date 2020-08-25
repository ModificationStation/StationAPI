package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Smoother;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.AbstractClientPlayer;
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
import net.modificationstation.stationloader.api.common.event.entity.player.PlayerBaseRegister;
import net.modificationstation.stationloader.impl.common.entity.player.PlayerAPI;
import net.modificationstation.stationloader.api.common.entity.player.ClientPlayerAccessor;
import net.modificationstation.stationloader.mixin.common.accessor.EntityPlayerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(AbstractClientPlayer.class)
public class MixinEntityPlayerSP extends PlayerBase implements EntityPlayerAccessor, ClientPlayerAccessor {

    @Shadow private Smoother field_163;
    @Shadow private Smoother field_164;
    @Shadow private Smoother field_165;
    private List<PlayerHandler> playerBases;

    public MixinEntityPlayerSP(Level world) {
        super(world);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/level/Level;Lnet/minecraft/client/util/Session;I)V", at = @At("RETURN"))
    private void initEnd(CallbackInfo ci) {
    }

    @Environment(EnvType.CLIENT)
    public List<PlayerHandler> getPlayerBases() {
        if (playerBases == null) {
            field_163 = new Smoother();
            field_164 = new Smoother();
            field_165 = new Smoother();
            playerBases = new ArrayList<>();
            PlayerBaseRegister.EVENT.getInvoker().registerRegisterPlayerBases(playerBases, (AbstractClientPlayer) (Object) this);
        }
        return playerBases;
    }

    @Inject(method = "tickHandSwing", at = @At("HEAD"), cancellable = true)
    private void onUpdatePlayerActionState(CallbackInfo ci) {
        if (PlayerAPI.updatePlayerActionState((AbstractClientPlayer) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
    private void onHandleKeyPress(int i, boolean b, CallbackInfo ci) {
        if (PlayerAPI.handleKeyPress((AbstractClientPlayer) (Object) this, i, b)) {
            ci.cancel();
        }
    }

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"), cancellable = true)
    private void onWriteEntityToNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.writeEntityToNBT((AbstractClientPlayer) (Object) this, nbtTagCompound)) {
            ci.cancel();
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"), cancellable = true)
    private void onReadEntityFromNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.readEntityFromNBT((AbstractClientPlayer) (Object) this, nbtTagCompound)) {
            ci.cancel();
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
    private void onCloseScreen(CallbackInfo ci) {
        if (PlayerAPI.onExitGUI((AbstractClientPlayer) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "openChestScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIChest(InventoryBase iInventory, CallbackInfo ci) {
        if (PlayerAPI.displayGUIChest((AbstractClientPlayer) (Object) this, iInventory)) {
            ci.cancel();
        }
    }

    @Inject(method = "openCraftingScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayWorkbenchGUI(int i, int i1, int i2, CallbackInfo ci) {
        if (PlayerAPI.displayWorkbenchGUI((AbstractClientPlayer) (Object) this, i, i1, i2)) {
            ci.cancel();
        }
    }

    @Inject(method = "openFurnaceScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIFurnace(TileEntityFurnace tileEntityFurnace, CallbackInfo ci) {
        if (PlayerAPI.displayGUIFurnace((AbstractClientPlayer) (Object) this, tileEntityFurnace)) {
            ci.cancel();
        }
    }

    @Inject(method = "openDispenserScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIDispenser(TileEntityDispenser tileEntityDispenser, CallbackInfo ci) {
        if (PlayerAPI.displayGUIDispenser((AbstractClientPlayer) (Object) this, tileEntityDispenser)) {
            ci.cancel();
        }
    }

    /*@Inject(method = "method_491", at = @At("HEAD"), cancellable = true)
    private void onOnItemPickup(EntityBase entity, int i, CallbackInfo ci) {
        if (PlayerAPI.onItemPickup((EntityPlayerSP) (Object) this, entity, i)) {
            ci.cancel();
        }
    }*/

    @Override
    public void setPosition(double v, double v1, double v2) {
        if (PlayerAPI.moveEntity((AbstractClientPlayer) (Object) this, v, v1, v2)) {
            return;
        }
        super.setPosition(v, v1, v2);
    }


    @Redirect(method = "method_141", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;method_687()I"))
    private int redirectGetPlayerArmorValue(PlayerInventory inventoryPlayer) {
        return PlayerAPI.getPlayerArmorValue((AbstractClientPlayer) (Object) this, inventoryPlayer.method_687());
    }

    @Inject(method = "method_1373", at = @At("RETURN"), cancellable = true)
    private void isIsSneaking(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(PlayerAPI.isSneaking((AbstractClientPlayer) (Object) this, cir.getReturnValue()));
    }

    @Override
    public float getStrengh(BlockBase block) {
        return PlayerAPI.getCurrentPlayerStrVsBlock((AbstractClientPlayer) (Object) this, block, super.getStrengh(block));
    }

    @Override
    public void method_1392(int i) {
        if (!PlayerAPI.heal(((AbstractClientPlayer) (Object) this), i)) {
            super.method_1392(i);
        }
    }

    @Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
    private void onRespawnPlayer(CallbackInfo ci) {
        if (PlayerAPI.respawn((AbstractClientPlayer) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "method_1372", at = @At("HEAD"), cancellable = true)
    private void injectPushOutOfBlocks(double v, double v1, double v2, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerAPI.pushOutOfBlocks((AbstractClientPlayer) (Object) this, v, v1, v2)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updateDespawnCounter", at = @At("HEAD"), cancellable = true)
    private void dumbName(CallbackInfo ci) {
        if (PlayerAPI.onLivingUpdate((AbstractClientPlayer) (Object) this)) {
            ci.cancel();
        }
    }

    @Override
    public void tick() {
        PlayerAPI.beforeUpdate((AbstractClientPlayer) (Object) this);
        if (!PlayerAPI.onUpdate((AbstractClientPlayer) (Object) this)) {
            super.tick();
        }
        PlayerAPI.afterUpdate((AbstractClientPlayer) (Object) this);
    }

    public void superMoveFlying(float f, float f1, float f2) {
        super.movementInputToVelocity(f, f1, f2);
    }

    @Override
    public void move(double v, double v1, double v2) {
        PlayerAPI.beforeMoveEntity((AbstractClientPlayer) (Object) this, v, v1, v2);
        if (!PlayerAPI.moveEntity((AbstractClientPlayer) (Object) this, v, v1, v2)) {
            super.move(v, v1, v2);
        }
        PlayerAPI.afterMoveEntity((AbstractClientPlayer) (Object) this, v, v1, v2);
    }

    @Override
    public SleepStatus trySleep(int i, int i1, int i2) {
        PlayerAPI.beforeSleepInBedAt((AbstractClientPlayer) (Object) this, i, i1, i2);
        SleepStatus enumstatus = PlayerAPI.sleepInBedAt((AbstractClientPlayer) (Object) this, i, i1, i2);
        if (enumstatus == null) {
            return super.trySleep(i, i1, i2);
        } else {
            return enumstatus;
        }
    }

    public void doFall(float fallDist) {
        super.handleFallDamage(fallDist);
    }

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

    @Override
    public void travel(float v, float v1) {
        if (!PlayerAPI.moveEntityWithHeading((AbstractClientPlayer) (Object) this, v, v1)) {
            super.travel(v, v1);
        }
    }

    @Override
    public boolean method_932() {
        return PlayerAPI.isOnLadder((AbstractClientPlayer) (Object) this, super.method_932());
    }

    public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
        setMoveStrafing(newMoveStrafing);
        setMoveForward(newMoveForward);
        setIsJumping(newIsJumping);
    }


    @Override
    public boolean isInFluid(Material material) {
        return PlayerAPI.isInsideOfMaterial((AbstractClientPlayer) (Object) this, material, super.isInFluid(material));
    }

    @Override
    public void dropSelectedItem() {
        if (!PlayerAPI.dropCurrentItem((AbstractClientPlayer) (Object) this)) {
            super.dropSelectedItem();
        }
    }

    @Override
    public void dropItem(ItemInstance itemStack) {
        if (!PlayerAPI.dropPlayerItem((AbstractClientPlayer) (Object) this, itemStack)) {
            super.dropItem(itemStack);
        }
    }

    public boolean superIsInsideOfMaterial(Material material) {
        return super.isInFluid(material);
    }

    public float superGetEntityBrightness(float f) {
        return super.getBrightnessAtEyes(f);
    }

    public void sendChatMessage(String s) {
        PlayerAPI.sendChatMessage((AbstractClientPlayer) (Object) this, s);
    }

    @Override
    protected String getHurtSound() {
        String hurtSound = PlayerAPI.getHurtSound((AbstractClientPlayer) (Object) this);
        if (hurtSound != null) {
            return hurtSound;
        }
        return super.getHurtSound();
    }

    public String superGetHurtSound() {
        return super.getHurtSound();
    }

    public float superGetCurrentPlayerStrVsBlock(BlockBase block) {
        return super.getStrengh(block);
    }


    @Override
    public boolean canRemoveBlock(BlockBase block) {
        Boolean canHarvestBlock = PlayerAPI.canHarvestBlock((AbstractClientPlayer) (Object) this, block);
        if (canHarvestBlock != null) {
            return canHarvestBlock;
        }
        return super.canRemoveBlock(block);
    }

    @Override
    protected void handleFallDamage(float f) {
        if (!PlayerAPI.fall((AbstractClientPlayer) (Object) this, f)) {
            super.handleFallDamage(f);
        }
    }

    public void superFall(float f) {
        super.handleFallDamage(f);
    }

    @Override
    protected void jump() {
        if (!PlayerAPI.jump((AbstractClientPlayer) (Object) this)) {
            super.jump();
        }
    }

    public void superJump() {
        super.jump();
    }

    @Override
    protected void applyDamage(int i) {
        if (!PlayerAPI.damageEntity((AbstractClientPlayer) (Object) this, i)) {
            super.applyDamage(i);
        }
    }

    @Override
    public void method_494() {

    }

    protected void superDamageEntity(int i) {
        super.applyDamage(i);
    }

    @Override
    public double method_1352(EntityBase entity) {
        Double result = PlayerAPI.getDistanceSqToEntity((AbstractClientPlayer) (Object) this, entity);
        if (result != null) {
            return result;
        }
        return super.method_1352(entity);
    }

    public double superGetDistanceSqToEntity(EntityBase entity) {
        return super.method_1352(entity);
    }

    public void attackTargetEntityWithCurrentItem(EntityBase entity) {
        if (!PlayerAPI.attackTargetEntityWithCurrentItem((AbstractClientPlayer) (Object) this, entity)) {
            super.attack(entity);
        }
    }

    public void superAttackTargetEntityWithCurrentItem(EntityBase entity) {
        super.attack(entity);
    }

    @Override
    public boolean method_1393() {
        Boolean result = PlayerAPI.handleWaterMovement((AbstractClientPlayer) (Object) this);
        if (result != null) {
            return result;
        }
        return super.method_1393();
    }

    public boolean superHandleWaterMovement() {
        return super.method_1393();
    }

    @Override
    public boolean method_1335() {
        Boolean result = PlayerAPI.handleLavaMovement((AbstractClientPlayer) (Object) this);
        if (result != null) {
            return result;
        }
        return super.method_1335();
    }

    public boolean superHandleLavaMovement() {
        return super.method_1335();
    }

    public void dropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
        if (!PlayerAPI.dropPlayerItemWithRandomChoice((AbstractClientPlayer) (Object) this, itemstack, flag)) {
            super.dropItem(itemstack, flag);
        }
    }

    public void superDropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
        super.dropItem(itemstack, flag);
    }

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
