package net.modificationstation.stationapi.impl.client.gui.screen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.menu.EditLevel;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.client.event.gui.screen.EditWorldScreenEvent;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDetachedContext;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.gui.client.ScreenBaseAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class EditWorldScreenImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerRenameWorld(EditWorldScreenEvent.ScrollableButtonContextRegister event) {
        event.contexts.add(screen -> new ButtonWidgetDetachedContext(
                id -> new Button(id, 0, 0, I18n.translate("selectWorld.rename")),
                button -> ((ScreenBaseAccessor) screen).getMinecraft().openScreen(new EditLevel(screen, screen.worldData.getLevelName()))
        ));
    }
}
