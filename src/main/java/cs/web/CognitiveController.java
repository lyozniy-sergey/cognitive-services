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

import java.io.IOException;
import java.net.URI;

import static cs.util.Path.Web.GET_FACE_RECOGNIZE;
import static cs.util.Path.Web.GET_REC_BY_USER;
import static spark.Spark.get;

/**
 * This class encapsulates the controllers for the cognitive service calls.
 */
public class CognitiveController {

    public static void main(String[] args) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        get(getRecommendToUserBy(httpclient));
        get(getFaceRecognize(httpclient));
    }

    private static Route getRecommendToUserBy(final HttpClient httpclient) {
        return executeCSCall(GET_REC_BY_USER, httpclient, RecommendationParameters.builder());
    }

    private static Route getFaceRecognize(final HttpClient httpclient) {
        return executeCSCall(GET_FACE_RECOGNIZE, httpclient, FaceParameters.builder());
    }

    private static Route executeCSCall(String path, HttpClient httpclient, IBuilder featureBuilder) {
        return new Route(path) {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    URI uri = featureBuilder.init(request).buildURI();
                    HttpResponse httpResponse = httpclient.execute(featureBuilder.buildRequest(uri));

                    return getJsonResult(httpResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    halt(500);
                }
                return null;
            }
        };
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
        System.out.println(formatJson(jsonString));
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
}
