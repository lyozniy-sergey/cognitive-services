package cs.model;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public abstract class CognitiveParameters {
    private transient String subscriptionKey;
    private transient String uriBase;
    private transient Object source;
    private transient String modelId;

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getUriBase() {
        return uriBase;
    }

    public void setUriBase(String uriBase) {
        this.uriBase = uriBase;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    protected static Boolean toBoolean(String p) {
        return Boolean.valueOf(p);
    }

    protected static Integer toInt(String p) {
        return Integer.valueOf(p);
    }

    protected static String toJson(CognitiveParameters parameters) {
        return new Gson().toJson(parameters);
    }

    protected static Optional<String> getOptional(Request request, String field) {
        return Optional.ofNullable(request.queryParams(field));
    }

    public static class CognitiveBuilder {
        private final CognitiveParameters parameters;

        public CognitiveBuilder(CognitiveParameters parameters) {
            this.parameters = parameters;
        }

        public CognitiveBuilder init(Request request) {
            parameters.setSubscriptionKey(Optional.ofNullable(request.queryParams("subscriptionKey")).orElseThrow(() -> throwException("Subscription key is not provided")));
            parameters.setUriBase(Optional.ofNullable(request.queryParams("uriBase")).orElseThrow(() -> throwException("URI base is not provided")));
            parameters.setSource(request.queryParams("source"));
            parameters.setModelId(request.queryParams("modelId"));
            return this;
        }

        public URIBuilder buildURIBuilder() throws URISyntaxException {
            URIBuilder uriBuilder = new URIBuilder(parameters.getUriBase());
            if (parameters.getModelId() != null) {
                uriBuilder.setParameter("modelId", parameters.getModelId());
            }
            return uriBuilder;
        }

        public <R extends HttpUriRequest> R buildHeader(R request) {
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            return request;
        }
    }

    public static RuntimeException throwException(String message) {
        return new RuntimeException(message);
    }
}
