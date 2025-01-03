package net.modificationstation.stationapi.mixin.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.impl.effect.EffectsRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends HandledScreen {
	@Unique private final EffectsRenderer stationapi_effectRenderer = new EffectsRenderer();
	
	public MixinInventoryScreen(ScreenHandler container) {
		super(container);
	}
	
	@Inject(method = "drawBackground", at = @At("TAIL"))
	private void stationapi_renderEffects(float delta, CallbackInfo info) {
		int offset = FabricLoader.getInstance().isModLoaded("hmifabric") ? 24 : 2;
		stationapi_effectRenderer.renderEffects(minecraft, offset, delta, true);
	}
}
