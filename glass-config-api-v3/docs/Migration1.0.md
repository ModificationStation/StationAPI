## Migrating from 1.0 to 2.0

Some breaking changes happened between these two versions, mostly for config factories, save/load listeners, and custom config entries.

Anything that mixes into GCCore will likely no longer work, due to some invasive changes.

NEW:
- `ConfigEntry`
    - `reset(Ljava/lang/Object)V`
        - Used when resetting to a specified value.
    - `vanillaServerBehavior()V`
        - Called when joining a vanilla server. Make sure to check for the vanilla server annotations if you don't want to always do something on vanilla servers.
- `@DefaultOnVanillaServer`
    - Resets the given field to default on joining a vanilla server.
- `@ValueOnVanillaServer`
    - `stringValue`, `booleanValue` (`TriBoolean`), `intValue`, `floatValue`.
        - Fill one of these out, and it will be applied to the config field on joining a vanilla server.
        - Specifying an invalid value WILL result in a crash.
- `TriBoolean`
    - Used in `@ValueOnVanillaServer`.
    - Has three states: `TRUE`, `FALSE`, `DEFAULT` (`null`).
- `ConfigFactoryProvider`
    - `provideLoadTypeAdapterFactories(ImmutableMap.Builder)V`
        - An optional method for providing load type adapters. See `ExampleConfigEnumFactories` for an example.
- `GeneratedConfig`
    - Implement this interface in your config classes to allow for picking the fields you want to load, and if you want the class itself to be loaded.

CHANGED:
- `GCCore`
    - Saving and loading got changed somewhat drastically. Reduced copy-paste code to almost nothing.
- `ConfigFactoryProvider`
    - `provideLoadFactories`
        - Added a new parameter. The function is now `NonFunction`, and the new parameter is the default value, positioned after value.
- `ConfigEntry`
    - `saveToField()V` is now used whenever saving the value stored to the config field.
    - `init(Screen,TextRenderer)V` no longer handles adding icons indicating server sync.
- `PreConfigSavedListener` and `PostConfigLoadedListener`
    - A new `source` parameter was added.
    - See `EventStorage.EventSource` for more information.
