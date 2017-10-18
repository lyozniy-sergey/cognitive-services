package cs.model;

import cs.build.IBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * @author lyozniy.sergey on 11 Oct 2017.
 */
public class DeleteBuildParameters extends GetBuildParameters {
    public static Builder builder() {
        return new Builder(new DeleteBuildParameters());
    }

    public static final class Builder extends BuildBuilder implements IBuilder {
        private final DeleteBuildParameters parameters;

        private Builder(DeleteBuildParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        @Override
        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            return buildHeader(new HttpDelete(uri));
        }

        @Override
        public DeleteBuildParameters build() {
            return parameters;
        }
    }
}
