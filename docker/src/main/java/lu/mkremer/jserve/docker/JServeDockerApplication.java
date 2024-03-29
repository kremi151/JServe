package lu.mkremer.jserve.docker;

import lu.mkremer.jserve.JServe;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.mappers.IndexPathMapper;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class JServeDockerApplication {

	public static void main(String[] args) throws IOException {
		Path rootPath = Paths.get("/", "opt", "jserve");

		try (InputStream inputStream = Files.newInputStream(rootPath.resolve("tinylog.properties"), StandardOpenOption.READ)) {
			Properties properties = new Properties();
			properties.load(inputStream);

			for (String key : properties.stringPropertyNames()) {
				System.setProperty("tinylog." + key, properties.getProperty(key));
			}
		}

		Path filesPath = rootPath.resolve("files");

		if (!Files.isDirectory(filesPath)) {
			throw new IllegalStateException("No readable volume mounted at " + filesPath);
		}

		ServerConfiguration config = new ServerConfiguration();
		config.getPathMappers().add(new IndexPathMapper("index.html"));
		config.setServePath(filesPath.toString());
		config.setMimeSource(rootPath.resolve("types.csv").toString());
		configure(config);

		new JServe(config).start();
	}

	private static void configure(ServerConfiguration config) {
		String value = System.getenv("JSERVE_PORT");
		if (value != null && !value.isEmpty()) {
			config.setPort(Integer.parseInt(value));
		}
		value = System.getenv("JSERVE_MAX_THREADS");
		if (value != null && !value.isEmpty()) {
			config.setMaxThreads(Integer.parseInt(value));
		}
	}

}
