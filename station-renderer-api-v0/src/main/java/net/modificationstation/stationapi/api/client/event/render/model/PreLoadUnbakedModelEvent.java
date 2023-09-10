package net.modificationstation.stationapi.api.client.event.render.model;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.function.ExceptionalFunction;

import java.io.IOException;

@SuperBuilder
public class PreLoadUnbakedModelEvent extends Event {
    public final Identifier identifier;
    public final ModelLoader modelLoader;
    public ExceptionalFunction<Identifier, UnbakedModel, IOException> loader;
}
