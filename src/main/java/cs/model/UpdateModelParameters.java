package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public class UpdateModelParameters extends ModelParameters {
    private Integer activeBuildId;
    private String description;

    public Integer getActiveBuildId() {
        return activeBuildId;
    }

    public void setActiveBuildId(Integer activeBuildId) {
        this.activeBuildId = activeBuildId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder(new UpdateModelParameters());
    }

    public static final class Builder extends ModelBuilder implements IBuilder {
        private final UpdateModelParameters parameters;

        private Builder(UpdateModelParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setActiveBuildId(toInt(getOptional(request, "activeBuildId").orElseThrow(() -> throwException("The build is of model is not set"))));
            parameters.setDescription(getOptional(request, "description").orElse(""));
            return this;
        }

        public URIBuilder buildURI() throws URISyntaxException {
            return buildURIBuilder();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPatch httpPatch = buildHeader(new HttpPatch(uri));
            // Request body.
            StringEntity reqEntity = new StringEntity(toJson(parameters));
            httpPatch.setEntity(reqEntity);
            return httpPatch;
        }
    }
}
