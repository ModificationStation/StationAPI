package net.modificationstation.stationapi.api.client.event.render.entity;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class EntityRendererRegister extends Event {

    public final Map<Class<? extends EntityBase>, EntityRenderer> renderers;
}
