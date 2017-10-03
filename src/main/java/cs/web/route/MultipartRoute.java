package cs.web.route;

import cs.build.IBuilder;
import org.apache.http.client.HttpClient;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static spark.Spark.halt;

/**
 * @author lyozniy.sergey on 03 Oct 2017.
 */
public class MultipartRoute extends BaseRoute {
    public static final String FILE_CONTENT = "fileContent";

    public MultipartRoute(HttpClient httpClient, IBuilder builder) {
        super(httpClient, builder);
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("upload").getInputStream()) { // getPart needs to use same "name" as input field in form
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
