package lu.mkremer.jserve;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.CSVReader;
import lu.mkremer.jserve.threading.SocketListener;
import lu.mkremer.jserve.threading.SocketResponder;
import lu.mkremer.jserve.util.MimeContext;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public void start() throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownThreads));

        CSVReader csvReader;
        if (configuration.getMimeSource() == null || configuration.getMimeSource().equals(ServerConfiguration.DEFAULT_MIME_FILE)) {
            ClassLoader classLoader = JServe.class.getClassLoader();
            csvReader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("mime/types.csv")));
        } else {
            csvReader = new CSVReader(new FileReader(configuration.getMimeSource()));
        }

        try {
            MimeContext.getInstance().loadMimeTypes(csvReader);
        } finally {
            csvReader.close();
        }

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
