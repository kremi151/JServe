package lu.mkremer.jserve.conf;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lu.mkremer.jserve.mappers.MapperState;
import lu.mkremer.jserve.mappers.PathMapper;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
public class ServerConfiguration {
	
	public static final String DEFAULT_SERVE_PATH = "/";
	public static final int DEFAULT_PORT = 1806;
	public static final int DEFAULT_MAX_THREADS = 10;

	private String servePath = DEFAULT_SERVE_PATH;
	private int port = DEFAULT_PORT;
	private List<PathMapper> pathMappers = new ArrayList<>();
	private int maxThreads = DEFAULT_MAX_THREADS;
	
	public ServerConfiguration() {
		
	}

	public List<PathMapper> getPathMappers() {
		return pathMappers;
	}

	public void setPathMappers(List<PathMapper> pathMappers) {
		this.pathMappers = pathMappers;
	}

	public String getServePath() {
		return servePath;
	}

	public void setServePath(String servePath) {
		this.servePath = servePath;
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

}
