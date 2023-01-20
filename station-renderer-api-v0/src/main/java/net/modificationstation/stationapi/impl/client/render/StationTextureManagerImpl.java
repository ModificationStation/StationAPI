package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.resource.ClientResources;
import net.modificationstation.stationapi.api.client.texture.*;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import java.io.IOException;
import java.util.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public final class StationTextureManagerImpl implements StationTextureManager {

    public static final Identifier MISSING_IDENTIFIER = Identifier.of("");

    private final TextureManagerAccessor textureManagerAccessor;
    private final Map<Identifier, AbstractTexture> textures = new HashMap<>();
    private final Set<TextureTickListener> tickListeners = new HashSet<>();
    private final Set<TextureTickListener> tickListenersView = Collections.unmodifiableSet(tickListeners);
    private final Map<String, Integer> dynamicIdCounters = new HashMap<>();

    public StationTextureManagerImpl(TextureManager textureManager) {
        textureManagerAccessor = (TextureManagerAccessor) textureManager;
    }

    @Override
    public void registerTexture(Identifier identifier, AbstractTexture texture) {
        texture = this.loadSafely(identifier, texture);
        AbstractTexture abstractTexture2 = this.textures.put(identifier, texture);
        if (abstractTexture2 != texture) {
            if (abstractTexture2 != null && abstractTexture2 != MissingSprite.getMissingSpriteTexture()) {
                //noinspection SuspiciousMethodCalls
                this.tickListeners.remove(abstractTexture2);
                this.close(identifier, abstractTexture2);
            }

            if (texture instanceof TextureTickListener listener) {
                this.tickListeners.add(listener);
            }
        }
    }

    private void close(Identifier identifier, AbstractTexture abstractTexture) {
        if (abstractTexture != MissingSprite.getMissingSpriteTexture()) {
            try {
                abstractTexture.close();
            } catch (Exception var4) {
                LOGGER.warn("Failed to close texture {}", identifier, var4);
            }
        }

        abstractTexture.clearGlId();
    }

    private AbstractTexture loadSafely(Identifier identifier, AbstractTexture abstractTexture) {
        try {
            abstractTexture.load(ClientResources.getResourceManager());
            return abstractTexture;
        } catch (IOException var6) {
            if (identifier != MISSING_IDENTIFIER) {
                LOGGER.warn("Failed to load texture: {}", identifier, var6);
            }

            return MissingSprite.getMissingSpriteTexture();
        } catch (Throwable var7) {
            CrashReport crashReport = CrashReport.create(var7, "Registering texture");
            CrashReportSection crashReportSection = crashReport.addElement("Resource location being registered");
            crashReportSection.add("Resource location", identifier);
            crashReportSection.add("Texture object class", () -> abstractTexture.getClass().getName());
            throw new CrashException(crashReport);
        }
    }

    @Override
    public void bindTexture(Identifier id) {
        this.bindTextureInner(id);
    }

    private void bindTextureInner(Identifier id) {
        AbstractTexture abstractTexture = this.textures.get(id);
        if (abstractTexture == null) {
            abstractTexture = new ResourceTexture(id);
            this.registerTexture(id, abstractTexture);
        }

        abstractTexture.bindTexture();
    }

    @Override
    public Set<TextureTickListener> getTickListeners() {
        return tickListenersView;
    }

    @Override
    public AbstractTexture getTexture(Identifier id) {
        AbstractTexture abstractTexture = this.textures.get(id);
        if (abstractTexture == null) {
            abstractTexture = new ResourceTexture(id);
            this.registerTexture(id, abstractTexture);
        }
        return abstractTexture;
    }

    @Override
    public Identifier registerDynamicTexture(String prefix, NativeImageBackedTexture texture) {
        Integer integer = this.dynamicIdCounters.get(prefix);
        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }
        this.dynamicIdCounters.put(prefix, integer);
        Identifier identifier = Identifier.of(String.format(Locale.ROOT, "dynamic/%s_%d", prefix, integer));
        this.registerTexture(identifier, texture);
        return identifier;
    }

    public static StationTextureManagerImpl get(TextureManager textureManager) {
        return ((StationTextureManagerAccess) textureManager).stationapi$stationTextureManager();
    }

    public interface StationTextureManagerAccess {

        StationTextureManagerImpl stationapi$stationTextureManager();
    }
}
