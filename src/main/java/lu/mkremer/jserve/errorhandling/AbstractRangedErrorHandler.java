package lu.mkremer.jserve.errorhandling;

public abstract class AbstractRangedErrorHandler implements ErrorHandler {
	
	private int from;
	private int to;

	@Override
	public boolean canHandle(int errorCode, String path) {
		return from <= errorCode && errorCode <= to;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

}
