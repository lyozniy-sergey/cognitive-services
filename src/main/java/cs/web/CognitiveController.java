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


import cs.model.CreateBuildParameters;
import cs.model.CreateModelParameters;
import cs.model.DeleteBuildParameters;
import cs.model.FaceParameters;
import cs.model.FileUploadParameters;
import cs.model.GetBuildParameters;
import cs.model.RecommendationParameters;
import cs.model.UpdateModelParameters;
import cs.web.route.BaseRoute;
import cs.web.route.MultipartRoute;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static cs.util.Path.Web.CREATE_BUILD;
import static cs.util.Path.Web.CREATE_MODEL;
import static cs.util.Path.Web.DELETE_BUILD;
import static cs.util.Path.Web.GET_BUILD;
import static cs.util.Path.Web.GET_FACE_RECOGNIZE;
import static cs.util.Path.Web.GET_REC_BY_ITEM;
import static cs.util.Path.Web.GET_REC_BY_USER;
import static cs.util.Path.Web.Headers.CATALOG;
import static cs.util.Path.Web.Headers.USAGE;
import static cs.util.Path.Web.UPDATE_MODEL;
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
        get("/c", (req, res) -> getUploadCatalogForm());
        get("/u", (req, res) -> getUploadUsageForm());
        get(CREATE_MODEL, new BaseRoute(httpclient, CreateModelParameters.builder()));
        get(UPDATE_MODEL, new BaseRoute(httpclient, UpdateModelParameters.builder()));
        post(UPLOAD_CATALOG, new MultipartRoute(httpclient, FileUploadParameters.builder(CATALOG)));
        post(UPLOAD_USAGE, new MultipartRoute(httpclient, FileUploadParameters.builder(USAGE)));
        get(CREATE_BUILD, new BaseRoute(httpclient, CreateBuildParameters.builder()));
        get(DELETE_BUILD, new BaseRoute(httpclient, DeleteBuildParameters.builder()));
        get(GET_BUILD, new BaseRoute(httpclient, GetBuildParameters.builder()));
        get(GET_REC_BY_USER, new BaseRoute(httpclient, RecommendationParameters.builder()));
        get(GET_REC_BY_ITEM, new BaseRoute(httpclient, RecommendationParameters.builder()));
        get(GET_FACE_RECOGNIZE, new BaseRoute(httpclient, FaceParameters.builder()));
    }

    private static String getUploadUsageForm() {
        return getUploadForm("upload_usage", "usage","usageDisplayName", "Usage1");
    }

    private static String getUploadCatalogForm() {
        return getUploadForm("upload_catalog", "catalog","catalogDisplayName", "Catalog1");
    }

    private static String getUploadForm(String action, String uri, String header, String value) {
        return String.format("<form method='post' action='/%s' enctype='multipart/form-data'>"
                + "<input type='file' name='upload' accept='.csv'>"
                + "<input type='hidden' name='uriBase' value='https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/b1a1e954-ab2e-4da3-9aaa-6ecee7b08166/%s'>"
                + "<input type='hidden' name='subscriptionKey' value='abe7eea3a1e94cb4bf150735292971ce'>"
                + "<input type='text' name='%s' value='%s'>"
                + "<button>%s</button>"
                + "</form>", action, uri, header, value, action);
    }
}
