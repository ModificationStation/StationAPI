package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.client.resource.Resource;

import java.io.*;
import java.util.*;

@RequiredArgsConstructor
public class TextureAnimationData {

    public final int frametime;
    public final ImmutableList<Frame> frames;
    public final boolean interpolate;

    public static Optional<TextureAnimationData> parse(Resource resource) {
        if (resource.getMeta().isPresent()) {
            InputStream inputStream = resource.getMeta().get();
            JsonElement tmp = JsonParser.parseReader(new InputStreamReader(inputStream));
            if (tmp.isJsonObject()) {
                JsonObject meta = tmp.getAsJsonObject();
                if (meta.has("animation")) {
                    JsonObject animation = meta.getAsJsonObject("animation");
                    int frametime = animation.has("frametime") ? animation.getAsJsonPrimitive("frametime").getAsInt() : 1;
                    ImmutableList.Builder<Frame> frames = ImmutableList.builder();
                    if (animation.has("frames")) {
                        for (JsonElement element : animation.getAsJsonArray("frames")) {
                            if (element.isJsonPrimitive())
                                frames.add(new Frame(element.getAsInt(), frametime));
                            else if (element.isJsonObject()) {
                                JsonObject frame = element.getAsJsonObject();
                                frames.add(new Frame(frame.getAsJsonPrimitive("index").getAsInt(), frame.has("time") ? frame.getAsJsonPrimitive("time").getAsInt() : frametime));
                            } else
                                throw new RuntimeException("Unknown frame entry: " + element);
                        }
                    }
                    boolean interpolate = animation.has("interpolate") && animation.getAsJsonPrimitive("interpolate").getAsBoolean();
                    return Optional.of(new TextureAnimationData(frametime, frames.build(), interpolate));
                }
            }
        }
        return Optional.empty();
    }

    @RequiredArgsConstructor
    public static class Frame {

        public final int index;
        public final int time;
    }
}
