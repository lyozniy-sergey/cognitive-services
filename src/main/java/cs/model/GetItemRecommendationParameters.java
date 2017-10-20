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
public class GetItemRecommendationParameters extends RecommendationParameters {
    public final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s/recommend/item";
    private Double minimalScore;

    @Override
    protected String setupUriBase(){
        return URI_BASE;
    }

    public Double getMinimalScore() {
        return minimalScore;
    }

    public void setMinimalScore(Double minimalScore) {
        this.minimalScore = minimalScore;
    }


    public static Builder builder() {
        return new Builder(new GetItemRecommendationParameters());
    }

    @Override
    public String toString() {
        return "GetItemRecommendationParameters{" +
                super.toString() +
                ", minimalScore=" + minimalScore +
                '}';
    }

    public static final class Builder extends RecommendationBuilder implements IBuilder {
        private static final String MINIMAL_SCORE = "minimalScore";
        private static final String ITEM_IDS = "itemIds";
        private final GetItemRecommendationParameters parameters;

        private Builder(GetItemRecommendationParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public Builder init(Request request) {
            super.init(request);
            parameters.setMinimalScore(toDouble(getOptional(request, MINIMAL_SCORE).orElseThrow(() -> throwException("Minimal score is not provided"))));
            parameters.setItemsIds(request.queryParams(ITEM_IDS));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURI();
            if (parameters.getMinimalScore() != null) {
                builder.setParameter(MINIMAL_SCORE, parameters.getMinimalScore().toString());
            }
            if (parameters.getItemsIds() != null) {
                builder.setParameter(ITEM_IDS, parameters.getItemsIds());
            }
            return builder;
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpGet(uri));
        }

        public Builder setMinimalScore(Double minimalScore){
            parameters.setMinimalScore(minimalScore);
            return this;
        }
    }
}
