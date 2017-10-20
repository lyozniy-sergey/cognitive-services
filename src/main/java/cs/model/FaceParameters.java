package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public class FaceParameters extends CognitiveParameters {
    private static Logger logger = LoggerFactory.getLogger(FaceParameters.class);
    private static final String body = "{\"url\":\"%s\"}";
    public static final String URI_BASE = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    private boolean returnFaceId;
    private boolean returnFaceLandmarks;
    private String returnFaceAttributes;
    private Object source;

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

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

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "FaceParameters{" +
                "returnFaceId=" + returnFaceId +
                ", returnFaceLandmarks=" + returnFaceLandmarks +
                ", returnFaceAttributes='" + returnFaceAttributes + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder(new FaceParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        public static final String RETURN_FACE_ID = "returnFaceId";
        public static final String RETURN_FACE_LANDMARKS = "returnFaceLandmarks";
        public static final String RETURN_FACE_ATTRIBUTES = "returnFaceAttributes";
        public static final String SOURCE = "source";
        private final FaceParameters parameters;

        private Builder(FaceParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public Builder init(Request request) {
            super.init(request);
            parameters.setReturnFaceId(toBoolean(getOptional(request, RETURN_FACE_ID).orElse("true")));
            parameters.setReturnFaceLandmarks(toBoolean(getOptional(request, RETURN_FACE_LANDMARKS).orElse("false")));
            parameters.setReturnFaceAttributes(request.queryParams(RETURN_FACE_ATTRIBUTES));
            parameters.setSource(getOptional(request, SOURCE).orElseThrow(() -> throwException("Source is not provided")));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter(RETURN_FACE_ID, String.valueOf(parameters.isReturnFaceId()));
            builder.setParameter(RETURN_FACE_LANDMARKS, String.valueOf(parameters.isReturnFaceLandmarks()));
            if (parameters.getReturnFaceAttributes() != null) {
                builder.setParameter(RETURN_FACE_ATTRIBUTES, parameters.getReturnFaceAttributes());
            }
            builder.setParameter(SOURCE, parameters.getSource().toString());
            return builder;
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = buildHeader(new HttpPost(uri));
            // Request body.
            String body = String.format(FaceParameters.body, parameters.getSource());
            StringEntity reqEntity = new StringEntity(body);
            httpPost.setEntity(reqEntity);
            logger.debug("body: {}", body);
            return httpPost;
        }

        public Builder setReturnFaceId(boolean returnFaceId) {
            parameters.setReturnFaceId(returnFaceId);
            return this;
        }

        public Builder setReturnFaceLandmarks(boolean returnFaceLandmarks) {
            parameters.setReturnFaceLandmarks(returnFaceLandmarks);
            return this;
        }

        public Builder setReturnFaceAttributes(String returnFaceAttributes) {
            parameters.setReturnFaceAttributes(returnFaceAttributes);
            return this;
        }

        public Builder setSource(Object source) {
            parameters.setSource(source);
            return this;
        }
    }
}
