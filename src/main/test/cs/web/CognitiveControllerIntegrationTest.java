package cs.web;

import com.sun.org.glassfish.gmbal.Description;
import cs.build.IBuilder;
import cs.model.CreateBuildParameters;
import cs.model.FaceParameters;
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
    public void testFaceDetect() throws IOException {
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
    public void testCreateBuild() throws IOException {
        String modelId = "1fa58243-75ab-4e6c-97fc-9cdf96db3f76";
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

    @Test
    @Description("Test gets existing build. Otherwise test will fail")
    public void testGetBuild() throws IOException {
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
    public void testGetUserRecommendation() throws IOException {
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
    public void testGetItemRecommendation() throws IOException {
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
