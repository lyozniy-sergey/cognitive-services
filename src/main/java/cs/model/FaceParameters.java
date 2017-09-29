package cs.model;

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
public class FaceParameters extends CognitiveParameters {
    private static final String body = "{\"url\":\"%s\"}";
    private boolean returnFaceId;
    private boolean returnFaceLandmarks;
    private String returnFaceAttributes;

    public boolean isReturnFaceId() {
        return returnFaceId;
    }

    public void setReturnFaceId(boolean returnFaceId) {
        this.returnFaceId = returnFaceId;
    }

    public boolean isReturnFaceLandmarks() {
        return returnFaceLandmarks;
    }

    public void setReturnFaceLandmarks(boolean returnFaceLandmarks) {
        this.returnFaceLandmarks = returnFaceLandmarks;
    }

    public String getReturnFaceAttributes() {
        return returnFaceAttributes;
    }

    public void setReturnFaceAttributes(String returnFaceAttributes) {
        this.returnFaceAttributes = returnFaceAttributes;
    }

    public static Builder builder() {
        return new Builder(new FaceParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final FaceParameters parameters;

        private Builder(FaceParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setReturnFaceId(Boolean.valueOf(Optional.ofNullable(request.queryParams("returnFaceId")).orElse("true")));
            parameters.setReturnFaceLandmarks(Boolean.valueOf(Optional.ofNullable(request.queryParams("returnFaceLandmarks")).orElse("false")));
            parameters.setReturnFaceAttributes(Optional.ofNullable(request.queryParams("returnFaceAttributes")).orElse(""));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter("returnFaceId", String.valueOf(parameters.isReturnFaceId()));
            builder.setParameter("returnFaceLandmarks", String.valueOf(parameters.isReturnFaceLandmarks()));
            builder.setParameter("returnFaceAttributes", parameters.getReturnFaceAttributes());
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            // Request body.
            StringEntity reqEntity = new StringEntity(String.format(body, parameters.getSource()));
            httpPost.setEntity(reqEntity);
            return httpPost;
        }
    }
}
