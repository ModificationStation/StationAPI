package net.modificationstation.sltest.render.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.model.Biped;
import net.modificationstation.sltest.entity.PoorGuy;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;

public class EntityRendererListener {

    @EventListener
    public void registerEntityRenderers(EntityRendererRegisterEvent event) {
        event.renderers.put(PoorGuy.class, new BipedEntityRenderer(new Biped(), 0.5f));
    }
}
