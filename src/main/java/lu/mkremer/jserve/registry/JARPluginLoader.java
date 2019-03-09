package lu.mkremer.jserve.registry;

import lu.mkremer.jserve.api.JServePlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JARPluginLoader {

    public static JServePlugin loadPluginFromJar(File jar) throws IOException {
        URLClassLoader child = new URLClassLoader(
                new URL[] {jar.toURI().toURL()},
                JARPluginLoader.class.getClassLoader()
        );

        JarFile jarFile = new JarFile(jar);
        Manifest manifest = jarFile.getManifest();

        Attributes attr = manifest.getMainAttributes();
        String jserve_plugin = attr.getValue("JServe-Plugin");
        if (jserve_plugin == null) {
            throw new IOException("No JServe-Plugin property defined in the Manifest of Jar file at " + jar.getAbsolutePath());
        }

        try {
            Class classToLoad = Class.forName(jserve_plugin, true, child);
            if (!JServePlugin.class.isAssignableFrom(classToLoad)) {
                throw new IOException("Plugin class at " + classToLoad.getName() + " does not extend " + JServePlugin.class.getName());
            }
            Constructor<? extends JServePlugin> constructor = classToLoad.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e);
        }
    }

    public static void collectPluginsAt(File path, PluginRegistry registry) throws IOException {
        if (!path.exists() || !path.isDirectory()) {
            return;
        }
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".jar")) {
                continue;
            }
            JServePlugin plugin = loadPluginFromJar(file);
            registry.registerPlugin(plugin);
        }
    }

}
