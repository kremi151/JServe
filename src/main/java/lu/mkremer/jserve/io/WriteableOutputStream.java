package lu.mkremer.jserve.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class WriteableOutputStream extends OutputStream{
	
	private final OutputStream outputStream;
	private final Charset charset;
	
	public WriteableOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
		this.charset = Charset.forName("UTF-8");
	}

	@Override
	public void write(int arg0) throws IOException {
		outputStream.write(arg0);
	}
	
	@Override
	public void write(byte buffer[], int offset, int length) throws IOException {
		outputStream.write(buffer, offset, length);
	}
	
	public void write(String string) throws IOException {
		outputStream.write(string.getBytes(charset));
	}
	
	@Override
	public void close() throws IOException {
		outputStream.close();
	}

}
