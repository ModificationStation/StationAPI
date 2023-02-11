package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.client.event.gui.screen.EditWorldScreenEvent;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDetachedContext;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.vanillafix.client.ScreenBaseAccessor;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class EditWorldScreenImpl {

    private static final String
            ROOT_KEY = "selectWorld",
            CONVERT_TO_MCREGION_KEY = ROOT_KEY + "." + MODID.id("convertToMcRegion");

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerConversionButton(EditWorldScreenEvent.ScrollableButtonContextRegister event) {
        event.contexts.add(screen -> new ButtonWidgetDetachedContext(
                id -> new Button(id, 0, 0, I18n.translate(CONVERT_TO_MCREGION_KEY)),
                button -> ((ScreenBaseAccessor) screen).getMinecraft().openScreen(new WarningScreen(screen, () -> {}))
        ));
    }
}
