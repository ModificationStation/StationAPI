package net.modificationstation.stationapi.api.util;

import java.lang.annotation.*;

/**
 * Marks methods, constructors and fields that are a part of API to suppress the "unused" warning.
 * @author mine_diver
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
public @interface API { }
