package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.class_591;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldConversionWarning {
    public static final String
            ROOT_KEY = NAMESPACE.id("worldConversion").toString(),
            FROM_MCREGION_EXPLANATION_KEY = ROOT_KEY + "." + NAMESPACE.id("fromMcRegionExplanation"),
            TO_MCREGION_EXPLANATION_KEY = ROOT_KEY + "." + NAMESPACE.id("toMcRegionExplanation"),
            CONVERT_KEY = ROOT_KEY + "." + NAMESPACE.id("convert");

    public static void warnIfMcRegion(Minecraft minecraft, Screen parentScreen, class_591 worldData, Runnable loadWorld) {
        if (NbtHelper.getDataVersions(((FlattenedWorldStorage) minecraft.method_2127()).getWorldTag(worldData.method_1956())).contains(NAMESPACE.toString()))
            loadWorld.run();
        else minecraft.setScreen(new WarningScreen(parentScreen, loadWorld, FROM_MCREGION_EXPLANATION_KEY, CONVERT_KEY));
    }
}
