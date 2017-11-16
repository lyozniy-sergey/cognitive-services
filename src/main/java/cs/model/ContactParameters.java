package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public class ContactParameters extends CognitiveParameters {
    private static Logger logger = LoggerFactory.getLogger(ContactParameters.class);
    public static final String URI_BASE = "http://localhost:8080/contacts";

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

    @Override
    public String toString() {
        return "FaceParameters{" +
                "uri base=" + URI_BASE +
                '}';
    }

    public static Builder builder() {
        return new Builder(new ContactParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final ContactParameters parameters;

        private Builder(ContactParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public Builder init(Request request) {
//            super.init(request);
            parameters.setUriBase(getOptional(request, "uriBase").orElse(parameters.setupUriBase()));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            return super.buildURIBuilder();
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return new HttpGet(uri);
        }
    }
}
