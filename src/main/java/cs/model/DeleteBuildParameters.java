package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 11 Oct 2017.
 */
public class DeleteBuildParameters extends CognitiveParameters {
    public static Builder builder() {
        return new Builder(new DeleteBuildParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final DeleteBuildParameters parameters;

        private Builder(DeleteBuildParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            return super.buildURIBuilder().build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpDelete(uri));
        }

        public DeleteBuildParameters build() {
            return parameters;
        }
    }
}
