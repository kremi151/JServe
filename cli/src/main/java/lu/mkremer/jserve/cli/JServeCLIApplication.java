package lu.mkremer.jserve.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lu.mkremer.jserve.cli.command.ExportCommand;
import lu.mkremer.jserve.cli.command.ServeCommand;
import picocli.CommandLine;

public class JServeCLIApplication {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper()
				.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

		CommandLine cli = new CommandLine(new ServeCommand(objectMapper));
		cli.addSubcommand(new ExportCommand(objectMapper));

		int exitCode = cli.execute(args);
		if (exitCode != 0) {
			System.exit(exitCode);
		}
	}

}
