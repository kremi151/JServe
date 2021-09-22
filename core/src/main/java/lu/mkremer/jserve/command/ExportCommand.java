package lu.mkremer.jserve.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lu.mkremer.jserve.conf.ServerConfiguration;
import org.tinylog.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;

@Command(name = "export")
public class ExportCommand extends AbstractConfigCommand {

    @Parameters(index = "0", description = "The target path to export the configuration to")
    private File destination;

    @Option(names = {"--pretty"}, description = "Whether to pretty print the exported configuration")
    private boolean pretty = false;

    public ExportCommand(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void handleConfig(ServerConfiguration config) throws Exception {
        saveConfigToFile(config, destination);
        Logger.info("Configuration file exported to {}", destination);
    }

    private void saveConfigToFile(ServerConfiguration config, File file) throws IOException {
        ObjectWriter objectWriter;
        if (pretty) {
            objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        } else {
            objectWriter = objectMapper.writer();
        }
        objectWriter.writeValue(file, config);
    }

}
