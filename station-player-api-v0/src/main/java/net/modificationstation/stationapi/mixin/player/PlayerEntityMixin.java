package net.modificationstation.stationapi.mixin.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.class_141;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.player.PlayerBaseSettersGetters;
import net.modificationstation.stationapi.api.entity.player.PlayerBaseSuper;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHandlerContainer;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.impl.entity.player.PlayerAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(PlayerEntity.class)
class PlayerEntityMixin extends LivingEntity implements PlayerBaseSettersGetters, PlayerHandlerContainer, PlayerBaseSuper {
    @Shadow
    public PlayerInventory inventory;
    @Shadow
    protected boolean sleeping;
    @Unique
    private List<PlayerHandler> playerBases;

    private PlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public List<PlayerHandler> getPlayerHandlers() {
        if (playerBases == null) {
            playerBases = new ArrayList<>();
            StationAPI.EVENT_BUS.post(
                    PlayerEvent.HandlerRegister.builder()
                            .player((PlayerEntity) (Object) this)
                            .playerHandlers(playerBases)
                            .build()
            );
        }
        return playerBases;
    }

    @Inject(
            method = "method_910",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onUpdatePlayerActionState(CallbackInfo ci) {
        if (PlayerAPI.updatePlayerActionState((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "writeNbt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onWriteEntityToNBT(NbtCompound nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.writeEntityToNBT((PlayerEntity) (Object) this, nbtTagCompound))
            ci.cancel();
    }

    @Inject(
            method = "readNbt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onReadEntityFromNBT(NbtCompound nbtTagCompound, CallbackInfo ci) {
        if (PlayerAPI.readEntityFromNBT((PlayerEntity) (Object) this, nbtTagCompound))
            ci.cancel();
    }

    @Inject(
            method = "closeScreen",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onCloseScreen(CallbackInfo ci) {
        if (PlayerAPI.onExitGUI((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "method_486",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onDisplayGUIChest(Inventory iInventory, CallbackInfo ci) {
        if (PlayerAPI.displayGUIChest((PlayerEntity) (Object) this, iInventory))
            ci.cancel();
    }

    @Inject(
            method = "method_484",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onDisplayWorkbenchGUI(int i, int i1, int i2, CallbackInfo ci) {
        if (PlayerAPI.displayWorkbenchGUI((PlayerEntity) (Object) this, i, i1, i2))
            ci.cancel();
    }

    @Inject(
            method = "method_487",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onDisplayGUIFurnace(FurnaceBlockEntity tileEntityFurnace, CallbackInfo ci) {
        if (PlayerAPI.displayGUIFurnace((PlayerEntity) (Object) this, tileEntityFurnace))
            ci.cancel();
    }

    @Inject(
            method = "method_485",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onDisplayGUIDispenser(DispenserBlockEntity tileEntityDispenser, CallbackInfo ci) {
        if (PlayerAPI.displayGUIDispenser((PlayerEntity) (Object) this, tileEntityDispenser))
            ci.cancel();
    }

    @Override
    public void method_1340(double v, double v1, double v2) {
        if (PlayerAPI.moveEntity((PlayerEntity) (Object) this, v, v1, v2))
            return;
        super.method_1340(v, v1, v2);
    }

    @Inject(
            method = "method_511",
            at = @At("RETURN"),
            cancellable = true
    )
    private void stationapi_getStrength(Block arg, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(PlayerAPI.getCurrentPlayerStrVsBlock((PlayerEntity) (Object) this, arg, cir.getReturnValue()));
    }

    @Override
    public void method_939(int i) {
        if (!PlayerAPI.heal(((PlayerEntity) (Object) this), i))
            super.method_939(i);
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "respawn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onRespawnPlayer(CallbackInfo ci) {
        if (PlayerAPI.respawn((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "method_937",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onLivingUpdate(CallbackInfo ci) {
        if (PlayerAPI.onLivingUpdate((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "tick()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_beforeTick(CallbackInfo ci) {
        PlayerAPI.beforeUpdate((PlayerEntity) (Object) this);
        if (PlayerAPI.onUpdate((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "tick()V",
            at = @At("RETURN")
    )
    private void afterTick(CallbackInfo ci) {
        PlayerAPI.afterUpdate((PlayerEntity) (Object) this);
    }

    @Override
    public void superMoveFlying(float f, float f1, float f2) {
        super.method_1324(f, f1, f2);
    }

    @Override
    public void move(double v, double v1, double v2) {
        PlayerAPI.beforeMoveEntity((PlayerEntity) (Object) this, v, v1, v2);
        if (!PlayerAPI.moveEntity((PlayerEntity) (Object) this, v, v1, v2)) {
            super.move(v, v1, v2);
        }
        PlayerAPI.afterMoveEntity((PlayerEntity) (Object) this, v, v1, v2);
    }

    @Inject(
            method = "method_495",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stationapi_trySleep(int i, int i1, int i2, CallbackInfoReturnable<class_141> cir) {
        PlayerAPI.beforeSleepInBedAt((PlayerEntity) (Object) this, i, i1, i2);
        class_141 enumstatus = PlayerAPI.sleepInBedAt((PlayerEntity) (Object) this, i, i1, i2);
        if (enumstatus != null)
            cir.setReturnValue(enumstatus);
    }

    @Override
    public void doFall(float fallDist) {
        this.method_1389(fallDist);
    }

    @Override
    public float getFallDistance() {
        return field_1636;
    }

    @Override
    public void setFallDistance(float f) {
        field_1636 = f;
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
        method_944();
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public void setYSize(float f) {
        eyeHeight = f;
    }

    @Inject(
            method = "method_945",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_travel(float f, float f1, CallbackInfo ci) {
        if (PlayerAPI.moveEntityWithHeading((PlayerEntity) (Object) this, f, f1))
            ci.cancel();
    }

    @Override
    public boolean method_932() {
        return PlayerAPI.isOnLadder((PlayerEntity) (Object) this, super.method_932());
    }

    @Override
    public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
        setMoveStrafing(newMoveStrafing);
        setMoveForward(newMoveForward);
        setIsJumping(newIsJumping);
    }

    @Override
    public boolean isInFluid(Material material) {
        return PlayerAPI.isInsideOfMaterial((PlayerEntity) (Object) this, material, super.isInFluid(material));
    }

    @Inject(
            method = "dropSelectedItem()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_dropSelectedItem(CallbackInfo ci) {
        if (PlayerAPI.dropCurrentItem((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Inject(
            method = "method_513",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_dropItem(ItemStack arg, CallbackInfo ci) {
        if (PlayerAPI.dropPlayerItem((PlayerEntity) (Object) this, arg))
            ci.cancel();
    }

    @Override
    public boolean superIsInsideOfMaterial(Material material) {
        return super.isInFluid(material);
    }

    @Override
    public float superGetEntityBrightness(float f) {
        return super.method_1394(f);
    }

    @Override
    public String method_912() {
        String hurtSound = PlayerAPI.getHurtSound((PlayerEntity) (Object) this);
        if (hurtSound != null) {
            return hurtSound;
        }
        return super.method_912();
    }

    @Override
    public String superGetHurtSound() {
        return super.method_912();
    }

    @Inject(
            method = "method_514",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_canRemoveBlock(Block arg, CallbackInfoReturnable<Boolean> cir) {
        Boolean canHarvestBlock = PlayerAPI.canHarvestBlock((PlayerEntity) (Object) this, arg);
        if (canHarvestBlock != null)
            cir.setReturnValue(canHarvestBlock);
    }

    @Override
    public void method_1389(float f) {
        if (!PlayerAPI.fall((PlayerEntity) (Object) this, f)) {
            super.method_1389(f);
        }
    }

    @Inject(
            method = "method_1389",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_handleFallDamage(float height, CallbackInfo ci) {
        if (PlayerAPI.fall((PlayerEntity) (Object) this, height))
            ci.cancel();
    }

    @Override
    public void superFall(float f) {
        super.method_1389(f);
    }

    @Inject(
            method = "method_944",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_jump(CallbackInfo ci) {
        if (PlayerAPI.jump((PlayerEntity) (Object) this))
            ci.cancel();
    }

    @Override
    public void superJump() {
        super.method_944();
    }

    @Override
    public void superDamageEntity(int i) {
        super.method_946(i);
    }

    @Override
    public double method_1352(Entity entity) {
        Double result = PlayerAPI.getDistanceSqToEntity((PlayerEntity) (Object) this, entity);
        if (result != null)
            return result;
        return super.method_1352(entity);
    }

    @Override
    public double superGetDistanceSqToEntity(Entity entity) {
        return super.method_1352(entity);
    }

    // ???
    /*public void superAttackTargetEntityWithCurrentItem(EntityBase entity) {
        super.attack(entity);
    }*/

    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_attackTargetEntityWithCurrentItem(Entity arg, CallbackInfo ci) {
        if (PlayerAPI.attackTargetEntityWithCurrentItem((PlayerEntity) (Object) this, arg))
            ci.cancel();
    }

    @Override
    public boolean isSubmergedInWater() {
        Boolean result = PlayerAPI.handleWaterMovement((PlayerEntity) (Object) this);
        if (result != null)
            return result;
        return super.isSubmergedInWater();
    }

    @Override
    public boolean superHandleWaterMovement() {
        return super.isSubmergedInWater();
    }

    @Override
    public boolean method_1335() {
        Boolean result = PlayerAPI.handleLavaMovement((PlayerEntity) (Object) this);
        if (result != null)
            return result;
        return super.method_1335();
    }

    @Override
    public boolean superHandleLavaMovement() {
        return super.method_1335();
    }

    // ???
    /*public void superDropPlayerItemWithRandomChoice(ItemInstance itemstack, boolean flag) {
        super.dropItem(itemstack, flag);
    }*/

    @Inject(
            method = "method_509",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stationapi_dropPlayerItemWithRandomChoice(ItemStack arg, boolean flag, CallbackInfo ci) {
        if (PlayerAPI.dropPlayerItemWithRandomChoice((PlayerEntity) (Object) this, arg, flag))
            ci.cancel();
    }

    @Inject(
            method = "method_946",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_applyDamage(int initialDamage, CallbackInfo ci) {
        if (PlayerAPI.damageEntity((PlayerEntity) (Object) this, initialDamage))
            ci.cancel();
    }

    @Override
    public void superUpdatePlayerActionState() {
        super.method_910();
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
}
