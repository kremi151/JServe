package lu.mkremer.jserve.errorhandling;

public class UnknownErrorHandler extends DefaultErrorHandler {

	@Override
	public boolean canHandle(int errorCode, String path) {
		return false; 
	}
	
}
