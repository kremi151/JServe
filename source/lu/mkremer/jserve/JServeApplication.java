package lu.mkremer.jserve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.mappers.MapperState;
import lu.mkremer.jserve.mappers.PathMapper;
import lu.mkremer.jserve.threading.SocketListener;

public class JServeApplication {
	
	private static final int MAX_THREADS = 10;
	private static final int PORT = 1806;
	
	private final ExecutorService executorService;
	private final File servePath;
	
	private List<PathMapper> pathMappers = new LinkedList<>();
	
	private JServeApplication(File servePath) {
		this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
		this.servePath = servePath;
		
		pathMappers.add(new IndexPathMapper("index.html"));
	}
	
	private void start() {
		executorService.execute(new SocketListener(this));
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	public int getPort() {
		return PORT;
	}
	
	public File getServePath() {
		return servePath;
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

	public static void main(String[] args) {
		File servePath = null;
		
		for (int i = 0 ; i < args.length ; i++) {
			String arg = args[i];
			if (arg.equals("-d")) {
				String path = args[++i];
				servePath = new File(path);
			}
		}
		
		if (servePath == null) {
			servePath = new File(System.getProperty("user.dir"));
		}
		
		new JServeApplication(servePath).start();
	}

}
