package lu.mkremer.jserve;

import lu.mkremer.jserve.command.ExportCommand;
import lu.mkremer.jserve.command.ServeCommand;

import picocli.CommandLine;

public class JServeApplication {

	public static void main(String[] args) {
		CommandLine cli = new CommandLine(new ServeCommand());
		cli.addSubcommand(new ExportCommand());

		int exitCode = cli.execute(args);
		System.exit(exitCode);
	}

}
