package net.modificationstation.stationapi.api.config;

import net.modificationstation.stationapi.impl.config.GlassYamlWrapper;

import java.lang.reflect.*;

/**
 * Implement in custom types. This listener does not support java builtins because I value what little remains of my sanity.
 */
public interface FieldModifiedListener {

    /**
     * @param field The field. Use the field name as the yaml key if setting values in there.
     * @param glassYamlWrapper The config file at the level the field is at.
     * @param eventSource {@link net.modificationstation.stationapi.impl.config.EventStorage.EventSource}
     */
    void fieldModified(Field field, GlassYamlWrapper glassYamlWrapper, int eventSource);
}
