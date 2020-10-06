package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;


public class MyVerticle extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start() throws Exception {
        server = vertx.createHttpServer();

        server.requestHandler(
                httpServerRequest -> {
                    if (httpServerRequest.method() == HttpMethod.GET) {
                        HttpServerResponse httpServerResponse = httpServerRequest.response();
                        String query = httpServerRequest.query();
                        httpServerResponse.setStatusCode(200);
                    }
                    if (httpServerRequest.method() == HttpMethod.POST) {
                        HttpServerResponse httpServerResponse = httpServerRequest.response();
                        httpServerResponse.setStatusCode(200);
                    }
                    if (httpServerRequest.method() == HttpMethod.DELETE) {
                        HttpServerResponse httpServerResponse = httpServerRequest.response();
                        httpServerResponse.setStatusCode(200);
                    }
                }
        );

        server.listen(config().getInteger("port"));
    }

}
