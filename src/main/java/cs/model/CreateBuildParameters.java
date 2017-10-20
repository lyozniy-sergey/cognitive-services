package cs.model;

import com.google.common.collect.ImmutableMap;
import cs.build.IBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
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
public class CreateBuildParameters extends ModelParameters {
    public final static String URI_BASE = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/%s/builds";
    private String description;
    private String buildType;
    private Map<String, Parameters> buildParameters;

    @Override
    protected String setupUriBase() {
        return URI_BASE;
    }

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
        return new Builder(new CreateBuildParameters());
    }

    public static final class Builder extends ModelBuilder implements IBuilder {
        private final CreateBuildParameters parameters;

        private Builder(CreateBuildParameters parameters) {
            super(parameters);
            this.parameters = parameters;
        }

        public Builder init(Request request) {
            super.init(request);
            parameters.setBuildType(getOptional(request, "buildType").orElseThrow(() -> throwException("Build type is not set")));
            parameters.setDescription(getOptional(request, "description").orElse(""));
            Optional.ofNullable(parameters.getBuildParameters().get(parameters.getBuildType())).orElseThrow(()->throwException("Build type is not valid")).getBuilder().init(request);
            return this;
        }

        public URIBuilder buildURI() throws URISyntaxException {
            return super.buildURIBuilder();
        }

        public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
            HttpPost httpPost = buildHeader(new HttpPost(uri));
            // Request body.
            StringEntity reqEntity = new StringEntity(toJson(parameters));
            httpPost.setEntity(reqEntity);
            return httpPost;
        }

        public Builder setDescription(String description) {
            parameters.setDescription(description);
            return this;
        }

        public Builder setBuildType(String buildType) {
            parameters.setBuildType(buildType);
            return this;
        }

        public CreateBuildParameters build() {
            return parameters;
        }
    }

    private static class BuildParametersHolder {
        private static final Map<String, Parameters> buildTypes = ImmutableMap.<String, Parameters>builder().
                put(RankParameters.RANK, new RankParameters()).
                put(BuildRecommendationParameters.RECOMMENDATION, new BuildRecommendationParameters()).
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

    private static class BuildRecommendationParameters implements Parameters {
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

        public IBuilder getBuilder() {
            return builder(this);
        }

        public static Builder builder() {
            return builder(new BuildRecommendationParameters());
        }

        public static Builder builder(BuildRecommendationParameters bp) {
            return new Builder(bp);
        }

        public static final class Builder implements IBuilder {
            private final BuildRecommendationParameters parameters;

            private Builder(BuildRecommendationParameters parameters) {
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
            public URIBuilder buildURI() throws URISyntaxException {
                return null;
            }

            @Override
            public HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException {
                return null;
            }

            public BuildRecommendationParameters build() {
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

        public IBuilder getBuilder() {
            return builder(this);
        }

        public static Builder builder() {
            return builder(new RandomSplitterParameters());
        }

        public static Builder builder(RandomSplitterParameters rsp) {
            return new Builder(rsp);
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
            public URIBuilder buildURI() throws URISyntaxException {
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

        public IBuilder getBuilder() {
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
            public URIBuilder buildURI() throws URISyntaxException {
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

    private static class RankParameters implements Parameters {

        public static final String RANK = "rank";

        @Override
        public String getBuildType() {
            return RANK;
        }

        public IBuilder getBuilder() {
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
            public URIBuilder buildURI() throws URISyntaxException {
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
        private Integer supportThreshold;
        private Integer maxItemSetSize;
        private Integer popularItemBenchmarkWindow;
        private Double minimalScore;
        private String similarityFunction;
        private Boolean enableModelingInsights;
        private String splitterStrategy;
        private RandomSplitterParameters randomSplitterParameters;
        private DateSplitterParameters dateSplitterParameters;

        public Integer getSupportThreshold() {
            return supportThreshold;
        }

        public void setSupportThreshold(Integer supportThreshold) {
            this.supportThreshold = supportThreshold;
        }

        public Integer getMaxItemSetSize() {
            return maxItemSetSize;
        }

        public void setMaxItemSetSize(Integer maxItemSetSize) {
            this.maxItemSetSize = maxItemSetSize;
        }

        public Double getMinimalScore() {
            return minimalScore;
        }

        public void setMinimalScore(Double minimalScore) {
            this.minimalScore = minimalScore;
        }

        public String getSimilarityFunction() {
            return similarityFunction;
        }

        public void setSimilarityFunction(String similarityFunction) {
            this.similarityFunction = similarityFunction;
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

        public Integer getPopularItemBenchmarkWindow() {
            return popularItemBenchmarkWindow;
        }

        public void setPopularItemBenchmarkWindow(Integer popularItemBenchmarkWindow) {
            this.popularItemBenchmarkWindow = popularItemBenchmarkWindow;
        }

        @Override
        public String getBuildType() {
            return FBT;
        }

        public IBuilder getBuilder() {
            return builder(this);
        }

        public static Builder builder() {
            return builder(new FbtParameters());
        }

        public static Builder builder(FbtParameters rsp) {
            return new Builder(rsp);
        }

        public static final class Builder implements IBuilder {
            private final FbtParameters parameters;

            private Builder(FbtParameters parameters) {
                this.parameters = parameters;
            }

            public Builder init(Request request) {
                getOptional(request, "supportThreshold").ifPresent(p -> parameters.setSupportThreshold(toInt(p)));
                getOptional(request, "maxItemSetSize").ifPresent(p -> parameters.setMaxItemSetSize(toInt(p)));
                getOptional(request, "popularItemBenchmarkWindow").ifPresent(p -> parameters.setPopularItemBenchmarkWindow(toInt(p)));
                getOptional(request, "minimalScore").ifPresent(p -> parameters.setMinimalScore(Double.valueOf(p)));

                getOptional(request, "enableModelingInsights").ifPresent(p -> parameters.setEnableModelingInsights(toBoolean(p)));

                parameters.setSimilarityFunction(request.queryParams("similarityFunction"));
                parameters.setSplitterStrategy(request.queryParams("splitterStrategy"));

                parameters.setRandomSplitterParameters(RandomSplitterParameters.builder().init(request).build());
                parameters.setDateSplitterParameters(DateSplitterParameters.builder().init(request).build());
                return this;
            }

            @Override
            public URIBuilder buildURI() throws URISyntaxException {
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
        private Integer supportThreshold;
        private Integer popularItemBenchmarkWindow;
        private String similarityFunction;
        private Boolean enableModelingInsights;
        private String splitterStrategy;
        private RandomSplitterParameters randomSplitterParameters;
        private DateSplitterParameters dateSplitterParameters;
        private String cooccurrenceUnit;
        private Boolean enableColdItemPlacement;
        private Boolean enableColdToColdRecommendations;
        private Boolean enableU2I;
        private Boolean enableUserAffinity;
        private Boolean allowSeedItemsInRecommendations;
        private Boolean enableBackfilling;

        public Integer getSupportThreshold() {
            return supportThreshold;
        }

        public void setSupportThreshold(Integer supportThreshold) {
            this.supportThreshold = supportThreshold;
        }

        public Integer getPopularItemBenchmarkWindow() {
            return popularItemBenchmarkWindow;
        }

        public void setPopularItemBenchmarkWindow(Integer popularItemBenchmarkWindow) {
            this.popularItemBenchmarkWindow = popularItemBenchmarkWindow;
        }

        public String getSimilarityFunction() {
            return similarityFunction;
        }

        public void setSimilarityFunction(String similarityFunction) {
            this.similarityFunction = similarityFunction;
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

        public String getCooccurrenceUnit() {
            return cooccurrenceUnit;
        }

        public void setCooccurrenceUnit(String cooccurrenceUnit) {
            this.cooccurrenceUnit = cooccurrenceUnit;
        }

        public Boolean getEnableColdItemPlacement() {
            return enableColdItemPlacement;
        }

        public void setEnableColdItemPlacement(Boolean enableColdItemPlacement) {
            this.enableColdItemPlacement = enableColdItemPlacement;
        }

        public Boolean getEnableColdToColdRecommendations() {
            return enableColdToColdRecommendations;
        }

        public void setEnableColdToColdRecommendations(Boolean enableColdToColdRecommendations) {
            this.enableColdToColdRecommendations = enableColdToColdRecommendations;
        }

        public Boolean getEnableU2I() {
            return enableU2I;
        }

        public void setEnableU2I(Boolean enableU2I) {
            this.enableU2I = enableU2I;
        }

        public Boolean getEnableUserAffinity() {
            return enableUserAffinity;
        }

        public void setEnableUserAffinity(Boolean enableUserAffinity) {
            this.enableUserAffinity = enableUserAffinity;
        }

        public Boolean getAllowSeedItemsInRecommendations() {
            return allowSeedItemsInRecommendations;
        }

        public void setAllowSeedItemsInRecommendations(Boolean allowSeedItemsInRecommendations) {
            this.allowSeedItemsInRecommendations = allowSeedItemsInRecommendations;
        }

        public Boolean getEnableBackfilling() {
            return enableBackfilling;
        }

        public void setEnableBackfilling(Boolean enableBackfilling) {
            this.enableBackfilling = enableBackfilling;
        }

        @Override
        public String getBuildType() {
            return SAR;
        }

        public IBuilder getBuilder() {
            return builder(this);
        }

        public static Builder builder() {
            return builder(new SarParameters());
        }

        public static Builder builder(SarParameters rsp) {
            return new Builder(rsp);
        }

        public static final class Builder implements IBuilder {
            private final SarParameters parameters;

            private Builder(SarParameters parameters) {
                this.parameters = parameters;
            }

            @Override
            public Builder init(Request request) {
                getOptional(request, "supportThreshold").ifPresent(p -> parameters.setSupportThreshold(toInt(p)));
                getOptional(request, "popularItemBenchmarkWindow").ifPresent(p -> parameters.setPopularItemBenchmarkWindow(toInt(p)));

                getOptional(request, "enableModelingInsights").ifPresent(p -> parameters.setEnableModelingInsights(toBoolean(p)));
                getOptional(request, "enableColdItemPlacement").ifPresent(p -> parameters.setEnableColdItemPlacement(toBoolean(p)));
                getOptional(request, "enableColdToColdRecommendations").ifPresent(p -> parameters.setEnableColdToColdRecommendations(toBoolean(p)));
                getOptional(request, "enableU2I").ifPresent(p -> parameters.setEnableU2I(toBoolean(p)));
                getOptional(request, "enableUserAffinity").ifPresent(p -> parameters.setEnableUserAffinity(toBoolean(p)));
                getOptional(request, "allowSeedItemsInRecommendations").ifPresent(p -> parameters.setAllowSeedItemsInRecommendations(toBoolean(p)));
                getOptional(request, "enableBackfilling").ifPresent(p -> parameters.setEnableBackfilling(toBoolean(p)));

                parameters.setCooccurrenceUnit(request.queryParams("cooccurrenceUnit"));
                parameters.setSimilarityFunction(request.queryParams("similarityFunction"));
                parameters.setSplitterStrategy(request.queryParams("splitterStrategy"));

                parameters.setRandomSplitterParameters(RandomSplitterParameters.builder().init(request).build());
                parameters.setDateSplitterParameters(DateSplitterParameters.builder().init(request).build());
                return this;
            }

            @Override
            public URIBuilder buildURI() throws URISyntaxException {
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
