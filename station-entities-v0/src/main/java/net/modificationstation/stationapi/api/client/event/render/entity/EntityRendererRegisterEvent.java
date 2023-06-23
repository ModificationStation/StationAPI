package net.modificationstation.stationapi.api.client.event.render.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;

import java.util.Map;

@SuperBuilder
public class EntityRendererRegisterEvent extends Event {
    public final Map<Class<? extends EntityBase>, EntityRenderer> renderers;
}
