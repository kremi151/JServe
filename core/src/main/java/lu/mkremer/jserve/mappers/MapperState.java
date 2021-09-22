package lu.mkremer.jserve.mappers;

public enum MapperState {
	
	/**
	 * The mapper does not apply to the given path
	 */
	NOT_APPLICABLE(false, false),
	
	/**
	 * The mapper applies to the given path and the mapped path can be further
	 * transformed by a different mapper in the hierarchy
	 */
	ACCEPT_FORWARD(true, false),
	
	/**
	 * The mapper applies to the given path but the mapped path cannot be further
	 * transformed by a different mapper in the hierarchy
	 */
	ACCEPT_FINISH(true, true);
	
	private final boolean applicable;
	private final boolean terminal;
	
	MapperState(boolean applicable, boolean terminal) {
		this.applicable = applicable;
		this.terminal = terminal;
	}

	public boolean isApplicable() {
		return applicable;
	}

	public boolean isTerminal() {
		return terminal;
	}
	
}
