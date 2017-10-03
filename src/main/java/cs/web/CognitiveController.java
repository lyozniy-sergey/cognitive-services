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
import cs.model.FileUploadParameters;
import cs.model.RecommendationParameters;
import cs.web.route.BaseRoute;
import cs.web.route.MultipartRoute;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Route;

import java.io.IOException;

import static cs.util.Path.Web.CREATE_MODEL;
import static cs.util.Path.Web.GET_FACE_RECOGNIZE;
import static cs.util.Path.Web.GET_REC_BY_USER;
import static cs.util.Path.Web.Headers.CATALOG;
import static cs.util.Path.Web.Headers.USAGE;
import static cs.util.Path.Web.UPLOAD_CATALOG;
import static cs.util.Path.Web.UPLOAD_USAGE;
import static spark.Spark.get;
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
                        + "<input type='file' name='upload' accept='.csv'>" // make sure to call getPart using the same "name" in the post
                        + "<input type='hidden' name='uriBase' value='https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/catalog'>"
                        + "<input type='hidden' name='subscriptionKey' value='abe7eea3a1e94cb4bf150735292971ce'>"
                        + "<input type='hidden' name='catalogDisplayName' value='Catalog1'>"
                        + "<button>Upload file</button>"
                        + "</form>"
        );
        get("/u", (req, res) ->
                "<form method='post' action='/upload_usage' enctype='multipart/form-data'>" // note the enctype
                        + "<input type='file' name='upload' accept='.csv'>" // make sure to call getPart using the same "name" in the post
                        + "<input type='hidden' name='uriBase' value='https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/usage'>"
                        + "<input type='hidden' name='subscriptionKey' value='abe7eea3a1e94cb4bf150735292971ce'>"
                        + "<input type='hidden' name='usageDisplayName' value='Usage1'>"
                        + "<button>Upload file</button>"
                        + "</form>"
        );
        get(GET_REC_BY_USER, getRecommendToUserBy(httpclient));
        get(GET_FACE_RECOGNIZE, getFaceRecognizeBy(httpclient));
        post(UPLOAD_CATALOG, uploadCatalog(httpclient));
        post(UPLOAD_USAGE, uploadUsage(httpclient));
        post(CREATE_MODEL, createModel(httpclient));
    }

    private static Route createModel(final HttpClient httpclient) {
        return new BaseRoute(httpclient, FaceParameters.builder());
    }

    private static Route getRecommendToUserBy(final HttpClient httpclient) {
        return new BaseRoute(httpclient, RecommendationParameters.builder());
    }

    private static Route getFaceRecognizeBy(final HttpClient httpclient) {
        return new BaseRoute(httpclient, FaceParameters.builder());
    }

    private static Route uploadCatalog(final HttpClient httpclient) {
        return new MultipartRoute(httpclient, FileUploadParameters.builder(CATALOG));
    }

    private static Route uploadUsage(final HttpClient httpclient) {
        return new MultipartRoute(httpclient, FileUploadParameters.builder(USAGE));
    }
}
