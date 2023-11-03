package net.modificationstation.stationapi.api.client.gui.widget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import net.minecraft.client.gui.widget.ButtonWidget;

public interface ButtonWidgetContextAttacher {

    static ButtonWidgetContextAttacher toListDeconstructed(List<ButtonWidget> buttons, List<PressAction> actions) {
        return toDeconstructed(() -> {
            int buttonSize = buttons.size();
            int actionSize = actions.size();
            if (buttonSize != actionSize) throw new IllegalStateException();
            return buttonSize;
        }, buttons::add, actions::add);
    }

    static ButtonWidgetContextAttacher toList(List<ButtonWidgetAttachedContext> contexts) {
        return to(contexts::size, contexts::add);
    }

    static ButtonWidgetContextAttacher to(IntSupplier nextId, Consumer<ButtonWidgetAttachedContext> postProcess) {
        return detachedContext -> {
            ButtonWidgetAttachedContext attachedContext = detachedContext.attach(nextId.getAsInt());
            postProcess.accept(attachedContext);
            return attachedContext;
        };
    }

    static ButtonWidgetContextAttacher toDeconstructed(IntSupplier nextId, Consumer<ButtonWidget> buttonPostProcess, Consumer<PressAction> actionPostProcess) {
        return to(nextId, attachedContext -> {
            buttonPostProcess.accept(attachedContext.button());
            actionPostProcess.accept(attachedContext.action());
        });
    }

    ButtonWidgetAttachedContext attach(ButtonWidgetDetachedContext context);

    default ButtonWidgetAttachedContext attach(ButtonWidgetFactory buttonFactory, PressAction action) {
        return attach(new ButtonWidgetDetachedContext(buttonFactory, action));
    }
}
