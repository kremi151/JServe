package lu.mkremer.jserve.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.mkremer.jserve.JServe;
import lu.mkremer.jserve.cli.JServeCLIApplication;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.CSVReader;
import lu.mkremer.jserve.util.MimeContext;
import picocli.CommandLine.Command;

import java.io.FileReader;
import java.io.InputStreamReader;

@Command(name = "jserve")
public class ServeCommand extends AbstractConfigCommand {

    public ServeCommand(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void handleConfig(ServerConfiguration config) throws Exception {
        CSVReader csvReader;
        if (config.getMimeSource() == null || config.getMimeSource().equals(ServerConfiguration.DEFAULT_MIME_FILE)) {
            ClassLoader classLoader = JServeCLIApplication.class.getClassLoader();
            csvReader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("mime/types.csv")));
        } else {
            csvReader = new CSVReader(new FileReader(config.getMimeSource()));
        }

        try {
            MimeContext.getInstance().loadMimeTypes(csvReader);
        } finally {
            csvReader.close();
        }

        new JServe(config).start();
    }

}
