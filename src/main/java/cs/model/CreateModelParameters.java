package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public class CreateModelParameters extends CognitiveParameters {
    private String modelName;
    private String description;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder(new CreateModelParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final CreateModelParameters parameters;

        private Builder(CreateModelParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setModelName(Optional.ofNullable(request.queryParams("modelName")).orElseThrow(() -> throwException("The name of model is not set")));
            parameters.setDescription(Optional.ofNullable(request.queryParams("description")).orElse(""));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = buildHeader(new HttpPost(uri));
            // Request body.
            StringEntity reqEntity = new StringEntity(toJson(parameters));
            httpPost.setEntity(reqEntity);
            return httpPost;
        }
    }
}
