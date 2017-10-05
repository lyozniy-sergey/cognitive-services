package cs.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lyozniy.sergey on 04 Oct 2017.
 */
public class BuildParameters extends CognitiveParameters {
    private String description;
    private String buildType;
    private Map<String, Parameters> buildParameters;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
        setBuildParameter(buildType, BuildParametersHolder.getBy(buildType));
    }

    public Map<String, Parameters> getBuildParameters() {
        return Collections.unmodifiableMap(buildParameters);
    }

    public void setBuildParameter(String buildParameter, Parameters parameter) {
        if (buildParameters == null) {
            buildParameters = new HashMap<>();
        }
        this.buildParameters.put(buildParameter, parameter);
    }

    public void setBuildParameters(Map<String, Parameters> buildParameters) {
        this.buildParameters = buildParameters;
    }

    public static Builder builder() {
        return new Builder(new BuildParameters());
    }

    public static final class Builder extends CognitiveBuilder implements IBuilder {
        private final BuildParameters parameters;

        private Builder(BuildParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setBuildType(Optional.ofNullable(request.queryParams("buildType")).orElseThrow(() -> throwException("Build type is not set")));
            parameters.setDescription(Optional.ofNullable(request.queryParams("description")).orElse(""));
            parameters.getBuildParameters().get(parameters.getBuildType()).getBuilder().init(request);
            return this;
        }

        public URI buildURI() throws URISyntaxException {
            return super.buildURIBuilder().build();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = buildHeader(new HttpPost(uri));
            // Request body.
            StringEntity reqEntity = new StringEntity(new Gson().toJson(parameters));
            httpPost.setEntity(reqEntity);
            return httpPost;
        }

        public BuildParameters build() {
            return parameters;
        }
    }

    private static class BuildParametersHolder {
        private static final Map<String, Parameters> buildTypes = ImmutableMap.<String, Parameters>builder().
                put(RankParameters.RANK, new RankParameters()).
                put(RecommendationParameters.RECOMMENDATION, new RecommendationParameters()).
                put(FbtParameters.FBT, new FbtParameters()).
                put(SarParameters.SAR, new SarParameters()).
                build();

        public static Parameters getBy(String buildType) {
            return buildTypes.get(buildType);
        }
    }

    private interface Parameters {
        String getBuildType();

        IBuilder getBuilder();
    }

    private static class RecommendationParameters implements Parameters {
        public static final String RECOMMENDATION = "recommendation";
        private Integer numberOfModelIterations;
        private Integer numberOfModelDimensions;
        private Integer itemCutOffLowerBound;
        private Integer itemCutOffUpperBound;
        private Integer userCutOffLowerBound;
        private Integer userCutOffUpperBound;
        private Boolean enableModelingInsights;
        private String splitterStrategy;
        private RandomSplitterParameters randomSplitterParameters;
        private DateSplitterParameters dateSplitterParameters;
        private Integer popularItemBenchmarkWindow;
        private Boolean useFeaturesInModel;
        private String modelingFeatureList;
        private Boolean allowColdItemPlacement;
        private Boolean enableFeatureCorrelation;
        private String reasoningFeatureList;
        private Boolean enableU2I;

        @Override
        public String getBuildType() {
            return RECOMMENDATION;
        }

        private static Boolean toBoolean(String p) {
            return Boolean.valueOf(p);
        }

        private static Integer toInt(String p) {
            return Integer.valueOf(p);
        }

        private static Optional<String> getOptional(Request request, String field) {
            return Optional.ofNullable(request.queryParams(field));
        }

        public IBuilder getBuilder(){
            return builder();
        }

        public static Builder builder() {
            return new Builder(new RecommendationParameters());
        }

        public static final class Builder implements IBuilder {
            private final RecommendationParameters parameters;

            private Builder(RecommendationParameters parameters) {
                this.parameters = parameters;
            }

            public Builder init(Request request) {
                parameters.setSplitterStrategy(request.queryParams("splitterStrategy"));
                getOptional(request, "numberOfModelIterations").ifPresent(p -> parameters.setNumberOfModelIterations(toInt(p)));
                getOptional(request, "numberOfModelDimensions").ifPresent(p -> parameters.setNumberOfModelDimensions(toInt(p)));
                getOptional(request, "itemCutOffLowerBound").ifPresent(p -> parameters.setItemCutOffLowerBound(toInt(p)));
                getOptional(request, "itemCutOffUpperBound").ifPresent(p -> parameters.setItemCutOffUpperBound(toInt(p)));
                getOptional(request, "userCutOffLowerBound").ifPresent(p -> parameters.setUserCutOffLowerBound(toInt(p)));
                getOptional(request, "userCutOffUpperBound").ifPresent(p -> parameters.setUserCutOffUpperBound(toInt(p)));
                getOptional(request, "popularItemBenchmarkWindow").ifPresent(p -> parameters.setPopularItemBenchmarkWindow(toInt(p)));

                getOptional(request, "useFeaturesInModel").ifPresent(p -> parameters.setUseFeaturesInModel(toBoolean(p)));
                getOptional(request, "enableModelingInsights").ifPresent(p -> parameters.setEnableModelingInsights(toBoolean(p)));
                getOptional(request, "allowColdItemPlacement").ifPresent(p -> parameters.setAllowColdItemPlacement(toBoolean(p)));
                getOptional(request, "enableFeatureCorrelation").ifPresent(p -> parameters.setEnableFeatureCorrelation(toBoolean(p)));
                getOptional(request, "enableU2I").ifPresent(p -> parameters.setEnableU2I(toBoolean(p)));

                parameters.setReasoningFeatureList(request.queryParams("reasoningFeatureList"));
                parameters.setSplitterStrategy(request.queryParams("splitterStrategy"));
                parameters.setModelingFeatureList(request.queryParams("modelingFeatureList"));

                parameters.setRandomSplitterParameters(RandomSplitterParameters.builder().init(request).build());
                parameters.setDateSplitterParameters(DateSplitterParameters.builder().init(request).build());
                return this;
            }

            @Override
            public URI buildURI() throws URISyntaxException {
                return null;
            }

            @Override
            public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                return null;
            }

            public RecommendationParameters build() {
                return parameters;
            }
        }

        public Integer getNumberOfModelIterations() {
            return numberOfModelIterations;
        }

        public void setNumberOfModelIterations(Integer numberOfModelIterations) {
            this.numberOfModelIterations = numberOfModelIterations;
        }

        public Integer getNumberOfModelDimensions() {
            return numberOfModelDimensions;
        }

        public void setNumberOfModelDimensions(Integer numberOfModelDimensions) {
            this.numberOfModelDimensions = numberOfModelDimensions;
        }

        public Integer getItemCutOffLowerBound() {
            return itemCutOffLowerBound;
        }

        public void setItemCutOffLowerBound(Integer itemCutOffLowerBound) {
            this.itemCutOffLowerBound = itemCutOffLowerBound;
        }

        public Integer getItemCutOffUpperBound() {
            return itemCutOffUpperBound;
        }

        public void setItemCutOffUpperBound(Integer itemCutOffUpperBound) {
            this.itemCutOffUpperBound = itemCutOffUpperBound;
        }

        public Integer getUserCutOffLowerBound() {
            return userCutOffLowerBound;
        }

        public void setUserCutOffLowerBound(Integer userCutOffLowerBound) {
            this.userCutOffLowerBound = userCutOffLowerBound;
        }

        public Integer getUserCutOffUpperBound() {
            return userCutOffUpperBound;
        }

        public void setUserCutOffUpperBound(Integer userCutOffUpperBound) {
            this.userCutOffUpperBound = userCutOffUpperBound;
        }

        public Boolean getEnableModelingInsights() {
            return enableModelingInsights;
        }

        public void setEnableModelingInsights(Boolean enableModelingInsights) {
            this.enableModelingInsights = enableModelingInsights;
        }

        public String getSplitterStrategy() {
            return splitterStrategy;
        }

        public void setSplitterStrategy(String splitterStrategy) {
            this.splitterStrategy = splitterStrategy;
        }

        public Integer getPopularItemBenchmarkWindow() {
            return popularItemBenchmarkWindow;
        }

        public void setPopularItemBenchmarkWindow(Integer popularItemBenchmarkWindow) {
            this.popularItemBenchmarkWindow = popularItemBenchmarkWindow;
        }

        public Boolean getUseFeaturesInModel() {
            return useFeaturesInModel;
        }

        public void setUseFeaturesInModel(Boolean useFeaturesInModel) {
            this.useFeaturesInModel = useFeaturesInModel;
        }

        public String getModelingFeatureList() {
            return modelingFeatureList;
        }

        public void setModelingFeatureList(String modelingFeatureList) {
            this.modelingFeatureList = modelingFeatureList;
        }

        public Boolean getAllowColdItemPlacement() {
            return allowColdItemPlacement;
        }

        public void setAllowColdItemPlacement(Boolean allowColdItemPlacement) {
            this.allowColdItemPlacement = allowColdItemPlacement;
        }

        public Boolean getEnableFeatureCorrelation() {
            return enableFeatureCorrelation;
        }

        public void setEnableFeatureCorrelation(Boolean enableFeatureCorrelation) {
            this.enableFeatureCorrelation = enableFeatureCorrelation;
        }

        public String getReasoningFeatureList() {
            return reasoningFeatureList;
        }

        public void setReasoningFeatureList(String reasoningFeatureList) {
            this.reasoningFeatureList = reasoningFeatureList;
        }

        public Boolean getEnableU2I() {
            return enableU2I;
        }

        public void setEnableU2I(Boolean enableU2I) {
            this.enableU2I = enableU2I;
        }

        public RandomSplitterParameters getRandomSplitterParameters() {
            return randomSplitterParameters;
        }

        public void setRandomSplitterParameters(RandomSplitterParameters randomSplitterParameters) {
            this.randomSplitterParameters = randomSplitterParameters;
        }

        public DateSplitterParameters getDateSplitterParameters() {
            return dateSplitterParameters;
        }

        public void setDateSplitterParameters(DateSplitterParameters dateSplitterParameters) {
            this.dateSplitterParameters = dateSplitterParameters;
        }

        private static class RandomSplitterParameters implements Parameters {
            private Integer testPercent;
            private Integer randomSeed;

            public Integer getTestPercent() {
                return testPercent;
            }

            public void setTestPercent(Integer testPercent) {
                this.testPercent = testPercent;
            }

            public Integer getRandomSeed() {
                return randomSeed;
            }

            public void setRandomSeed(Integer randomSeed) {
                this.randomSeed = randomSeed;
            }

            @Override
            public String getBuildType() {
                return null;
            }

            public IBuilder getBuilder(){
                return builder();
            }

            public static Builder builder() {
                return new Builder(new RandomSplitterParameters());
            }

            public static final class Builder implements IBuilder {
                private final RandomSplitterParameters parameters;

                private Builder(RandomSplitterParameters parameters) {
                    this.parameters = parameters;
                }

                public Builder init(Request request) {
                    getOptional(request, "testPercent").ifPresent(p -> parameters.setTestPercent(toInt(p)));
                    getOptional(request, "randomSeed").ifPresent(p -> parameters.setRandomSeed(toInt(p)));
                    return this;
                }

                @Override
                public URI buildURI() throws URISyntaxException {
                    return null;
                }

                @Override
                public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                    return null;
                }

                public RandomSplitterParameters build() {
                    return parameters;
                }
            }
        }

        private static class DateSplitterParameters implements Parameters {
            private String splitDate;

            public String getSplitDate() {
                return splitDate;
            }

            public void setSplitDate(String splitDate) {
                this.splitDate = splitDate;
            }

            @Override
            public String getBuildType() {
                return null;
            }

            public IBuilder getBuilder(){
                return builder();
            }

            public static Builder builder() {
                return new Builder(new DateSplitterParameters());
            }

            public static final class Builder implements IBuilder {
                private final DateSplitterParameters parameters;

                private Builder(DateSplitterParameters parameters) {
                    this.parameters = parameters;
                }

                public Builder init(Request request) {
                    parameters.setSplitDate(request.queryParams("splitDate"));
                    return this;
                }

                @Override
                public URI buildURI() throws URISyntaxException {
                    return null;
                }

                @Override
                public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                    return null;
                }

                public DateSplitterParameters build() {
                    return parameters;
                }
            }
        }
    }

    private static class RankParameters implements Parameters {

        public static final String RANK = "rank";

        @Override
        public String getBuildType() {
            return RANK;
        }

        public IBuilder getBuilder(){
            return builder();
        }

        public static Builder builder() {
            return new Builder(new RankParameters());
        }

        public static final class Builder implements IBuilder {
            private final RankParameters parameters;

            private Builder(RankParameters parameters) {
                this.parameters = parameters;
            }

            public Builder init(Request request) {
                return this;
            }

            @Override
            public URI buildURI() throws URISyntaxException {
                return null;
            }

            @Override
            public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                return null;
            }

            public RankParameters build() {
                return parameters;
            }
        }
    }

    private static class FbtParameters implements Parameters {

        public static final String FBT = "fbt";

        @Override
        public String getBuildType() {
            return FBT;
        }

        public IBuilder getBuilder(){
            return builder();
        }

        public static Builder builder() {
            return new Builder(new FbtParameters());
        }

        public static final class Builder implements IBuilder {
            private final FbtParameters parameters;

            private Builder(FbtParameters parameters) {
                this.parameters = parameters;
            }

            public Builder init(Request request) {
                return this;
            }

            @Override
            public URI buildURI() throws URISyntaxException {
                return null;
            }

            @Override
            public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                return null;
            }

            public FbtParameters build() {
                return parameters;
            }
        }
    }

    private static class SarParameters implements Parameters {

        public static final String SAR = "sar";

        @Override
        public String getBuildType() {
            return SAR;
        }

        public IBuilder getBuilder(){
            return builder();
        }

        public static Builder builder() {
            return new Builder(new SarParameters());
        }

        public static final class Builder implements IBuilder {
            private final SarParameters parameters;

            private Builder(SarParameters parameters) {
                this.parameters = parameters;
            }

            public Builder init(Request request) {
                return this;
            }

            @Override
            public URI buildURI() throws URISyntaxException {
                return null;
            }

            @Override
            public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                return null;
            }

            public SarParameters build() {
                return parameters;
            }
        }
    }
}
