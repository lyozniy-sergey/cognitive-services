package cs.model;

import com.google.gson.Gson;
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
public class ModelParameters extends CognitiveParameters {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder(new ModelParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final ModelParameters parameters;

        private Builder(ModelParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setName(Optional.ofNullable(request.queryParams("name")).orElseThrow(() -> throwException("name of model is not set")));
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
            StringEntity reqEntity = new StringEntity(new Gson().toJson(parameters));
            httpPost.setEntity(reqEntity);
            return httpPost;
        }
    }
}
