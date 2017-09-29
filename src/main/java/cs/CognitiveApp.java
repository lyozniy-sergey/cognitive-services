package cs;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

/**
 * @author lyozniy.sergey on 21 Sep 2017.
 */
public class CognitiveApp {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    public static final String subscriptionKey = "abe7eea3a1e94cb4bf150735292971ce";

    // Replace or verify the region.
    //
    // You must use the same region in your REST API call as you used to obtain your subscription keys.
    // For example, if you obtained your subscription keys from the westus region, replace
    // "westcentralus" in the URI below with "westus".
    //
    // NOTE: Free trial subscription keys are generated in the westcentralus region, so if you are using
    // a free trial subscription key, you should not need to change this region.
//    public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
//    public static final String uriBase = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";
//    public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    public static final String uriBase = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/recommend/user";


    public static void main(String[] args) {
        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
//            builder.setParameter("returnFaceId", "true");
//            builder.setParameter("returnFaceLandmarks", "false");
//            builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

//            builder.setParameter("modelId", "b1a1e954-ab2e-4da3-9aaa-6ecee7b08166");
            builder.setParameter("userId", "00034001CFAC09B3");
            builder.setParameter("numberOfResults", "150");
//            builder.setParameter("includeMetadata", "true");
//            builder.setParameter("buildId", "1657098");
            // Prepare the URI for the REST API call.
            URI uri = builder.build();
//            HttpPost request = new HttpPost(uri);
            HttpGet request = new HttpGet(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity("{body}");

//            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
//                System.out.println(EntityUtils.toString(entity));
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                } else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));
                } else {
                    System.out.println(jsonString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
