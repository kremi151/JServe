package lu.mkremer.jserve.docker;

import lu.mkremer.jserve.JServe;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.mappers.IndexPathMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JServeDockerApplication {

	public static void main(String[] args) {
		Path rootPath = Paths.get("/", "opt", "jserve");
		Path filesPath = rootPath.resolve("files");

		if (!Files.isDirectory(filesPath)) {
			throw new IllegalStateException("No readable volume mounted at " + filesPath);
		}

		String portStr = System.getenv("JSERVE_PORT");

		ServerConfiguration config = new ServerConfiguration();
		config.getPathMappers().add(new IndexPathMapper("index.html"));
		config.setServePath(filesPath.toString());
		if (portStr != null && !portStr.isEmpty()) {
			config.setPort(Integer.parseInt(portStr));
		}

		new JServe(config).start();
	}

}
