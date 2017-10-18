package cs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author lyozniy.sergey on 03 Oct 2017.
 */
public final class JsonHandler {
    private static Logger logger = LoggerFactory.getLogger(JsonHandler.class);
    public static String getJsonResult(HttpResponse httpResponse) throws IOException {
        String jsonString = null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            jsonString = prettify(EntityUtils.toString(entity).trim());
            printJson(jsonString);
        }
        return jsonString;
//        return new Gson().toJson(StatusRes.SUCCESS,new Gson().toJsonTree(jsonString));
    }

    public static void printJson(String jsonString) {
        System.out.println(jsonString);
        logger.debug("Response: {}", jsonString);
    }

    public static String prettify(String jsonText) {
        if (jsonText == null || jsonText.isEmpty()) {
            return "";
        }
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonText);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (jsonElement.isJsonArray()) {
            JsonArray json = jsonElement.getAsJsonArray();
            return gson.toJson(json);
        } else {
            JsonObject json = jsonElement.getAsJsonObject();
            return gson.toJson(json);
        }
    }

}
