package cs.model;

import cs.build.IBuilder;
import spark.Request;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public abstract class ModelParameters extends CognitiveParameters {
    private final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s";

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

    public static abstract class ModelBuilder extends CognitiveBuilder implements IBuilder {
        private final ModelParameters parameters;

        protected ModelBuilder(ModelParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public ModelBuilder init(Request request) {
            super.init(request);

            if (parameters.getUriBase() != null && parameters.getUriBase().contains("%s")) {
                String modelId = getOptional(request, "modelId").orElseThrow(() -> throwException("Model id is not provided"));
                transformUriBase(parameters.getUriBase(), modelId);
            }
            return this;
        }
        public ModelBuilder transformUriBase(String modelId) {
            return transformUriBase(parameters.getUriBase(), modelId);
        }

        public ModelBuilder transformUriBase(String uri, String modelId) {
            parameters.setUriBase(String.format(uri, modelId));
            return this;
        }
    }
}
