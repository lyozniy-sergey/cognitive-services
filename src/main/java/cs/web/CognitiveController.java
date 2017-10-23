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
import cs.model.GetItemRecommendationParameters;
import cs.model.GetUserRecommendationParameters;
import cs.model.UpdateModelParameters;
import cs.web.route.BaseRoute;
import cs.web.route.FreemarkerBasedRoute;
import cs.web.route.MultipartRoute;
import freemarker.template.Configuration;
import freemarker.template.Version;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static cs.util.Path.Templates.WELCOME;
import static cs.util.Path.Web.CREATE_BUILD;
import static cs.util.Path.Web.CREATE_MODEL;
import static cs.util.Path.Web.DELETE_BUILD;
import static cs.util.Path.Web.GET_BUILD;
import static cs.util.Path.Web.GET_FACE_DETECT;
import static cs.util.Path.Web.GET_REC_BY_ITEM;
import static cs.util.Path.Web.GET_REC_BY_USER;
import static cs.util.Path.Web.HOME;
import static cs.util.Path.Web.Headers.CATALOG_DISPLAY_NAME;
import static cs.util.Path.Web.Headers.USAGE_DISPLAY_NAME;
import static cs.util.Path.Web.Requests.CATALOG_PARAM;
import static cs.util.Path.Web.Requests.USAGE_PARAM;
import static cs.util.Path.Web.UPDATE_MODEL;
import static cs.util.Path.Web.UPLOAD_CATALOG;
import static cs.util.Path.Web.UPLOAD_CATALOG_FORM;
import static cs.util.Path.Web.UPLOAD_USAGE;
import static cs.util.Path.Web.UPLOAD_USAGE_FORM;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 * This class encapsulates the controllers for the cognitive service calls.
 */
public class CognitiveController {

    public static void main(String[] args) throws IOException {
        port(8082);
        HttpClient httpClient = HttpClients.createDefault();
        final Configuration configuration = new Configuration(new Version(2, 3, 0));
        configuration.setClassForTemplateLoading(CognitiveController.class, "/");

        get(HOME, new FreemarkerBasedRoute(configuration, WELCOME, null));
        get(UPLOAD_CATALOG_FORM, (req, res) -> getUploadCatalogForm());
        get(UPLOAD_USAGE_FORM, (req, res) -> getUploadUsageForm());
        get(CREATE_MODEL, new BaseRoute(httpClient, CreateModelParameters.builder()));
        get(UPDATE_MODEL, new BaseRoute(httpClient, UpdateModelParameters.builder()));
        post(UPLOAD_CATALOG, new MultipartRoute(httpClient, FileUploadParameters.builder(CATALOG_DISPLAY_NAME, CATALOG_PARAM)));
        post(UPLOAD_USAGE, new MultipartRoute(httpClient, FileUploadParameters.builder(USAGE_DISPLAY_NAME, USAGE_PARAM)));
        get(CREATE_BUILD, new BaseRoute(httpClient, CreateBuildParameters.builder()));
        get(DELETE_BUILD, new BaseRoute(httpClient, DeleteBuildParameters.builder()));
        get(GET_BUILD, new BaseRoute(httpClient, GetBuildParameters.builder()));
        get(GET_REC_BY_USER, new BaseRoute(httpClient, GetUserRecommendationParameters.builder()));
        get(GET_REC_BY_ITEM, new BaseRoute(httpClient, GetItemRecommendationParameters.builder()));
        get(GET_FACE_DETECT, new BaseRoute(httpClient, FaceParameters.builder()));
    }

    private static String getUploadUsageForm() {
        return getUploadForm("upload_usage", "usageDisplayName", "Usage2");
    }

    private static String getUploadCatalogForm() {
        return getUploadForm("upload_catalog", "catalogDisplayName", "Catalog2");
    }

    private static String getUploadForm(String action, String header, String value) {
        return String.format("<form method='post' action='/%s' enctype='multipart/form-data'>"
                + "<input type='file' name='upload' accept='.csv'>"
                + "<input type='hidden' name='modelId' value='0a423382-ebd0-4132-b7ec-0cdf9190743b'>"
                + "<input type='hidden' name='subscriptionKey' value='84f59c16e2d74a179af9d8f8ac03119a'>"
                + "<input type='text' name='%s' value='%s'>"
                + "<button>%s</button>"
                + "</form>", action, header, value, action);
    }
}
