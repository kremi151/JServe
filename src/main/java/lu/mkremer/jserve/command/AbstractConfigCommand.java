package lu.mkremer.jserve.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.util.BuildVersion;
import org.tinylog.Logger;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class AbstractConfigCommand implements Callable<Integer> {

    @Option(names = {"-c", "--config-file"}, description = "The configuration file to load")
    protected File configFile;

    @Option(names = {"-p", "--port"}, description = "The port to serve the files on")
    protected Integer port;

    @Option(names = {"-d", "--directory", "--path"}, description = "The root path of the files to be served")
    protected String path;

    @Option(names = {"-m", "--max-threads"}, description = "The maximum amount of worker threads")
    protected Integer maxThreads;

    @Option(names = {"-t", "--mime-source", "--types"}, description = "The path to a comma separated values file of extension-to-mime-type mappings")
    protected String mimeSource;

    @Option(names = {"-l", "--logging-level"}, description = "Specifies the logging level (e.g. \"trace\", \"debug\", \"info\"")
    protected String loggingLevel;

    @Override
    public final Integer call() throws Exception {
        ServerConfiguration configuration;

        if (configFile != null) {
            configuration = loadConfigFromFile(configFile);
        } else {
            configuration = new ServerConfiguration();
            configuration.getPathMappers().add(new IndexPathMapper("index.html"));
        }

        if (port != null) {
            configuration.setPort(port);
        }
        if (path != null) {
            configuration.setServePath(path);
        }
        if (maxThreads != null) {
            configuration.setMaxThreads(maxThreads);
        }
        if (mimeSource != null) {
            configuration.setMimeSource(mimeSource);
        }
        if (loggingLevel != null) {
            configuration.setLoggingLevel(loggingLevel);
        }

        // Set logging level before logging anything
        if (configuration.getLoggingLevel() != null) {
            org.tinylog.configuration.Configuration.set("level", configuration.getLoggingLevel());
        }

        Logger.info("JServe version {}", BuildVersion.VERSION);
        if (configFile != null) {
            Logger.info("Loaded config from {}", configFile);
        }

        handleConfig(configuration);
        return 0;
    }

    protected abstract void handleConfig(ServerConfiguration config) throws Exception;

    private static ServerConfiguration loadConfigFromFile(File file) throws IOException {
        return new ObjectMapper().readValue(file, ServerConfiguration.class);
    }
}
