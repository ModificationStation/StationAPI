package net.modificationstation.stationapi.api.client.gl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public class GlBlendState {
	@Nullable
	private static GlBlendState activeBlendState;
	private final int srcRgb;
	private final int srcAlpha;
	private final int dstRgb;
	private final int dstAlpha;
	private final int func;
	private final boolean separateBlend;
	private final boolean blendDisabled;

	private GlBlendState(boolean separateBlend, boolean blendDisabled, int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, int func) {
		this.separateBlend = separateBlend;
		this.srcRgb = srcRgb;
		this.dstRgb = dstRgb;
		this.srcAlpha = srcAlpha;
		this.dstAlpha = dstAlpha;
		this.blendDisabled = blendDisabled;
		this.func = func;
	}

	public GlBlendState() {
		this(false, true, 1, 0, 1, 0, 32774);
	}

	public GlBlendState(int srcRgb, int dstRgb, int func) {
		this(false, false, srcRgb, dstRgb, srcRgb, dstRgb, func);
	}

	public GlBlendState(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, int func) {
		this(true, false, srcRgb, dstRgb, srcAlpha, dstAlpha, func);
	}

	public void enable() {
		if (!this.equals(activeBlendState)) {
			if (activeBlendState == null || this.blendDisabled != activeBlendState.isBlendDisabled()) {
				activeBlendState = this;
				if (this.blendDisabled) {
					RenderSystem.disableBlend();
					return;
				}

				RenderSystem.enableBlend();
			}

			RenderSystem.blendEquation(this.func);
			if (this.separateBlend) {
				RenderSystem.blendFuncSeparate(this.srcRgb, this.dstRgb, this.srcAlpha, this.dstAlpha);
			} else {
				RenderSystem.blendFunc(this.srcRgb, this.dstRgb);
			}

		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof GlBlendState glBlendState)) {
			return false;
		} else {
			if (this.func != glBlendState.func) {
				return false;
			} else if (this.dstAlpha != glBlendState.dstAlpha) {
				return false;
			} else if (this.dstRgb != glBlendState.dstRgb) {
				return false;
			} else if (this.blendDisabled != glBlendState.blendDisabled) {
				return false;
			} else if (this.separateBlend != glBlendState.separateBlend) {
				return false;
			} else if (this.srcAlpha != glBlendState.srcAlpha) {
				return false;
			} else {
				return this.srcRgb == glBlendState.srcRgb;
			}
		}
	}

	public int hashCode() {
		int i = this.srcRgb;
		i = 31 * i + this.srcAlpha;
		i = 31 * i + this.dstRgb;
		i = 31 * i + this.dstAlpha;
		i = 31 * i + this.func;
		i = 31 * i + (this.separateBlend ? 1 : 0);
		i = 31 * i + (this.blendDisabled ? 1 : 0);
		return i;
	}

	public boolean isBlendDisabled() {
		return this.blendDisabled;
	}

	public static int getFuncFromString(String name) {
		String string = name.trim().toLowerCase(Locale.ROOT);
		return switch (string) {
			case "add" -> 32774;
			case "subtract" -> 32778;
			case "reversesubtract", "reverse_subtract" -> 32779;
			case "min" -> 32775;
			default -> "max".equals(string) ? '耈' : '耆';
		};
	}

	public static int getComponentFromString(String expression) {
		String string = expression.trim().toLowerCase(Locale.ROOT);
		string = string.replaceAll("_", "");
		string = string.replaceAll("one", "1");
		string = string.replaceAll("zero", "0");
		string = string.replaceAll("minus", "-");
		return switch (string) {
			case "0" -> 0;
			case "1" -> 1;
			case "srccolor" -> 768;
			case "1-srccolor" -> 769;
			case "dstcolor" -> 774;
			case "1-dstcolor" -> 775;
			case "srcalpha" -> 770;
			case "1-srcalpha" -> 771;
			case "dstalpha" -> 772;
			default -> "1-dstalpha".equals(string) ? 773 : -1;
		};
	}
}
