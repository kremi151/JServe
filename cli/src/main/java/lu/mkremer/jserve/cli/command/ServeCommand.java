package lu.mkremer.jserve.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.mkremer.jserve.JServe;
import lu.mkremer.jserve.conf.ServerConfiguration;
import picocli.CommandLine.Command;

@Command(name = "jserve")
public class ServeCommand extends AbstractConfigCommand {

    public ServeCommand(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void handleConfig(ServerConfiguration config) throws Exception {
        new JServe(config).start();
    }

}
