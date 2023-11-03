package net.modificationstation.stationapi.impl.client.level.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_467;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.client.level.dimension.TravelMessageProvider;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperClientImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerEntity player, Identifier destination, double scale, class_467 travelAgent) {

        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        //noinspection deprecation
        Minecraft game = (Minecraft) FabricLoader.getInstance().getGameInstance();
        int overworldSerial = dimensions.getLegacyId(OVERWORLD).orElseThrow(() -> new IllegalStateException("Couldn't find overworld dimension in the registry!"));
        int destinationSerial = dimensions.getLegacyId(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        game.world.method_231(player);
        player.dead = false;
        double var1 = player.x;
        double var3 = player.z;
        if (player.dimensionId == destinationSerial) {
            var1 *= scale;
            var3 *= scale;
            player.method_1341(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.world.method_193(player, false);
            }

            Dimension dimension = Dimension.method_1767(destinationSerial);
            World var10 = new World(game.world, dimension);
            game.method_2115(var10, I18n.getTranslation(dimension instanceof TravelMessageProvider provider ? provider.getEnteringTranslationKey() : "gui." + of(MODID, "entering"), destination), player);
        } else {
            var1 /= scale;
            var3 /= scale;
            player.method_1341(var1, player.y, var3, player.yaw, player.pitch);
            if (player.isAlive()) {
                game.world.method_193(player, false);
            }

            World var12 = new World(game.world, Dimension.method_1767(overworldSerial));
            game.method_2115(var12, I18n.getTranslation(player.world.dimension instanceof TravelMessageProvider provider ? provider.getLeavingTranslationKey() : "gui." + of(MODID, "leaving"), destination), player);
        }

        player.world = game.world;
        if (player.isAlive()) {
            player.method_1341(var1, player.y, var3, player.yaw, player.pitch);
            game.world.method_193(player, false);
            travelAgent.method_1530(game.world, player);
        }
    }
}
