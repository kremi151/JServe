package lu.mkremer.jserve.conf.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.mkremer.jserve.errorhandling.ErrorHandler;
import lu.mkremer.jserve.errorhandling.ErrorPageHandler;
import lu.mkremer.jserve.errorhandling.RedirectErrorHandler;

import java.io.IOException;

public class ErrorHandlerSerializer extends JsonSerializer<ErrorHandler> {

	@Override
	public void serialize(ErrorHandler value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		if (value instanceof ErrorPageHandler) {
			gen.writeStringField("type", "page");
			ErrorPageHandler handler = (ErrorPageHandler) value;
			gen.writeNumberField("from", handler.getFrom());
			gen.writeNumberField("to", handler.getTo());
			gen.writeStringField("path", handler.getPath());
		} else if (value instanceof RedirectErrorHandler) {
			gen.writeStringField("type", "redirect");
			RedirectErrorHandler handler = (RedirectErrorHandler) value;
			gen.writeNumberField("from", handler.getFrom());
			gen.writeNumberField("to", handler.getTo());
			gen.writeStringField("path", handler.getRedirect());
		} else {
			gen.writeStringField("type", "unknown");
		}
		gen.writeEndObject();
	}

}
