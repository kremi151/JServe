package lu.mkremer.jserve.api;

import lu.mkremer.jserve.api.registration.PathMapperRegistry;

public abstract class JServePlugin {

    private final String id;

    protected JServePlugin(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void onRegisterMappers(PathMapperRegistry registry) {}

}
