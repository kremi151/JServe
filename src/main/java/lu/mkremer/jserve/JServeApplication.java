package lu.mkremer.jserve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.conf.serializers.PathMapperDeserializer;
import lu.mkremer.jserve.conf.serializers.PathMapperSerializer;
import lu.mkremer.jserve.io.CSVReader;
import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.mappers.PathMapper;
import lu.mkremer.jserve.registry.JARPluginLoader;
import lu.mkremer.jserve.registry.PluginRegistry;
import lu.mkremer.jserve.threading.SocketListener;
import lu.mkremer.jserve.util.DefaultJServePlugin;
import lu.mkremer.jserve.util.MimeContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	public static void main(String[] args) throws IOException {
		boolean configFromFile = false;

		final File pluginsDir = new File("plugins");

		PathMapperFactory factory = new PathMapperFactory();

		SimpleModule module = new SimpleModule();
		module.addSerializer(PathMapper.class, new PathMapperSerializer(factory));
		module.addDeserializer(PathMapper.class, new PathMapperDeserializer(factory));

		PluginRegistry pluginRegistry = new PluginRegistry();
		pluginRegistry.registerPlugin(new DefaultJServePlugin());
		JARPluginLoader.collectPluginsAt(pluginsDir, pluginRegistry);
		pluginRegistry.onRegisterMappers(factory);

		ServerConfiguration configuration = new ServerConfiguration();
		configuration.setServePath(System.getProperty("user.dir"));
		configuration.setMimeSource(ServerConfiguration.DEFAULT_MIME_FILE);
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
			} else if (!configFromFile && arg.equals("-t")) {
				configuration.setMimeSource(args[++i]);
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
		
		CSVReader csvReader;
		if (configuration.getMimeSource() == null || configuration.getMimeSource().equals(ServerConfiguration.DEFAULT_MIME_FILE)) {
			ClassLoader classLoader = JServeApplication.class.getClassLoader();
			csvReader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("mime/types.csv")));
		} else {
			csvReader = new CSVReader(new FileReader(new File(configuration.getMimeSource())));
		}
		
		try {
			MimeContext.getInstance().loadMimeTypes(csvReader);
		} finally {
			csvReader.close();
		}
		
		new JServeApplication(configuration).start();
	}
	
	private static ServerConfiguration loadConfigFromFile(File file) throws IOException {
		return new ObjectMapper().readValue(file, ServerConfiguration.class);
	}
	
	private static void saveConfigToFile(ServerConfiguration config, File file) throws IOException {
		new ObjectMapper().writeValue(file, config);
	}

}
