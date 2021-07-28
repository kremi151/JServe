package lu.mkremer.jserve.command;

import lu.mkremer.jserve.JServe;
import lu.mkremer.jserve.JServeApplication;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.CSVReader;
import lu.mkremer.jserve.util.MimeContext;
import picocli.CommandLine.Command;

import java.io.FileReader;
import java.io.InputStreamReader;

@Command(name = "jserve")
public class ServeCommand extends AbstractConfigCommand {

    @Override
    protected void handleConfig(ServerConfiguration config) throws Exception {
        CSVReader csvReader;
        if (config.getMimeSource() == null || config.getMimeSource().equals(ServerConfiguration.DEFAULT_MIME_FILE)) {
            ClassLoader classLoader = JServeApplication.class.getClassLoader();
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
