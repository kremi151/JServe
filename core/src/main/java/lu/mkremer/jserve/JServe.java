package lu.mkremer.jserve;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.threading.SocketListener;
import lu.mkremer.jserve.threading.SocketResponder;
import org.tinylog.Logger;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JServe {

    private final ServerConfiguration configuration;

    private final ExecutorService executorService;

    public JServe(ServerConfiguration configuration) {
        this.configuration = configuration;
        this.executorService = Executors.newFixedThreadPool(configuration.getMaxThreads());
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownThreads));

        new SocketListener(this).run();
    }

    public ServerConfiguration getConfiguration() {
        return configuration;
    }

    public void handleSocket(Socket socket) {
        executorService.execute(new SocketResponder(socket, configuration));
    }

    private void shutdownThreads() {
        Logger.debug("Shutting down worker threads...");
        executorService.shutdown();
        try {
            if (executorService.awaitTermination(2L, TimeUnit.SECONDS)) {
                Logger.debug("Worker threads shut down");
            } else {
                Logger.debug("Worker threads could not be shut down in time");
            }
        } catch (InterruptedException e) {
            Logger.debug(e, "Worker threads could not be shut down");
        }
    }

}
