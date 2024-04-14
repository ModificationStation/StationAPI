package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import com.mojang.serialization.Dynamic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.EditWorldScreenEvent;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDetachedContext;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import net.modificationstation.stationapi.mixin.vanillafix.client.ScreenAccessor;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.LOW;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Environment(EnvType.CLIENT)
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class EditWorldScreenImpl {
    private static final String
            ROOT_KEY = "selectWorld." + NAMESPACE,
            CONVERT_TO_MCREGION_KEY = ROOT_KEY + ".convertToMcRegion";

    @EventListener(priority = LOW)
    private static void registerConversionButton(EditWorldScreenEvent.ScrollableButtonContextRegister event) {
        event.contexts.add(screen -> new ButtonWidgetDetachedContext(
                id -> {
                    ButtonWidget button = new ButtonWidget(id, 0, 0, I18n.getTranslation(CONVERT_TO_MCREGION_KEY));
                    button.active = NbtHelper.getDataVersions(((FlattenedWorldStorage) ((ScreenAccessor) screen).getMinecraft().method_2127()).getWorldTag(screen.worldData.method_1956())).contains(NAMESPACE.toString());
                    return button;
                },
                button -> ((ScreenAccessor) screen).getMinecraft().setScreen(new WarningScreen(screen, () -> {
                    Minecraft mc = ((ScreenAccessor) screen).getMinecraft();
                    mc.setScreen(null);
                    FlattenedWorldStorage worldStorage = (FlattenedWorldStorage) mc.method_2127();
                    mc.field_2817.method_1491("Converting World to " + worldStorage.getPreviousWorldFormat());
                    mc.field_2817.method_1796("This may take a while :)");
                    worldStorage.convertWorld(screen.worldData.method_1958(), (type, compound) -> (NbtCompound) VanillaDataFixerImpl.DATA_DAMAGER.get().update(type, new Dynamic<>(NbtOps.INSTANCE, compound).remove(DataFixers.DATA_VERSIONS), VanillaDataFixerImpl.HIGHEST_VERSION - NbtHelper.getDataVersions(compound).getInt(NAMESPACE.toString()), VanillaDataFixerImpl.VANILLA_VERSION).getValue(), mc.field_2817);
                    mc.setScreen(screen);
                }, WorldConversionWarning.TO_MCREGION_EXPLANATION_KEY, WorldConversionWarning.CONVERT_KEY))
        ));
    }
}
