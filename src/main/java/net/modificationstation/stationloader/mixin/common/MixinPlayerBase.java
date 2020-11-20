package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationloader.api.common.entity.player.*;
import net.modificationstation.stationloader.api.common.event.entity.player.PlayerHandlerRegister;
import net.modificationstation.stationloader.api.common.item.CustomArmourValue;
import net.modificationstation.stationloader.impl.common.entity.player.PlayerAPI;
import net.modificationstation.stationloader.impl.common.util.ArmourUtils;
import net.modificationstation.stationloader.mixin.common.accessor.PlayerBaseAccessor;
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

@Mixin(PlayerBase.class)
public abstract class MixinPlayerBase extends Living implements PlayerBaseAccessor, PlayerBaseSettersGettersInvokers, HasPlayerHandlers, PlayerBaseSuper, StrengthOnMeta {

    @Shadow protected boolean sleeping;

    @Shadow public abstract float getStrengh(BlockBase arg);

    @Shadow public abstract ItemInstance getHeldItem();

    @Shadow public PlayerInventory inventory;
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
        if (PlayerAPI.updatePlayerActionState((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"), cancellable = true)
    private void onWriteEntityToNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.writeEntityToNBT((PlayerBase) (Object) this, nbtTagCompound))
            ci.cancel();
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"), cancellable = true)
    private void onReadEntityFromNBT(CompoundTag nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.readEntityFromNBT((PlayerBase) (Object) this, nbtTagCompound))
            ci.cancel();
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
    private void onCloseScreen(CallbackInfo ci) {
        if (PlayerAPI.onExitGUI((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "openChestScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIChest(InventoryBase iInventory, CallbackInfo ci) {
        if (PlayerAPI.displayGUIChest((PlayerBase) (Object) this, iInventory))
            ci.cancel();
    }

    @Inject(method = "openCraftingScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayWorkbenchGUI(int i, int i1, int i2, CallbackInfo ci) {
        if (PlayerAPI.displayWorkbenchGUI((PlayerBase) (Object) this, i, i1, i2))
            ci.cancel();
    }

    @Inject(method = "openFurnaceScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIFurnace(TileEntityFurnace tileEntityFurnace, CallbackInfo ci) {
        if (PlayerAPI.displayGUIFurnace((PlayerBase) (Object) this, tileEntityFurnace))
            ci.cancel();
    }

    @Inject(method = "openDispenserScreen", at = @At("HEAD"), cancellable = true)
    private void onDisplayGUIDispenser(TileEntityDispenser tileEntityDispenser, CallbackInfo ci) {
        if (PlayerAPI.displayGUIDispenser((PlayerBase) (Object) this, tileEntityDispenser))
            ci.cancel();
    }

    @Override
    public void setPosition(double v, double v1, double v2) {
        if (PlayerAPI.moveEntity((PlayerBase) (Object) this, v, v1, v2))
            return;
        super.setPosition(v, v1, v2);
    }

    @Inject(method = "getStrengh(Lnet/minecraft/block/BlockBase;)F", at = @At("RETURN"), cancellable = true)
    private void getStrength(BlockBase arg, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(PlayerAPI.getCurrentPlayerStrVsBlock((PlayerBase) (Object) this, arg, cir.getReturnValue()));
    }

    @Override
    public void addHealth(int i) {
        if (!PlayerAPI.heal(((PlayerBase) (Object) this), i))
            super.addHealth(i);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
    private void onRespawnPlayer(CallbackInfo ci) {
        if (PlayerAPI.respawn((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "updateDespawnCounter", at = @At("HEAD"), cancellable = true)
    private void onLivingUpdate(CallbackInfo ci) {
        if (PlayerAPI.onLivingUpdate((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    private void beforeTick(CallbackInfo ci) {
        PlayerAPI.beforeUpdate((PlayerBase) (Object) this);
        if (PlayerAPI.onUpdate((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void afterTick(CallbackInfo ci) {
        PlayerAPI.afterUpdate((PlayerBase) (Object) this);
    }

    @Override
    public void superMoveFlying(float f, float f1, float f2) {
        super.movementInputToVelocity(f, f1, f2);
    }

    @Override
    public void move(double v, double v1, double v2) {
        PlayerAPI.beforeMoveEntity((PlayerBase) (Object) this, v, v1, v2);
        if (!PlayerAPI.moveEntity((PlayerBase) (Object) this, v, v1, v2)) {
            super.move(v, v1, v2);
        }
        PlayerAPI.afterMoveEntity((PlayerBase) (Object) this, v, v1, v2);
    }

    @Inject(method = "trySleep(III)Lnet/minecraft/util/SleepStatus;", at = @At("HEAD"))
    public void trySleep(int i, int i1, int i2, CallbackInfoReturnable<SleepStatus> cir) {
        PlayerAPI.beforeSleepInBedAt((PlayerBase) (Object) this, i, i1, i2);
        SleepStatus enumstatus = PlayerAPI.sleepInBedAt((PlayerBase) (Object) this, i, i1, i2);
        if (enumstatus != null)
            cir.setReturnValue(enumstatus);
    }

    @Override
    public void doFall(float fallDist) {
        this.handleFallDamage(fallDist);
    }

    @Override
    public float getFallDistance() {
        return fallDistance;
    }

    @Override
    public void setFallDistance(float f) {
        fallDistance = f;
    }

    @Override
    public boolean getSleeping() {
        return sleeping;
    }

    @Override
    public boolean getJumping() {
        return jumping;
    }

    @Override
    public void doJump() {
        jump();
    }

    @Override
    public Random getRandom() {
        return rand;
    }

    @Override
    public void setYSize(float f) {
        standingEyeHeight = f;
    }

    @Inject(method = "travel(FF)V", at = @At("HEAD"), cancellable = true)
    private void travel(float f, float f1, CallbackInfo ci) {
        if (PlayerAPI.moveEntityWithHeading((PlayerBase) (Object) this, f, f1))
            ci.cancel();
    }

    @Override
    public boolean method_932() {
        return PlayerAPI.isOnLadder((PlayerBase) (Object) this, super.method_932());
    }

    @Override
    public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
        setMoveStrafing(newMoveStrafing);
        setMoveForward(newMoveForward);
        setIsJumping(newIsJumping);
    }

    @Override
    public boolean isInFluid(Material material) {
        return PlayerAPI.isInsideOfMaterial((PlayerBase) (Object) this, material, super.isInFluid(material));
    }

    @Inject(method = "dropSelectedItem()V", at = @At("HEAD"), cancellable = true)
    private void dropSelectedItem(CallbackInfo ci) {
        if (PlayerAPI.dropCurrentItem((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemInstance;)V", at = @At("HEAD"), cancellable = true)
    private void dropItem(ItemInstance arg, CallbackInfo ci) {
        if (PlayerAPI.dropPlayerItem((PlayerBase) (Object) this, arg))
            ci.cancel();
    }

    @Override
    public boolean superIsInsideOfMaterial(Material material) {
        return super.isInFluid(material);
    }

    @Override
    public float superGetEntityBrightness(float f) {
        return super.getBrightnessAtEyes(f);
    }

    @Override
    public String getHurtSound() {
        String hurtSound = PlayerAPI.getHurtSound((PlayerBase) (Object) this);
        if (hurtSound != null) {
            return hurtSound;
        }
        return super.getHurtSound();
    }

    @Override
    public String superGetHurtSound() {
        return super.getHurtSound();
    }

    @Inject(method = "canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z", at = @At("HEAD"))
    private void canRemoveBlock(BlockBase arg, CallbackInfoReturnable<Boolean> cir) {
        Boolean canHarvestBlock = PlayerAPI.canHarvestBlock((PlayerBase) (Object) this, arg);
        if (canHarvestBlock != null)
            cir.setReturnValue(canHarvestBlock);
    }

    @Override
    public void handleFallDamage(float f) {
        if (!PlayerAPI.fall((PlayerBase) (Object) this, f)) {
            super.handleFallDamage(f);
        }
    }

    @Inject(method = "handleFallDamage(F)V", at = @At("HEAD"), cancellable = true)
    private void handleFallDamage(float height, CallbackInfo ci) {
        if (PlayerAPI.fall((PlayerBase) (Object) this, height))
            ci.cancel();
    }

    @Override
    public void superFall(float f) {
        super.handleFallDamage(f);
    }

    @Inject(method = "jump()V", at = @At("HEAD"), cancellable = true)
    private void jump(CallbackInfo ci) {
        if (PlayerAPI.jump((PlayerBase) (Object) this))
            ci.cancel();
    }

    @Override
    public void superJump() {
        super.jump();
    }

    @Inject(method = "applyDamage(I)V", at = @At("HEAD"), cancellable = true)
    private void applyDamage(int initialDamage, CallbackInfo ci) {
        if (PlayerAPI.damageEntity((PlayerBase) (Object) this, initialDamage))
            ci.cancel();

        double damageAmount = initialDamage;
        ItemInstance[] armour = this.inventory.armour;

        for (ItemInstance armourInstance : armour) {
            // This solution is not exact with vanilla, but is WAY better than previous solutions which weren't even close to vanilla.
            if (armourInstance != null) {
                if (armourInstance.getType() instanceof CustomArmourValue) {
                    CustomArmourValue armor = (CustomArmourValue) armourInstance.getType();
                    int damageNegated = armor.modifyDamageDealt((PlayerBase) (Object) this, initialDamage, damageAmount);
                    damageAmount -= damageNegated;
                }
                else {
                    damageAmount -= ArmourUtils.getVanillaArmourReduction(armourInstance);
                    armourInstance.applyDamage(initialDamage, null);
                }
                if (damageAmount < 0) {
                    damageAmount = 0;
                }
            }
        }
        super.applyDamage((int) damageAmount);
        ci.cancel();
    }

    @Override
    public void superDamageEntity(int i) {
        super.applyDamage(i);
    }

    @Override
    public double method_1352(EntityBase entity) {
        Double result = PlayerAPI.getDistanceSqToEntity((PlayerBase) (Object) this, entity);
        if (result != null)
            return result;
        return super.method_1352(entity);
    }

    @Override
    public double superGetDistanceSqToEntity(EntityBase entity) {
        return super.method_1352(entity);
    }

    @Inject(method = "attack(Lnet/minecraft/entity/EntityBase;)V", at = @At("HEAD"), cancellable = true)
    private void attackTargetEntityWithCurrentItem(EntityBase arg, CallbackInfo ci) {
        if (PlayerAPI.attackTargetEntityWithCurrentItem((PlayerBase) (Object) this, arg))
            ci.cancel();
    }

    // ???
    /*public void superAttackTargetEntityWithCurrentItem(EntityBase entity) {
        super.attack(entity);
    }*/

    @Override
    public boolean method_1393() {
        Boolean result = PlayerAPI.handleWaterMovement((PlayerBase) (Object) this);
        if (result != null)
            return result;
        return super.method_1393();
    }

    @Override
    public boolean superHandleWaterMovement() {
        return super.method_1393();
    }

    @Override
    public boolean method_1335() {
        Boolean result = PlayerAPI.handleLavaMovement((PlayerBase) (Object) this);
        if (result != null)
            return result;
        return super.method_1335();
    }

    @Override
    public boolean superHandleLavaMovement() {
        return super.method_1335();
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemInstance;Z)V", at = @At("HEAD"), cancellable = true)
    public void dropPlayerItemWithRandomChoice(ItemInstance arg, boolean flag, CallbackInfo ci) {
        if (PlayerAPI.dropPlayerItemWithRandomChoice((PlayerBase) (Object) this, arg, flag))
            ci.cancel();
    }

    // ???
    /*public void superDropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
        super.dropItem(itemstack, flag);
    }*/

    @Override
    public void superUpdatePlayerActionState() {
        super.tickHandSwing();
    }

    @Override
    public void setMoveForward(float value) {
        this.field_1060 = value;
    }

    @Override
    public void setMoveStrafing(float value) {
        this.field_1029 = value;
    }

    @Override
    public void setIsJumping(boolean value) {
        this.jumping = value;
    }

    @Override
    public float getStrengh(BlockBase arg, int meta) {
        this.meta = meta;
        return getStrengh(arg);
    }

    @Redirect(method = "getStrengh(Lnet/minecraft/block/BlockBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;method_674(Lnet/minecraft/block/BlockBase;)F"))
    private float getStrengthForMeta(PlayerInventory playerInventory, BlockBase arg) {
        if (meta == null)
            return playerInventory.method_674(arg);
        else {
            ItemInstance itemInstance = playerInventory.getHeldItem();
            float ret = itemInstance == null ? playerInventory.method_674(arg) : ((net.modificationstation.stationloader.api.common.item.StrengthOnMeta) itemInstance.getType()).getStrengthOnBlock(itemInstance, arg, meta);
            meta = null;
            return ret;
        }
    }

    private Integer meta;
}
