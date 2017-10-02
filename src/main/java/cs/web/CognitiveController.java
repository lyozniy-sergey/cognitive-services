/*
 * Copyright 2012-2016 MongDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cs.web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cs.model.CatalogParameters;
import cs.model.FaceParameters;
import cs.model.IBuilder;
import cs.model.RecommendationParameters;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

import static cs.util.Path.Web.GET_FACE_RECOGNIZE;
import static cs.util.Path.Web.GET_REC_BY_USER;
import static cs.util.Path.Web.UPLOAD_CATALOG;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 * This class encapsulates the controllers for the cognitive service calls.
 */
public class CognitiveController {

    public static void main(String[] args) throws IOException {
        port(8082);
        HttpClient httpclient = HttpClients.createDefault();
        get("/c", (req, res) ->
                "<form method='post' action='/upload_catalog' enctype='multipart/form-data'>" // note the enctype
                        + "<input type='file' name='upload_catalog' accept='.csv'>" // make sure to call getPart using the same "name" in the post
                        + "<input type='hidden' name='uriBase' value='https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/catalog'>"
                        + "<input type='hidden' name='subscriptionKey' value='abe7eea3a1e94cb4bf150735292971ce'>"
                        + "<input type='hidden' name='catalogDisplayName' value='Catalog3'>"
                        + "<button>Upload file</button>"
                        + "</form>"
        );
        get(GET_REC_BY_USER, getRecommendToUserBy(httpclient));
        get(GET_FACE_RECOGNIZE, getFaceRecognizeBy(httpclient));
        post(UPLOAD_CATALOG, uploadCatalog(httpclient));
    }

    private static Route getRecommendToUserBy(final HttpClient httpclient) {
        return new BaseRoute(httpclient, RecommendationParameters.builder());
    }

    private static Route getFaceRecognizeBy(final HttpClient httpclient) {
        return new BaseRoute(httpclient, FaceParameters.builder());
    }

    private static Route uploadCatalog(final HttpClient httpclient) {
        return new MultipartRoute(httpclient, CatalogParameters.builder());
    }

    private static String getJsonResult(HttpResponse httpResponse) throws IOException {
        String jsonString = null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            jsonString = formatJson(EntityUtils.toString(entity).trim());
            printJson(jsonString);
        }
//        return new Gson().toJson(jsonString, String.class);
        return jsonString;
    }

    private static void printJson(String jsonString) {
        System.out.println(prettify(jsonString));
    }

    private static String formatJson(String jsonString) {
        if (jsonString.charAt(0) == '[') {
            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray.toString(2);
        } else if (jsonString.charAt(0) == '{') {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.toString(2);
        } else {
            return jsonString;
        }
    }

    public static String prettify(String jsonText) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonText);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(jsonElement.isJsonArray()){
            JsonArray json = jsonElement.getAsJsonArray();
            return gson.toJson(json);
        }
        else {
            JsonObject json = jsonElement.getAsJsonObject();
            return gson.toJson(json);
        }
    }

    public static class BaseRoute implements Route {
        protected IBuilder builder;
        protected HttpClient httpClient;

        public BaseRoute(HttpClient httpClient, IBuilder builder) {
            this.builder = builder;
            this.httpClient = httpClient;
        }

        @Override
        public Object handle(Request request, Response response) {
            try {
                URI uri = builder.init(request).buildURI();
                HttpResponse httpResponse = httpClient.execute(builder.buildRequest(uri));

                return getJsonResult(httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
                throw halt(500);
            }
        }
    }

    public static class MultipartRoute extends BaseRoute {
        public static final String FILE_CONTENT = "fileContent";

        public MultipartRoute(HttpClient httpClient, IBuilder builder) {
            super(httpClient, builder);
        }

        @Override
        public Object handle(Request request, Response response) {
            try {
//                File uploadDir = new File("upload");
//                if(!uploadDir.mkdir()){
//                    halt(500, "Can't create temporary dir for storing upload file");
//                    // create the upload directory if it doesn't exist
//                }
//
//                staticFiles.externalLocation("upload");
//                staticFiles.expireTime(600);
//
//                Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

                try (InputStream input = request.raw().getPart("upload_catalog").getInputStream()) { // getPart needs to use same "name" as input field in form
//                    Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    // skip the header of the csv
//                    inputList = br.lines().skip(1).map(function).collect(Collectors.toList());
                    request.attribute(FILE_CONTENT, br.lines().collect(Collectors.toList()));
                }
                return super.handle(request, response);
            } catch (Exception exc) {
                exc.printStackTrace();
                throw halt(500);
            }
        }
    }
}
