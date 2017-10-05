package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 28 Sep 2017.
 */
public class RecommendationParameters extends CognitiveParameters {
    private String userId;
    private Integer numberOfResults;

    private String itemsIds;
    private Integer minimalScore;
    private Boolean includeMetadata;
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

    public Boolean isIncludeMetadata() {
        return includeMetadata;
    }

    public void setIncludeMetadata(Boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public Integer getMinimalScore() {
        return minimalScore;
    }

    public void setMinimalScore(Integer minimalScore) {
        this.minimalScore = minimalScore;
    }

    public Boolean getIncludeMetadata() {
        return includeMetadata;
    }

    public static Builder builder() {
        return new Builder(new RecommendationParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private static final String USER_ID = "userId";
        private static final String NUMBER_OF_RESULTS = "numberOfResults";
        private static final String BUILD_ID = "buildId";
        private static final String INCLUDE_METADATA = "includeMetadata";
        private static final String ITEMS_IDS = "itemsIds";
        private static final String MINIMAL_SCORE = "minimalScore";
        private final RecommendationParameters parameters;

        private Builder(RecommendationParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setUserId(Optional.ofNullable(request.queryParams(USER_ID)).orElseThrow(() -> throwException("User id is not provided")));
            parameters.setNumberOfResults(Integer.valueOf(Optional.ofNullable(request.queryParams(NUMBER_OF_RESULTS)).orElseThrow(() -> throwException("Number of results is not provided"))));

            Optional<String> buildId = Optional.ofNullable(request.queryParams(BUILD_ID));
            buildId.ifPresent(b -> parameters.setBuildId(Integer.valueOf(b)));

            Optional<String> includeMetadata = Optional.ofNullable(request.queryParams(INCLUDE_METADATA));
            includeMetadata.ifPresent(i -> parameters.setIncludeMetadata(Boolean.valueOf(i)));

            parameters.setItemsIds(request.queryParams(ITEMS_IDS));
            parameters.setMinimalScore(Integer.valueOf(Optional.ofNullable(request.queryParams(MINIMAL_SCORE)).orElseThrow(() -> throwException("Minimal score is not provided"))));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter(USER_ID, parameters.getUserId());
            builder.setParameter(NUMBER_OF_RESULTS, parameters.getNumberOfResults().toString());
            if (parameters.isIncludeMetadata() != null) {
                builder.setParameter(INCLUDE_METADATA, parameters.isIncludeMetadata().toString());
            }
            if (parameters.getItemsIds() != null) {
                builder.setParameter(ITEMS_IDS, parameters.getItemsIds());
            }
            if (parameters.getBuildId() != null) {
                builder.setParameter(BUILD_ID, parameters.getBuildId().toString());
            }
            if (parameters.getMinimalScore() != null) {
                builder.setParameter(MINIMAL_SCORE, parameters.getMinimalScore().toString());
            }
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpGet(uri));
        }
    }
}
