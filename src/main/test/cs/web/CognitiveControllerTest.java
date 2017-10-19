package cs.web;

import cs.build.IBuilder;
import cs.model.CreateBuildParameters;
import cs.model.CreateModelParameters;
import cs.model.FaceParameters;
import cs.model.FileUploadParameters;
import cs.model.GetBuildParameters;
import cs.model.GetUserRecommendationParameters;
import cs.util.JsonHandler;
import cs.web.route.BaseRoute;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Arrays;

import static cs.util.Path.Web.Headers.CATALOG_DISPLAY_NAME;
import static cs.util.Path.Web.Headers.USAGE_DISPLAY_NAME;
import static cs.util.Path.Web.Requests.CATALOG_PARAM;
import static cs.util.Path.Web.Requests.USAGE_PARAM;
import static cs.web.route.MultipartRoute.FILE_CONTENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author lyozniy.sergey on 19 Oct 2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CognitiveControllerTest {
    @Mock
    private HttpClient httpClient;
    @Mock
    private Request request;
    @Mock
    private Response response;
    private String subscriptionKey = "abe7eea3a1e94cb4bf150735292971ce";

    @Test
    public void testFaceDetect() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("[ { \"faceId\": \"cc821ee6-9746-4dad-ad78-31befeb93f48\", \"faceRectangle\": { \"top\": 131, \"left\": 177, \"width\": 162, \"height\": 162 } } ]");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("source")).thenReturn("https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg");
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(FaceParameters.builder()).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testCreateModel() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("{ \"id\": \"1fa58243-75ab-4e6c-97fc-9cdf96db3f76\", \"name\": \"Model2\", \"description\": \"\", \"createdDateTime\": \"2017-10-18T09:57:04Z\", \"activeBuildId\": -1 }");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelName")).thenReturn("Model2");
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(CreateModelParameters.builder()).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testUploadCatalog() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("{ \"processedLineCount\": 534, \"errorLineCount\": 0, \"importedLineCount\": 534, \"errorSummary\": [], \"sampleErrorDetails\": [] }");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelId")).thenReturn("1fa58243-75ab-4e6c-97fc-9cdf96db3f76");
        when(request.attribute(FILE_CONTENT)).thenReturn(Arrays.asList("23191,Starter Kit,Camera Kits", "110561,COMPACT CHARGER,Sony"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(FileUploadParameters.builder(CATALOG_DISPLAY_NAME, CATALOG_PARAM)).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testUploadUsage() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("{ \"fileId\": \"3dc960c0-87ac-457d-afa7-018b75499cc4\", \"processedLineCount\": 44, \"errorLineCount\": 0, \"importedLineCount\": 44, \"errorSummary\": [], \"sampleErrorDetails\": [] }");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelId")).thenReturn("1fa58243-75ab-4e6c-97fc-9cdf96db3f76");
        when(request.attribute(FILE_CONTENT)).thenReturn(Arrays.asList("936ffba3-51b4-437c-bb25-302a302fb776,288420,2015-07-10T00:42:29", "936ffba3-51b4-437c-bb25-302a302fb776,356970,2016-01-09T23:43:24"));
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(FileUploadParameters.builder(USAGE_DISPLAY_NAME, USAGE_PARAM)).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testCreateBuild() throws IOException {
        HttpResponse httpResponse = getResponse(201);
        String expectedOutput = JsonHandler.prettify("{ \"buildId\": 1661906 }");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelId")).thenReturn("1fa58243-75ab-4e6c-97fc-9cdf96db3f76");
        when(request.queryParams("buildType")).thenReturn("recommendation");
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(CreateBuildParameters.builder()).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testGetBuild() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("{ \"type\": \"BuildModel\", \"status\": \"Succeeded\", \"createdDateTime\": \"2017-10-18T13:39:26\", \"percentComplete\": 0, \"resourceLocation\": \"https://westus.api.cognitive.microsoft.com/recommendations/v4.0/operations/1661906?buildId\\u003d1661906\", \"result\": { \"id\": 1661906, \"type\": \"Recommendation\", \"modelName\": \"Hybris_Model\", \"modelId\": \"72b9bed9-fae9-455b-9030-bbe2ff4f5a6a\", \"status\": \"Succeeded\", \"startDateTime\": \"2017-10-18T13:39:26\", \"endDateTime\": \"2017-10-18T13:41:50\" } }");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelId")).thenReturn("1fa58243-75ab-4e6c-97fc-9cdf96db3f76");
        when(request.queryParams("buildId")).thenReturn("1661906");
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(GetBuildParameters.builder()).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    @Test
    public void testGetUserRecommendation() throws IOException {
        HttpResponse httpResponse = getResponse(200);
        String expectedOutput = JsonHandler.prettify("{\"recommendedItems\": [{\"items\": [{ \"id\": \"S4T-00021\", \"name\": \"MS STORES 12 MONTH LIVE SUBSCRIPTION ESD US ONLY\"}],\"rating\": 0.50104714205762,\"reasoning\": [\"Default recommendation for \\u0027O365 Personal 1Mo 32/64 Alng SubPKL Onln MSStore DwnLdC2R NR RtlSyndctn\\u0027\"]}]}");
        httpResponse.setEntity(new StringEntity(expectedOutput));

        when(request.queryParams("subscriptionKey")).thenReturn(subscriptionKey);
        when(request.queryParams("modelId")).thenReturn("1fa58243-75ab-4e6c-97fc-9cdf96db3f76");
        when(request.queryParams("userId")).thenReturn("936ffba3-51b4-437c-bb25-302a302fb776");
        when(request.queryParams("numberOfResults")).thenReturn("10");
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        Object res = getBaseRoute(GetUserRecommendationParameters.builder()).handle(request, response);

        assertNotNull(res);
        assertEquals(expectedOutput, res);
    }

    private BaseRoute getBaseRoute(IBuilder builder) {
        return new BaseRoute(httpClient, builder);
    }

    private HttpResponse getResponse(Integer statusCode) {
        return new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), statusCode, ""));
    }
}
