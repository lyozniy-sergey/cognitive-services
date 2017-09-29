package cs.model;

import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public abstract class CognitiveParameters {
    private String subscriptionKey;
    private String uriBase;
    private String source;

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getUriBase() {
        return uriBase;
    }

    public void setUriBase(String uriBase) {
        this.uriBase = uriBase;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static class CognitiveBuilder {
        private final CognitiveParameters parameters;

        public CognitiveBuilder(CognitiveParameters parameters) {
            this.parameters = parameters;
        }

        public CognitiveBuilder init(Request request) {
            parameters.setSubscriptionKey(request.queryParams("subscriptionKey"));
            parameters.setUriBase(request.queryParams("uriBase"));
            parameters.setSource(request.queryParams("source"));
            return this;
        }

        public URIBuilder buildURIBuilder() throws URISyntaxException {
            return new URIBuilder(parameters.getUriBase());
        }
    }
}
