package net.modificationstation.stationapi.impl.client.level.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_467;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.modificationstation.stationapi.api.client.level.dimension.TravelMessageProvider;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperClientImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerBase player, Identifier destination, double scale, class_467 travelAgent) {

        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        //noinspection deprecation
        Minecraft game = (Minecraft) FabricLoader.getInstance().getGameInstance();
        int overworldSerial = dimensions.getSerialID(OVERWORLD).orElseThrow(() -> new IllegalStateException("Couldn't find overworld dimension in the registry!"));
        int destinationSerial = dimensions.getSerialID(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        game.level.removeEntity(player);
        player.removed = false;
        double var1 = player.x;
        double var3 = player.z;
        if (player.dimensionId == destinationSerial) {
            var1 = var1 * scale;
            var3 = var3 * scale;
            player.setPositionAndAngles(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.level.method_193(player, false);
            }

            Dimension dimension = Dimension.getByID(destinationSerial);
            Level var10 = new Level(game.level, dimension);
            game.showLevelProgress(var10, I18n.translate(dimension instanceof TravelMessageProvider ? ((TravelMessageProvider) dimension).getEnteringTranslationKey() : "gui." + Identifier.of(MODID, "entering"), destination), player);
        } else {
            var1 = var1 / scale;
            var3 = var3 / scale;
            player.setPositionAndAngles(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.level.method_193(player, false);
            }

            Dimension dimension = Dimension.getByID(overworldSerial);
            Level var12 = new Level(game.level, dimension);
            game.showLevelProgress(var12, I18n.translate(dimension instanceof TravelMessageProvider ? ((TravelMessageProvider) dimension).getLeavingTranslationKey() : "gui." + Identifier.of(MODID, "leaving"), destination), player);
        }

        player.level = game.level;
        if (player.isAlive()) {
            player.setPositionAndAngles(var1, player.y, var3, player.yaw, player.pitch);
            game.level.method_193(player, false);
            travelAgent.method_1530(game.level, player);
        }
    }
}
