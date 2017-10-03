package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 28 Sep 2017.
 */
public class RecommendationParameters extends CognitiveParameters {
    private String userId;
    private Integer numberOfResults;

    private String itemsIds;
    private boolean includeMetadata;
    private Integer buildId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public String getItemsIds() {
        return itemsIds;
    }

    public void setItemsIds(String itemsIds) {
        this.itemsIds = itemsIds;
    }

    public boolean isIncludeMetadata() {
        return includeMetadata;
    }

    public void setIncludeMetadata(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public static Builder builder() {
        return new Builder(new RecommendationParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final RecommendationParameters parameters;

        private Builder(RecommendationParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setUserId(request.queryParams("userId"));
            String numberOfResults = request.queryParams("numberOfResults");
            if (numberOfResults != null) {
                parameters.setNumberOfResults(Integer.valueOf(numberOfResults));
            }
            String buildId = request.queryParams("buildId");
            if (buildId != null) {
                parameters.setBuildId(Integer.valueOf(buildId));
            }
            String includeMetadata = request.queryParams("includeMetadata");
            if (includeMetadata != null) {
                parameters.setIncludeMetadata(Boolean.valueOf(includeMetadata));
            }
            parameters.setItemsIds(request.queryParams("itemsIds"));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter("userId", parameters.getUserId());
            if (parameters.getNumberOfResults() != null) {
                builder.setParameter("numberOfResults", String.valueOf(parameters.getNumberOfResults()));
            }
            if (parameters.getItemsIds() != null) {
                builder.setParameter("itemsIds", parameters.getItemsIds());
            }
            if (parameters.getBuildId() != null) {
                builder.setParameter("buildId", String.valueOf(parameters.getBuildId()));
            }
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            return httpGet;
        }
    }
}
