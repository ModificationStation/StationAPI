package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;

/**
 * PreInitialization event called for mods to do some set up involving adding new StAPI events, JSON recipe parsers, etc...
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 * @see EventRegistry
 * @see JsonRecipeParserRegistry
 */
public class PreInit extends Event { }
