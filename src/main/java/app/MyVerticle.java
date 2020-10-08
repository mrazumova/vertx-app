package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;


public class MyVerticle extends AbstractVerticle {

    private HttpServer server;

    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        server = vertx.createHttpServer();
        eventBus = vertx.eventBus();

        server.requestHandler(
                httpServerRequest -> {
                    String uri = httpServerRequest.uri();
                    System.out.println("INCOMING REQUEST : " + httpServerRequest.method() + " " + uri + " " + httpServerRequest.query());

                    int idx = uri.indexOf("?");
                    String address;
                    if (idx == -1)
                        address = uri;
                    else
                        address = uri.substring(0, uri.indexOf("?"));

                    String params = httpServerRequest.query();

                    eventBus.request(address, params, reply -> {
                        HttpServerResponse httpServerResponse = httpServerRequest.response();
                        if (reply.succeeded()) {
                            httpServerResponse.setStatusCode(200);
                            httpServerResponse.end(reply.result().body().toString());
                        } else {
                            httpServerResponse.setStatusCode(501);
                            httpServerResponse.end(reply.cause().getMessage());
                        }
                    });
                }
        );

        server.listen(8080);

        System.out.println("MyVerticle has started on port 8080.");
    }

}
