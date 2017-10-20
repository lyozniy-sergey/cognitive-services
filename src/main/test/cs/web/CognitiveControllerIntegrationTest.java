package cs.web;

import com.sun.org.glassfish.gmbal.Description;
import cs.build.IBuilder;
import cs.model.CreateBuildParameters;
import cs.model.CreateModelParameters;
import cs.model.DeleteBuildParameters;
import cs.model.FaceParameters;
import cs.model.FileUploadParameters;
import cs.model.GetBuildParameters;
import cs.model.GetItemRecommendationParameters;
import cs.model.GetUserRecommendationParameters;
import cs.web.route.BaseRoute;
import cs.web.route.TestBaseRoute;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static cs.util.Path.Web.Headers.CATALOG_DISPLAY_NAME;
import static cs.util.Path.Web.Headers.USAGE_DISPLAY_NAME;
import static cs.util.Path.Web.Requests.CATALOG_PARAM;
import static cs.util.Path.Web.Requests.USAGE_PARAM;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * It tests already prepared data before and which are still valid otherwise parameters should be changed.
 * Test contains methods for creating new data but they all are ignored.
 * It is necessary to check endpoints after changes
 * @author lyozniy.sergey on 19 Oct 2017.
 */
public class CognitiveControllerIntegrationTest {
    private HttpClient httpClient;

    @Before
    public void init() {
        httpClient = HttpClients.createDefault();
    }

    @Test
    @Description("Test gets face detect result. It works till jpg url is valid")
    public void testFaceDetect() {
        Object res = getBaseRoute(FaceParameters.builder().
                setSource("https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg").
                setReturnFaceId(true).
                setUriBase(FaceParameters.URI_BASE).
                setSubscriptionKey("4b28731ec9694254855d154f1cc1f31a")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("faceId"));
        assertTrue(res.toString().contains("faceRectangle"));
    }

    @Ignore
    @Description("Test creates model")
    public void testCreateModel() {
        String modelName = "Auto_Test_Model";
        String modelDescription = "Auto Test Model. Can be deleted";

        Object res = getBaseRoute(CreateModelParameters.builder().
                setModelName(modelName).
                setDescription(modelDescription).
                setUriBase(CreateModelParameters.URI_BASE).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("id"));
        assertTrue(res.toString().contains("createdDateTime"));
        assertTrue(res.toString().contains("activeBuildId"));
        assertTrue(res.toString().contains(modelName));
        assertTrue(res.toString().contains(modelDescription));
    }

    @Ignore
    @Description("Test uploads catalog")
    public void testUploadCatalog() throws IOException {
        String modelId = "b1a1e954-ab2e-4da3-9aaa-6ecee7b08166";
        String displayName = "Auto_Catalog";

        Object res = getBaseRoute(FileUploadParameters.builder(CATALOG_DISPLAY_NAME, CATALOG_PARAM).
                setDisplayName(displayName).
                setSource(Arrays.asList("23191,Starter Kit,Camera Kits", "110561,COMPACT CHARGER,Sony")).
                transformUriBase(FileUploadParameters.URI_BASE, modelId, CATALOG_PARAM).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("\"processedLineCount\": 2"));
        assertTrue(res.toString().contains("\"errorLineCount\": 0"));
    }

    @Ignore
    @Description("Test uploads usage")
    public void testUploadUsage() throws IOException {
        String modelId = "b1a1e954-ab2e-4da3-9aaa-6ecee7b08166";
        String displayName = "Auto_Usage";

        Object res = getBaseRoute(FileUploadParameters.builder(USAGE_DISPLAY_NAME, USAGE_PARAM).
                setDisplayName(displayName).
                setSource(Arrays.asList("936ffba3-51b4-437c-bb25-302a302fb776,288420,2015-07-10T00:42:29", "936ffba3-51b4-437c-bb25-302a302fb776,356970,2016-01-09T23:43:24")).
                transformUriBase(FileUploadParameters.URI_BASE, modelId, USAGE_PARAM).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("fileId"));
        assertTrue(res.toString().contains("\"processedLineCount\": 2"));
        assertTrue(res.toString().contains("\"errorLineCount\": 0"));
    }

    @Ignore
    @Description("Test creates build")
    public void testCreateBuild() {
        String modelId = "b1a1e954-ab2e-4da3-9aaa-6ecee7b08166";
        String buildType = "recommendation";

        Object res = getBaseRoute(CreateBuildParameters.builder().
                setBuildType(buildType).
                setDescription("Test build").
                transformUriBase(CreateBuildParameters.URI_BASE, modelId).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("buildId"));
    }

    @Ignore
    @Description("Test deletes build")
    public void testDeleteBuild() {
        String modelId = "1fa58243-75ab-4e6c-97fc-9cdf96db3f76";
        Integer buildId = 1659053;

        Object res = getBaseRoute(DeleteBuildParameters.builder().
                transformUriBase(DeleteBuildParameters.URI_BASE, modelId, buildId).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().isEmpty());
    }

    @Test
    @Description("Test gets existing build. Otherwise test will fail")
    public void testGetBuild() {
        String modelId = "1fa58243-75ab-4e6c-97fc-9cdf96db3f76";
        Integer buildId = 1661908;
        Object res = getBaseRoute(GetBuildParameters.builder().
                transformUriBase(GetBuildParameters.URI_BASE, modelId, buildId).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains(modelId));
        assertTrue(res.toString().contains("status"));
        assertTrue(res.toString().contains(String.valueOf(buildId)));
    }

    @Test
    @Description("Test gets user recommendation. It works only for real model and user id")
    public void testGetUserRecommendation() {
        String modelId = "72b9bed9-fae9-455b-9030-bbe2ff4f5a6a";
        String userId="936ffba3-51b4-437c-bb25-302a302fb776";
        Integer numberOfResults=10;

        Object res = getBaseRoute(GetUserRecommendationParameters.builder().
                setUserId(userId).
                setNumberOfResults(numberOfResults).
                transformUriBase(GetUserRecommendationParameters.URI_BASE, modelId).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("recommendedItems"));
        assertTrue(res.toString().contains("items"));
        assertTrue(res.toString().contains("rating"));
    }

    @Test
    @Description("Test gets item recommendation. It works only for real model and item id")
    public void testGetItemRecommendation() {
        String modelId = "1fa58243-75ab-4e6c-97fc-9cdf96db3f76";
        String itemIds="23191";
        Integer numberOfResults=10;
        Double minimalScore=0.0;

        Object res = getBaseRoute(GetItemRecommendationParameters.builder().
                setMinimalScore(minimalScore).
                setItemsIds(itemIds).
                setNumberOfResults(numberOfResults).
                transformUriBase(GetItemRecommendationParameters.URI_BASE, modelId).
                setSubscriptionKey("abe7eea3a1e94cb4bf150735292971ce")
        ).handle(null, null);

        assertNotNull(res);
        assertTrue(res.toString().contains("recommendedItems"));
        assertTrue(res.toString().contains("items"));
        assertTrue(res.toString().contains("rating"));
    }

    private BaseRoute getBaseRoute(IBuilder builder) {
        return new TestBaseRoute(httpClient, builder);
    }
}
