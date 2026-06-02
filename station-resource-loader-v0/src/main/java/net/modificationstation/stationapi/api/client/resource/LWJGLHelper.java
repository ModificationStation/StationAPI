package net.modificationstation.stationapi.api.client.resource;

import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

/**
 * Utility class to handle LWJGL2 vs LWJGL3/starac differences.
 *
 * For LWJGL2: Uses SharedDrawable for threaded context sharing.
 * For LWJGL3/starac: Uses EarlyRenderLoop for single-threaded rendering with proper event handling.
 */
final class LWJGLHelper {
    private static final boolean HAS_EARLY_RENDER_LOOP;
    private static final boolean IS_LWJGL2;

    static {
        boolean hasEarlyRenderLoop = false;
        boolean isLwjgl2 = false;

        // First check if starac's EarlyRenderLoop exists (preferred for LWJGL3)
        try {
            Class.forName("org.lwjgl.opengl.EarlyRenderLoop");
            hasEarlyRenderLoop = true;
        } catch (ClassNotFoundException e) {
            // Not available
        }

        // Check if this is LWJGL2 (has Drawable/SharedDrawable)
        if (!hasEarlyRenderLoop) {
            try {
                Class.forName("org.lwjgl.opengl.Drawable");
                Class.forName("org.lwjgl.opengl.SharedDrawable");
                Class<?> displayClass = Class.forName("org.lwjgl.opengl.Display");
                displayClass.getMethod("getDrawable");
                isLwjgl2 = true;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                // Not LWJGL2
            }
        }

        HAS_EARLY_RENDER_LOOP = hasEarlyRenderLoop;
        IS_LWJGL2 = isLwjgl2;
    }

    private LWJGLHelper() {}

    /**
     * @return true if starac's EarlyRenderLoop is available
     */
    public static boolean hasEarlyRenderLoop() {
        return HAS_EARLY_RENDER_LOOP;
    }

    /**
     * @return true if LWJGL2's SharedDrawable is available
     */
    public static boolean isLWJGL2() {
        return IS_LWJGL2;
    }

    /**
     * Runs an early render loop using starac's EarlyRenderLoop.
     * This runs on the main thread with proper event handling.
     *
     * @param shouldContinue Returns true while the loop should keep running
     * @param render Called each frame to perform rendering
     */
    public static void runEarlyRenderLoop(BooleanSupplier shouldContinue, Runnable render) throws Exception {
        if (!HAS_EARLY_RENDER_LOOP) {
            throw new IllegalStateException("EarlyRenderLoop not available");
        }

        Class<?> earlyRenderLoopClass = Class.forName("org.lwjgl.opengl.EarlyRenderLoop");
        Method runLoop = earlyRenderLoopClass.getMethod("runLoop", BooleanSupplier.class, Runnable.class);
        runLoop.invoke(null, shouldContinue, render);
    }

    /**
     * Creates a SharedDrawable for LWJGL2 threaded context sharing.
     */
    public static Object createSharedDrawable() throws Exception {
        if (!IS_LWJGL2) {
            throw new IllegalStateException("SharedDrawable not available (not LWJGL2)");
        }

        Class<?> displayClass = Class.forName("org.lwjgl.opengl.Display");
        Class<?> sharedDrawableClass = Class.forName("org.lwjgl.opengl.SharedDrawable");
        Class<?> drawableClass = Class.forName("org.lwjgl.opengl.Drawable");

        Method getDrawable = displayClass.getMethod("getDrawable");
        Object drawable = getDrawable.invoke(null);

        return sharedDrawableClass.getConstructor(drawableClass).newInstance(drawable);
    }

    /**
     * Makes the drawable current in the current thread (LWJGL2 only).
     */
    public static void makeCurrent(Object drawable) throws Exception {
        if (drawable == null) return;

        Class<?> drawableClass = Class.forName("org.lwjgl.opengl.Drawable");
        Method makeCurrent = drawableClass.getMethod("makeCurrent");
        makeCurrent.invoke(drawable);
    }

    /**
     * Releases the context from the current thread (LWJGL2 only).
     */
    public static void releaseContext(Object drawable) throws Exception {
        if (drawable == null) return;

        Class<?> drawableClass = Class.forName("org.lwjgl.opengl.Drawable");
        Method releaseContext = drawableClass.getMethod("releaseContext");
        releaseContext.invoke(drawable);
    }

    /**
     * Calls Display.update() for LWJGL2.
     */
    public static void displayUpdate() throws Exception {
        Class<?> displayClass = Class.forName("org.lwjgl.opengl.Display");
        Method update = displayClass.getMethod("update");
        update.invoke(null);
    }
}
