package net.modificationstation.stationapi.impl.resource;

import com.google.common.base.Functions;
import com.google.common.collect.*;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcePackManager {
    private final Set<ResourcePackProvider> providers;
    private Map<String, ResourcePackProfile> profiles = ImmutableMap.of();
    private List<ResourcePackProfile> enabled = ImmutableList.of();

    public ResourcePackManager(ResourcePackProvider... providers) {
        this.providers = ImmutableSet.copyOf(providers);
    }

    public void scanPacks() {
        List<String> list = this.enabled.stream().map(ResourcePackProfile::getName).collect(ImmutableList.toImmutableList());
        this.profiles = this.providePackProfiles();
        this.enabled = this.buildEnabledProfiles(list);
    }

    private Map<String, ResourcePackProfile> providePackProfiles() {
        Map<String, ResourcePackProfile> map = Maps.newTreeMap();

        for (ResourcePackProvider resourcePackProvider : this.providers)
            resourcePackProvider.register((profile) -> map.put(profile.getName(), profile));

        return ImmutableMap.copyOf(map);
    }

    public void setEnabledProfiles(Collection<String> enabled) {
        this.enabled = this.buildEnabledProfiles(enabled);
    }

    public boolean enable(String profile) {
        ResourcePackProfile resourcePackProfile = this.profiles.get(profile);
        if (resourcePackProfile != null && !this.enabled.contains(resourcePackProfile)) {
            List<ResourcePackProfile> list = Lists.newArrayList(this.enabled);
            list.add(resourcePackProfile);
            this.enabled = list;
            return true;
        } else return false;
    }

    public boolean disable(String profile) {
        ResourcePackProfile resourcePackProfile = this.profiles.get(profile);
        if (resourcePackProfile != null && this.enabled.contains(resourcePackProfile)) {
            List<ResourcePackProfile> list = Lists.newArrayList(this.enabled);
            list.remove(resourcePackProfile);
            this.enabled = list;
            return true;
        } else return false;
    }

    private List<ResourcePackProfile> buildEnabledProfiles(Collection<String> enabledNames) {
        List<ResourcePackProfile> list = this.streamProfilesByName(enabledNames).collect(Collectors.toList());

        for (ResourcePackProfile resourcePackProfile : this.profiles.values())
            if (resourcePackProfile.isAlwaysEnabled() && !list.contains(resourcePackProfile))
                resourcePackProfile.getInitialPosition().insert(list, resourcePackProfile, Functions.identity(), false);

        return ImmutableList.copyOf(list);
    }

    private Stream<ResourcePackProfile> streamProfilesByName(Collection<String> names) {
        return names.stream().map(Objects.requireNonNull(this.profiles)::get).filter(Objects::nonNull);
    }

    public Collection<String> getNames() {
        return this.profiles.keySet();
    }

    public Collection<ResourcePackProfile> getProfiles() {
        return this.profiles.values();
    }

    public Collection<String> getEnabledNames() {
        return this.enabled.stream().map(ResourcePackProfile::getName).collect(ImmutableSet.toImmutableSet());
    }

    public Collection<ResourcePackProfile> getEnabledProfiles() {
        return this.enabled;
    }

    @Nullable
    public ResourcePackProfile getProfile(String name) {
        return this.profiles.get(name);
    }

    public boolean hasProfile(String name) {
        return this.profiles.containsKey(name);
    }

    public List<ResourcePack> createResourcePacks() {
        return this.enabled.stream().map(ResourcePackProfile::createResourcePack).collect(ImmutableList.toImmutableList());
    }
}
