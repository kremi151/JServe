package lu.mkremer.jserve.registry;

import lu.mkremer.jserve.api.JServePlugin;
import lu.mkremer.jserve.api.registration.PathMapperRegistry;
import lu.mkremer.jserve.exception.DuplicateEntryException;

import java.util.HashMap;
import java.util.Map;

public class PluginRegistry {

    private final HashMap<String, JServePlugin> plugins = new HashMap<>();

    public synchronized void registerPlugin(JServePlugin plugin) {
        if (plugins.containsKey(plugin.getId())) {
            throw new DuplicateEntryException("A plugin with id " + plugin.getId() + " is already registred");
        }
        plugins.put(plugin.getId(), plugin);
    }

    public synchronized void onRegisterMappers(PathMapperRegistry registry) {
        for (Map.Entry<String, JServePlugin> entry : plugins.entrySet()) {
            entry.getValue().onRegisterMappers(registry);
        }
    }
}
