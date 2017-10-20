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
public class GetUserRecommendationParameters extends RecommendationParameters {
    public final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s/recommend/user";
    private String userId;

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GetUserRecommendationParameters{" +
                super.toString() +
                ", userId='" + userId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder(new GetUserRecommendationParameters());
    }

    public static final class Builder extends RecommendationBuilder implements IBuilder {
        private static final String ITEMS_IDS = "itemsIds";
        private static final String USER_ID = "userId";
        private final GetUserRecommendationParameters parameters;

        private Builder(GetUserRecommendationParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public Builder init(Request request) {
            super.init(request);
            parameters.setUserId(getOptional(request, USER_ID).orElseThrow(() -> throwException("User id is not provided")));
            parameters.setItemsIds(request.queryParams(ITEMS_IDS));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURI();
            builder.setParameter(USER_ID, parameters.getUserId());
            if (parameters.getItemsIds() != null) {
                builder.setParameter(ITEMS_IDS, parameters.getItemsIds());
            }
            return builder;
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpGet(uri));
        }

        public Builder setUserId(String userId) {
            parameters.setUserId(userId);
            return this;
        }
    }
}
