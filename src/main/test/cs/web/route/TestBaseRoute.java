package cs.web.route;

import cs.build.IBuilder;
import org.apache.http.client.HttpClient;
import spark.Request;

/**
 * @author lyozniy.sergey on 20 Oct 2017.
 */
public class TestBaseRoute extends BaseRoute {
    public TestBaseRoute(HttpClient httpClient, IBuilder builder) {
        super(httpClient, builder);
    }

    protected IBuilder init(Request request) {
        return builder;
    }
}
