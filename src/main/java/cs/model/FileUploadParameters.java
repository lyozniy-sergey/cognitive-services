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
public class FileUploadParameters extends CognitiveParameters {
    private String displayName;
    private final String displayHeader;

    public FileUploadParameters(String displayHeader) {
        this.displayHeader = displayHeader;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayHeader() {
        return displayHeader;
    }

    public static Builder builder(String displayHeader) {
        return new Builder(new FileUploadParameters(displayHeader));
    }

    public static class Builder extends CognitiveBuilder implements IBuilder {
        private final FileUploadParameters parameters;

        private Builder(FileUploadParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setDisplayName(request.queryParams(parameters.getDisplayHeader()));
            parameters.setSource(request.attribute(FILE_CONTENT));
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter(parameters.getDisplayHeader(), parameters.getDisplayName());
            return builder.build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Ocp-Apim-Subscription-Key", parameters.getSubscriptionKey());
            StringBuilder sb = new StringBuilder();
            if (parameters.getSource() instanceof List) {
                ((List<?>) parameters.getSource()).forEach(c -> {
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
