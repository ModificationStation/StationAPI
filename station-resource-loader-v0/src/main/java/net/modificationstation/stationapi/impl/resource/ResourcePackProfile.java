package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.metadata.PackResourceMetadata;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * Represents a resource pack in a {@link ResourcePackManager}.
 * 
 * <p>Compared to a single-use {@link ResourcePack}, a profile is persistent
 * and serves as {@linkplain #createResourcePack a factory} for the single-use
 * packs. It also contains user-friendly information about resource packs.
 * 
 * <p>The profiles are registered by {@link ResourcePackProvider}s.
 * 
 * <p>Closing the profile doesn't have any effect.
 */
public class ResourcePackProfile {
    private final String name;
    private final PackFactory packFactory;
    private final String displayName;
    private final String description;
    private final ResourcePackCompatibility compatibility;
    private final InsertionPosition position;
    private final boolean alwaysEnabled;
    private final boolean pinned;
    private final ResourcePackSource source;

    @Nullable
    public static ResourcePackProfile create(String name, String displayName, boolean alwaysEnabled, PackFactory packFactory, ResourceType type, InsertionPosition position, ResourcePackSource source) {
        Metadata metadata = ResourcePackProfile.loadMetadata(name, packFactory);
        return metadata != null ? ResourcePackProfile.of(name, displayName, alwaysEnabled, packFactory, metadata, type, position, false, source) : null;
    }

    /**
     * Creates a resource pack profile from the given parameters.
     * 
     * <p>Compared to calling the factory directly, this utility method obtains the
     * pack's metadata information from the pack created by the {@code packFactory}.
     * If the created pack doesn't have metadata information, this method returns
     * {@code null}.
     * 
     * @return the created profile, or {@code null} if missing metadata
     */
    public static ResourcePackProfile of(String name, String displayName, boolean alwaysEnabled, PackFactory packFactory, Metadata metadata, ResourceType type, InsertionPosition position, boolean pinned, ResourcePackSource source) {
        return new ResourcePackProfile(name, alwaysEnabled, packFactory, displayName, metadata, metadata.getCompatibility(type), position, pinned, source);
    }

    private ResourcePackProfile(String name, boolean alwaysEnabled, PackFactory packFactory, String displayName, Metadata metadata, ResourcePackCompatibility compatibility, InsertionPosition position, boolean pinned, ResourcePackSource source) {
        this.name = name;
        this.packFactory = packFactory;
        this.displayName = displayName;
        this.description = metadata.description();
        this.compatibility = compatibility;
        this.alwaysEnabled = alwaysEnabled;
        this.position = position;
        this.pinned = pinned;
        this.source = source;
    }

    @Nullable
    public static Metadata loadMetadata(String name, PackFactory packFactory) {
        try (ResourcePack resourcePack = packFactory.open(name)){
            PackResourceMetadata packResourceMetadata = resourcePack.parseMetadata(PackResourceMetadata.SERIALIZER);
            if (packResourceMetadata == null) {
                LOGGER.warn("Missing metadata in pack {}", name);
                return null;
            }
            return new Metadata(packResourceMetadata.getDescription(), packResourceMetadata.getPackFormat());
        } catch (Exception exception) {
            LOGGER.warn("Failed to read pack metadata", exception);
            return null;
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getInformationText(boolean enabled) {
//        return Texts.bracketed(this.source.decorate(Text.literal(this.name))).styled(style -> style.withColor(enabled ? Formatting.GREEN : Formatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.name)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.empty().append(this.displayName).append("\n").append(this.description))));
        return "fixText";
    }

    public ResourcePackCompatibility getCompatibility() {
        return this.compatibility;
    }

    public ResourcePack createResourcePack() {
        return this.packFactory.open(this.name);
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlwaysEnabled() {
        return this.alwaysEnabled;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public InsertionPosition getInitialPosition() {
        return this.position;
    }

    public ResourcePackSource getSource() {
        return this.source;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourcePackProfile resourcePackProfile)) return false;
        return this.name.equals(resourcePackProfile.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    @FunctionalInterface
    public interface PackFactory {
        ResourcePack open(String var1);
    }

    public record Metadata(String description, int format) {
        public ResourcePackCompatibility getCompatibility(ResourceType type) {
            return ResourcePackCompatibility.from(this.format, type);
        }
    }

    public enum InsertionPosition {
        TOP,
        BOTTOM;


        public <T> int insert(List<T> items, T item, Function<T, ResourcePackProfile> profileGetter, boolean listInverted) {
            ResourcePackProfile resourcePackProfile;
            int i;
            InsertionPosition insertionPosition = listInverted ? this.inverse() : this;
            if (insertionPosition == BOTTOM) {
                ResourcePackProfile resourcePackProfile2;
                int i2;
                i2 = 0;
                while (i2 < items.size() && (resourcePackProfile2 = profileGetter.apply(items.get(i2))).isPinned() && resourcePackProfile2.getInitialPosition() == this)
                    ++i2;
                items.add(i2, item);
                return i2;
            }
            i = items.size() - 1;
            while (i >= 0 && (resourcePackProfile = profileGetter.apply(items.get(i))).isPinned() && resourcePackProfile.getInitialPosition() == this)
                --i;
            items.add(i + 1, item);
            return i + 1;
        }

        public InsertionPosition inverse() {
            return this == TOP ? BOTTOM : TOP;
        }
    }
}

