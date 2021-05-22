package net.modificationstation.stationapi.impl.common;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.factory.EnumFactory;
import net.modificationstation.stationapi.api.factory.GeneralFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

/**
 * Temporary class to handle some setup that should be sorted into proper modules.
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class SortMe {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void setup(PreInitEvent event) {

        GeneralFactory generalFactory = GeneralFactory.INSTANCE;
        EnumFactory enumFactory = EnumFactory.INSTANCE;

        generalFactory.addFactory(EntityType.class, args -> enumFactory.addEnum(EntityType.class, (String) args[0], new Class[]{Class.class, int.class, Material.class, boolean.class}, new Object[]{args[1], args[2], args[3], args[4]}));
    }
}
