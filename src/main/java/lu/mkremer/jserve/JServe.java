package lu.mkremer.jserve;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.threading.SocketListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JServe {

    private final ServerConfiguration configuration;

    private final ExecutorService executorService;

    public JServe(ServerConfiguration configuration) {
        this.configuration = configuration;
        this.executorService = Executors.newFixedThreadPool(configuration.getMaxThreads());
    }

    public void start() {
        // executorService.execute(new SocketListener(this));
        new SocketListener(this).run();
    }

    public ServerConfiguration getConfiguration() {
        return configuration;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

}
