package cs.model;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static cs.web.CognitiveController.MultipartRoute.FILE_CONTENT;

/**
 * @author lyozniy.sergey on 02 Oct 2017.
 */
public class CatalogParameters extends CognitiveParameters {
    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public static Builder builder() {
        return new Builder(new CatalogParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final CatalogParameters parameters;

        private Builder(CatalogParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setCatalogName(request.queryParams("catalogDisplayName"));
            parameters.setSource(request.attribute(FILE_CONTENT));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter("catalogDisplayName", parameters.getCatalogName());
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            StringBuilder sb = new StringBuilder();
            if (parameters.getSource() instanceof List) {
                ((List) parameters.getSource()).forEach(c -> {
                    sb.append(c);
                    sb.append(System.getProperty("line.separator"));
                });
            }
            StringEntity reqEntity = new StringEntity(sb.toString());
            httpPost.setEntity(reqEntity);
            return httpPost;
        }
    }
}
