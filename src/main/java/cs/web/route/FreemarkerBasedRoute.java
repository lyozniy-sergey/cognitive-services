package cs.web.route;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static spark.Spark.halt;

/**
 * @author lyozniy.sergey on 18 Oct 2017.
 */
public class FreemarkerBasedRoute implements Route {
    protected final Template template;
    protected final Map<String, Object> attributes;

    public FreemarkerBasedRoute(Configuration configuration, String templateName, Map<String, Object> attributes) throws IOException {
        template = configuration.getTemplate(templateName);
        this.attributes = attributes;
    }

    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            template.process(attributes, writer);
        } catch (Exception e) {
            throw halt(500);
        }

        return writer;
    }
}
