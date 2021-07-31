package lu.mkremer.jserve.conf;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lu.mkremer.jserve.errorhandling.DefaultErrorHandler;
import lu.mkremer.jserve.errorhandling.ErrorHandler;
import lu.mkremer.jserve.mappers.MapperState;
import lu.mkremer.jserve.mappers.PathMapper;

@JsonInclude(Include.NON_NULL)
public class ServerConfiguration {
	
	public static final String DEFAULT_SERVE_PATH = "/";
	public static final int DEFAULT_PORT = 1806;
	public static final int DEFAULT_MAX_THREADS = 10;
	public static final String DEFAULT_MIME_FILE = "default";
	public static final String DEFAULT_LOGGING_LEVEL = "info";

	private Path servePath = Paths.get(DEFAULT_SERVE_PATH);
	private String mimeSource = DEFAULT_MIME_FILE;
	private int port = DEFAULT_PORT;
	private List<PathMapper> pathMappers = new ArrayList<>();
	private List<ErrorHandler> errorHandlers = new ArrayList<>();
	private int maxThreads = DEFAULT_MAX_THREADS;
	private String loggingLevel = DEFAULT_LOGGING_LEVEL;
	
	public ServerConfiguration() {
		
	}

	public List<PathMapper> getPathMappers() {
		return pathMappers;
	}

	public void setPathMappers(List<PathMapper> pathMappers) {
		this.pathMappers = pathMappers;
	}

	public List<ErrorHandler> getErrorHandlers() {
		return errorHandlers;
	}

	public void setErrorHandlers(List<ErrorHandler> errorHandlers) {
		this.errorHandlers = errorHandlers;
	}

	public String getServePath() {
		return servePath.toString();
	}

	public void setServePath(String servePath) {
		this.servePath = Paths.get(servePath);
	}
	
	public String getMimeSource() {
		return mimeSource;
	}

	public void setMimeSource(String mimeSource) {
		this.mimeSource = mimeSource;
	}

	@JsonIgnore
	public Path getServeNioPath() {
		return servePath;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public String getLoggingLevel() {
		return loggingLevel;
	}

	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	public String mapPath(String path) {
		for (PathMapper mapper : pathMappers) {
			MapperState state = mapper.applies(path);
			if (state.isApplicable()) {
				path = mapper.map(path);
				if (state.isTerminal()) {
					return path;
				}
			}
		}
		return path;
	}
	
	public ErrorHandler findErrorHandler(int code, String path) {
		for (ErrorHandler handler : errorHandlers) {
			if (handler.canHandle(code, path)) {
				return handler;
			}
		}
		return new DefaultErrorHandler();
	}

}
