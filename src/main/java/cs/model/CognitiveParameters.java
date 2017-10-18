package cs.model;

import com.google.gson.Gson;
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
    private transient Object source;

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
        return "subscriptionKey='" + subscriptionKey + '\'' + ", source=" + source;
    }

    public static class CognitiveBuilder {
        private final CognitiveParameters parameters;

        public CognitiveBuilder(CognitiveParameters parameters) {
            this.parameters = parameters;
        }

        public CognitiveBuilder init(Request request) {
            parameters.setSubscriptionKey(getOptional(request, "subscriptionKey").orElseThrow(() -> throwException("Subscription key is not provided")));
            parameters.setUriBase(parameters.setupUriBase());
            parameters.setSource(request.queryParams("source"));
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
    }

    public static RuntimeException throwException(String message) {
        return new RuntimeException(message);
    }
}
