package net.modificationstation.stationapi.api.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPath {
    private static Method ADD_URL;

    static {
        try {
            ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addFile(String s) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        File f = new File(s);
        addFile(f);
    }

    public static void addFile(File f) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        addURL(f.toURI().toURL());
    }

    public static void addURL(URL u) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ADD_URL.invoke(ClassPath.class.getClassLoader().getParent(), u);
    }

    public static URL[] getURLs() {
        return getURLs(ClassPath.class.getClassLoader());
    }

    public static URL[] getURLs(ClassLoader classLoader) {
        if(classLoader instanceof URLClassLoader) {
            return ((URLClassLoader) classLoader).getURLs();
        }
        return classLoader.getParent() == null ? new URL[0] : getURLs(classLoader.getParent());
    }
}
