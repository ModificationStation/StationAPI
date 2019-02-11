package net.modificationstation.stationloader.common.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	public String name() default "";
	public String version() default "";
	public String modid();
	boolean clientSideOnly() default false;
	boolean serverSideOnly() default false;
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface EventHandler {}
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Instance {
        String value() default "";
        String owner() default "";
	};
}
