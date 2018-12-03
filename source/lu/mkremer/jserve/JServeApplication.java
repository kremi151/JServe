package lu.mkremer.jserve;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.CSVReader;
import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.threading.SocketListener;
import lu.mkremer.jserve.util.MimeContext;

public class JServeApplication {
	
	private final ExecutorService executorService;
	
	private ServerConfiguration configuration;
	
	private JServeApplication(ServerConfiguration configuration) {
		this.executorService = Executors.newFixedThreadPool(configuration.getMaxThreads());
		this.configuration = configuration;
	}
	
	private void start() {
		executorService.execute(new SocketListener(this));
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	public ServerConfiguration getConfiguration() {
		return configuration;
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		boolean configFromFile = false;
		ServerConfiguration configuration = new ServerConfiguration();
		configuration.setServePath(System.getProperty("user.dir"));
		configuration.setPort(ServerConfiguration.DEFAULT_PORT);
		configuration.setMaxThreads(ServerConfiguration.DEFAULT_MAX_THREADS);
		configuration.getPathMappers().add(new IndexPathMapper("index.html"));
		
		for (int i = 0 ; i < args.length ; i++) {
			String arg = args[i];
			if (!configFromFile && arg.equals("-d")) {
				configuration.setServePath(args[++i]);
			} else if (!configFromFile && arg.equals("-p")) {
				configuration.setPort(Integer.parseInt(args[++i]));
			} else if (!configFromFile && arg.equals("-m")) {
				configuration.setMaxThreads(Integer.parseInt(args[++i]));
			} else if (arg.equals("-c")) {
				configuration = loadConfigFromFile(new File(args[++i]));
				configFromFile = true;
			} else if (arg.equals("--export-config")) {
				File destination = new File(args[++i]);
				saveConfigToFile(configuration, destination);
				System.out.println("Configuration file exported to " + destination);
				return;
			}
		}
		
		ClassLoader classLoader = JServeApplication.class.getClassLoader();
		MimeContext.getInstance().loadMimeTypes(new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("mime/types.csv"))));
		
		new JServeApplication(configuration).start();
	}
	
	private static ServerConfiguration loadConfigFromFile(File file) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(file, ServerConfiguration.class);
	}
	
	private static void saveConfigToFile(ServerConfiguration config, File file) throws JsonGenerationException, JsonMappingException, IOException {
		new ObjectMapper().writeValue(file, config);
	}

}
