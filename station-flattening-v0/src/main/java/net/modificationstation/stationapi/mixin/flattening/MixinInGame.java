package net.modificationstation.stationapi.mixin.flattening;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

@Mixin(InGame.class)
public abstract class MixinInGame extends DrawableHelper {
	@Shadow private Minecraft minecraft;
	
	@Unique
	@Inject(
		method = "renderHud(FZII)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/Runtime;maxMemory()J", shift = Shift.BEFORE),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void stapi_renderHud(float bl, boolean i, int j, int par4, CallbackInfo ci, ScreenScaler scaler, int var6, int var7, TextRenderer var8) {
		HitResult hit = minecraft.hitResult;
		int offset = 22;
		if (hit != null && hit.type == HitType.field_789) {
			BlockState state = ((BlockStateView) minecraft.level).getBlockState(hit.x, hit.y, hit.z);
			
			String text = "Block: " + state.getBlock().getTranslatedName();
			drawTextWithShadow(var8, text, var6 - var8.getTextWidth(text) - 2, offset += 10, 16777215);
			
			text = "Meta: " + minecraft.level.getTileMeta(hit.x, hit.y, hit.z);
			drawTextWithShadow(var8, text, var6 - var8.getTextWidth(text) - 2, offset += 10, 16777215);
			
			Collection<Property<?>> properties = state.getProperties();
			if (properties.size() > 1) {
				text = "Properties:";
				drawTextWithShadow(var8, text, var6 - var8.getTextWidth(text) - 2, offset += 10, 16777215);
				
				Iterator<Property<?>> iterator = properties.iterator();
				while (iterator.hasNext()) {
					text = iterator.next().toString();
					drawTextWithShadow(var8, text, var6 - var8.getTextWidth(text) - 2, offset += 10, 14737632);
				}
			}
			
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				TileEntityBase entity = minecraft.level.getTileEntity(hit.x, hit.y, hit.z);
				if (entity != null) {
					String className = entity.getClass().getName();
					text = "Tile Entity: " + className.substring(className.lastIndexOf('.') + 1);
					drawTextWithShadow(var8, text, var6 - var8.getTextWidth(text) - 2, offset + 20, 16777215);
				}
			}
		}
	}
}
