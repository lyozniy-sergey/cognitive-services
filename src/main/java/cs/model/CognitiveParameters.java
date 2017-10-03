package cs.model;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public abstract class CognitiveParameters {
    private String subscriptionKey;
    private String uriBase;
    private Object source;
    private String modelId;

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

    public static class CognitiveBuilder {
        private final CognitiveParameters parameters;

        public CognitiveBuilder(CognitiveParameters parameters) {
            this.parameters = parameters;
        }

        public CognitiveBuilder init(Request request) {
            parameters.setSubscriptionKey(Optional.ofNullable(request.queryParams("subscriptionKey")).orElseThrow(() -> throwException("Subscription key is not provided")));
            parameters.setUriBase(Optional.ofNullable(request.queryParams("uriBase")).orElseThrow(() -> throwException("URI base is not provided")));
            parameters.setSource(request.queryParams("source"));
            String modelId = request.queryParams("modelId");
            if (modelId != null) {
                parameters.setModelId(modelId);
            }
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
