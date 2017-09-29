package cs.model;

import org.apache.http.client.methods.HttpUriRequest;
import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lyozniy.sergey on 29 Sep 2017.
 */
public interface IBuilder {
    IBuilder init(Request request);

    URI buildURI() throws URISyntaxException;

    HttpUriRequest buildRequest(URI uri) throws UnsupportedEncodingException;
}
