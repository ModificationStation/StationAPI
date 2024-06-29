## Migrating from 2.0 to 3.0

A LOT of breaking changes happened between v2 and v3, the most important one being that GCAPI is now **part of StationAPI**, and proper config validation on load, along with in-game config editing for server admins.

Most breaking changes are package changes, and annotation changes, with only one major change happening elsewhere for proper validation on load.

- `@ConfigEntry` and `@ConfigCategory` now house all information about the field is on, removing the need for checking for 6 different annotations on load.
- `@GConfig` had it's `primary` value removed, and instead replaced with `priority`.
- 