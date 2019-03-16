package net.modificationstation.stationloader.coremod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.modificationstation.classloader.Side;

/**
 * This annotation marks type, field, method, or constructor with it's side (Side.SERVER or Side.CLIENT)
 * If value doesn't match the current side, SideTransformer will remove it from bytecode
 * 
 * @author mine_diver
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface SideOnly
{
    public Side value();
}
