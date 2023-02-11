package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.level.storage.LevelMetadata;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.level.storage.StationFlatteningWorldStorage;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldConversionWarning {

    public static final String
            ROOT_KEY = MODID.id("worldConversion").toString(),
            FROM_MCREGION_EXPLANATION_KEY = ROOT_KEY + "." + MODID.id("fromMcRegionExplanation"),
            TO_MCREGION_EXPLANATION_KEY = ROOT_KEY + "." + MODID.id("toMcRegionExplanation"),
            CONVERT_KEY = ROOT_KEY + "." + MODID.id("convert");

    public static void warnIfMcRegion(Minecraft minecraft, ScreenBase parentScreen, LevelMetadata worldData, Runnable loadWorld) {
        if (NbtHelper.getDataVersions(((StationFlatteningWorldStorage) minecraft.getLevelStorage()).getWorldTag(worldData.getFileName())).containsKey(MODID.toString()))
            loadWorld.run();
        else minecraft.openScreen(new WarningScreen(parentScreen, loadWorld, FROM_MCREGION_EXPLANATION_KEY, CONVERT_KEY));
    }
}
