package cs.model;

import com.google.gson.Gson;
import cs.build.IBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public abstract class CognitiveParameters {
    private final static Logger logger = LoggerFactory.getLogger(CognitiveParameters.class);

    private transient String subscriptionKey;
    private transient String uriBase;

    protected abstract String setupUriBase();

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

    protected static Boolean toBoolean(String p) {
        return Boolean.valueOf(p);
    }

    protected static Integer toInt(String p) {
        return Integer.valueOf(p);
    }

    protected static Double toDouble(String p) {
        return Double.valueOf(p);
    }

    protected static String toJson(CognitiveParameters parameters) {
        return new Gson().toJson(parameters);
    }

    protected static Optional<String> getOptional(Request request, String field) {
        return Optional.ofNullable(request.queryParams(field));
    }

    @Override
    public String toString() {
        return "subscriptionKey='" + subscriptionKey + '\'';
    }

    public static abstract class CognitiveBuilder implements IBuilder {
        private final CognitiveParameters parameters;

        public CognitiveBuilder(CognitiveParameters parameters) {
            this.parameters = parameters;
        }

        public CognitiveBuilder init(Request request) {
            parameters.setSubscriptionKey(getOptional(request, "subscriptionKey").orElseThrow(() -> throwException("Subscription key is not provided")));
            parameters.setUriBase(getOptional(request, "uriBase").orElse(parameters.setupUriBase()));
            return this;
        }

        public URIBuilder buildURIBuilder() throws URISyntaxException {
            return new URIBuilder(parameters.getUriBase());
        }

        public <R extends HttpUriRequest> R buildHeader(R request) {
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            logger.debug("URI: {}", request.getURI().toString());
            logger.debug("Parameters: {}", parameters.toString());
            return request;
        }

        public CognitiveBuilder setSubscriptionKey(String subscriptionKey) {
            parameters.setSubscriptionKey(subscriptionKey);
            return this;
        }

        public CognitiveBuilder setUriBase(String uriBase) {
            parameters.setUriBase(uriBase);
            return this;
        }
    }

    public static RuntimeException throwException(String message) {
        return new RuntimeException(message);
    }
}
