package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 12 Oct 2017.
 */
public class GetBuildParameters extends ModelParameters {
    public final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s/builds/%s";

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

    public static BuildBuilder builder() {
        return new BuildBuilder(new GetBuildParameters());
    }

    public static class BuildBuilder extends CognitiveBuilder implements IBuilder {
        private final GetBuildParameters parameters;

        protected BuildBuilder(GetBuildParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public BuildBuilder init(Request request) {
            super.init(request);
            if (parameters.getUriBase() != null && parameters.getUriBase().contains("%s")) {
                String modelId = getOptional(request, "modelId").orElseThrow(() -> throwException("Model id is not provided"));
                Integer buildId = toInt(getOptional(request, "buildId").orElseThrow(() -> throwException("Build id is not set")));
                transformUriBase(modelId, buildId);
            }
            return this;
        }

        public BuildBuilder transformUriBase(String modelId, Integer buildId) {
            return transformUriBase(parameters.getUriBase(), modelId, buildId);
        }

        public BuildBuilder transformUriBase(String uri, String modelId, Integer buildId) {
            parameters.setUriBase(String.format(uri, modelId, buildId));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            return super.buildURIBuilder();
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpGet(uri));
        }

        public GetBuildParameters build() {
            return parameters;
        }
    }
}
