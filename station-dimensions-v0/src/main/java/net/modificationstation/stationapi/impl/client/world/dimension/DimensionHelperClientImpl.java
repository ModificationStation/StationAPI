package net.modificationstation.stationapi.impl.client.world.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.client.world.dimension.TravelMessageProvider;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperClientImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerEntity player, Identifier destination, double scale, PortalForcer travelAgent) {

        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        //noinspection deprecation
        Minecraft game = (Minecraft) FabricLoader.getInstance().getGameInstance();
        int overworldSerial = dimensions.getLegacyId(OVERWORLD).orElseThrow(() -> new IllegalStateException("Couldn't find overworld dimension in the registry!"));
        int destinationSerial = dimensions.getLegacyId(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        game.world.remove(player);
        player.dead = false;
        double var1 = player.x;
        double var3 = player.z;
        if (player.dimensionId == destinationSerial) {
            var1 *= scale;
            var3 *= scale;
            player.setPositionAndAnglesKeepPrevAngles(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.world.updateEntity(player, false);
            }

            Dimension dimension = Dimension.fromId(destinationSerial);
            World var10 = new World(game.world, dimension);
            game.setWorld(var10, I18n.getTranslation(dimension instanceof TravelMessageProvider provider ? provider.getEnteringTranslationKey() : "gui." + NAMESPACE + ".entering", destination), player);
        } else {
            var1 /= scale;
            var3 /= scale;
            player.setPositionAndAnglesKeepPrevAngles(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.world.updateEntity(player, false);
            }

            World var12 = new World(game.world, Dimension.fromId(overworldSerial));
            game.setWorld(var12, I18n.getTranslation(player.world.dimension instanceof TravelMessageProvider provider ? provider.getLeavingTranslationKey() : "gui." + NAMESPACE + ".leaving", destination), player);
        }

        player.world = game.world;
        if (player.isAlive()) {
            player.setPositionAndAnglesKeepPrevAngles(var1, player.y, var3, player.yaw, player.pitch);
            game.world.updateEntity(player, false);
            travelAgent.moveToPortal(game.world, player);
        }
    }
}
