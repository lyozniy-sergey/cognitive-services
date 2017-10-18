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
public abstract class RecommendationParameters extends ModelParameters {
    private Integer numberOfResults;

    private String itemsIds;
    private Boolean includeMetadata;
    private Integer buildId;

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

    public Boolean getIncludeMetadata() {
        return includeMetadata;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", numberOfResults=" + numberOfResults +
                ", itemsIds='" + itemsIds + '\'' +
                ", includeMetadata=" + includeMetadata +
                ", buildId=" + buildId;
    }

    public abstract static class RecommendationBuilder extends ModelBuilder implements IBuilder {
        private static final String NUMBER_OF_RESULTS = "numberOfResults";
        private static final String BUILD_ID = "buildId";
        private static final String INCLUDE_METADATA = "includeMetadata";
        private final RecommendationParameters parameters;

        protected RecommendationBuilder(RecommendationParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public RecommendationBuilder init(Request request) {
            super.init(request);
            parameters.setNumberOfResults(toInt(getOptional(request, NUMBER_OF_RESULTS).orElseThrow(() -> throwException("Number of results is not provided"))));

            Optional<String> buildId = getOptional(request, BUILD_ID);
            buildId.ifPresent(b -> parameters.setBuildId(Integer.valueOf(b)));

            Optional<String> includeMetadata = getOptional(request, INCLUDE_METADATA);
            includeMetadata.ifPresent(i -> parameters.setIncludeMetadata(Boolean.valueOf(i)));

            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter(NUMBER_OF_RESULTS, parameters.getNumberOfResults().toString());
            if (parameters.isIncludeMetadata() != null) {
                builder.setParameter(INCLUDE_METADATA, parameters.isIncludeMetadata().toString());
            }
            if (parameters.getBuildId() != null) {
                builder.setParameter(BUILD_ID, parameters.getBuildId().toString());
            }
            return builder;
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpGet(uri));
        }
    }
}
