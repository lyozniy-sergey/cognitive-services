package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static cs.web.route.MultipartRoute.FILE_CONTENT;

/**
 * @author lyozniy.sergey on 02 Oct 2017.
 */
public class FileUploadParameters extends CognitiveParameters {
    private final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s/%s";
    private final String displayHeader;
    private final String uriParam;
    private String displayName;

    public FileUploadParameters(String displayHeader, String uriParam) {
        this.displayHeader = displayHeader;
        this.uriParam = uriParam;
    }

    @Override
    protected String setupUriBase(){
        return URI_BASE;
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

    public String getUriParam() {
        return uriParam;
    }

    public static Builder builder(String displayHeader, String uriParam) {
        return new Builder(new FileUploadParameters(displayHeader, uriParam));
    }

    public static class Builder extends CognitiveBuilder implements IBuilder {
        private final FileUploadParameters parameters;

        private Builder(FileUploadParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public Builder init(Request request) {
            super.init(request);
            parameters.setDisplayName(request.queryParams(parameters.getDisplayHeader()));
            parameters.setSource(request.attribute(FILE_CONTENT));
            String modelId = getOptional(request, "modelId").orElseThrow(() -> throwException("Model id is not provided"));
            parameters.setUriBase(String.format(parameters.getUriBase(), modelId, parameters.getUriParam()));
            return this;
        }

        @Override
        public URIBuilder buildURI() throws URISyntaxException {
            URIBuilder builder = super.buildURIBuilder();
            builder.setParameter(parameters.getDisplayHeader(), parameters.getDisplayName());
            return builder;
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = buildHeader(new HttpPost(uri));
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
