package cs.web.route;

import cs.build.IBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URI;

import static cs.util.JsonHandler.getJsonResult;
import static spark.Spark.halt;

/**
 * @author lyozniy.sergey on 03 Oct 2017.
 */
public class BaseRoute implements Route {
    protected IBuilder builder;
    protected HttpClient httpClient;

    public BaseRoute(HttpClient httpClient, IBuilder builder) {
        this.builder = builder;
        this.httpClient = httpClient;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            URI uri = builder.init(request).buildURI().build();
            HttpResponse httpResponse = httpClient.execute(builder.buildRequest(uri));

            return getJsonResult(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw halt(500);
        }
    }

}
