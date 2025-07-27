package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.storage.WorldSaveInfo;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldConversionWarning {
    public static final String
            ROOT_KEY = "gui." + NAMESPACE + ".worldConversion",
            FROM_MCREGION_EXPLANATION_KEY = ROOT_KEY + ".fromMcRegionExplanation",
            TO_MCREGION_EXPLANATION_KEY = ROOT_KEY + ".toMcRegionExplanation",
            CONVERT_KEY = ROOT_KEY + ".convert";

    public static void warnIfMcRegion(Minecraft minecraft, Screen parentScreen, WorldSaveInfo worldData, Runnable loadWorld) {
        if (NbtHelper.getDataVersions(((FlattenedWorldStorage) minecraft.getWorldStorageSource()).getWorldTag(worldData.getSaveName())).contains(NAMESPACE.toString()))
            loadWorld.run();
        else minecraft.setScreen(new WarningScreen(parentScreen, loadWorld, FROM_MCREGION_EXPLANATION_KEY, CONVERT_KEY));
    }
}
