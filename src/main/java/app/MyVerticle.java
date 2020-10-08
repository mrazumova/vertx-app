package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
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
                    System.out.println("INCOMING REQUEST : " + httpServerRequest.method() + " " + uri);

                    int idx = uri.indexOf("?");
                    String address;
                    if (idx == -1)
                        address = uri;
                    else
                        address = uri.substring(0, uri.indexOf("?"));

                    String params = httpServerRequest.query();

                    eventBus.request(address, params, processReply(httpServerRequest));
                }
        );

        server.listen(8080);

        System.out.println("MyVerticle has started on port 8080.");
    }

    private Handler<AsyncResult<Message<Object>>> processReply(HttpServerRequest httpServerRequest) {
        return reply -> {
            HttpServerResponse httpServerResponse = httpServerRequest.response();
            if (reply.succeeded()) {
                httpServerResponse.setStatusCode(200);
                httpServerResponse.end(reply.result().body().toString());
            } else {
                httpServerResponse.setStatusCode(501);
                httpServerResponse.end(reply.cause().getMessage());
            }
        };
    }

}

/*Every event loop is attached to a thread. By default Vert.x attaches 2 event loops per CPU core thread.
The direct consequence is that a regular verticle always processes events on the same thread, so there is no need to use
thread coordination mechanisms to manipulate a verticle state (e.g, Java class fields).Incoming network data are being
received from accepting threads then passed as events to the corresponding verticles.
When a verticle opens a network server and is deployed more than once, then the events are being distributed to
the verticle instances in a round-robin fashion which is very useful for maximizing CPU usage with lots of concurrent
networked requests. Finally, verticles have a simple start / stop life-cycle, and verticles can deploy other verticles.*/

   /* The Vert.x Thread Model
        Verticles run in single thread mode. That means, that a verticle is only ever executed by a single thread, and always by the same thread.
        That means that you will never have to think about multithreading inside your verticle (unless you yourself start other threads which your verticle communicates with etc).
        Vert.x is capable of using all the CPUs in your machine, or cores in your CPU.
        Vert.x does this by creating one thread per CPU. Each thread can send messages to multiple verticles.
        Remember, verticles are event driven and are only running when receiving messages, so a verticle does not need to have its own exclusive thread.
        A single thread can distribute messages to multiple verticles.
        When a thread delivers a message to a verticle, the message handling code of that verticle is executed by the thread.
        The message delivery and message handling logic is executed by calling a method in a handler (listener object) registered by the verticle.
        Once the verticle's message handling logic finishes, the thread can deliver a message to another verticle.*/

   /* Both send and publish are used to send a message to an event bus address. However there are some differences between the two.

        By using publish:

        A message is sent to one or multiple listeners
        All handlers listening against the address will be notified
        No answer is expected from handlers


        By using send:

        A message is sent to one and only one handler registered against the event bus address.
        If multiple handlers are registered, only one will be notified. The receiver will be selected by a "round-robin algorithm" as per the docs.
        The receiver can answer the message, this answer can be empty or contain a response body. A response timeout can also be specified.
        In practical usage, publish is quite useful to inform that an event has occurred, whereas send is quite handy for asking a treatment where the response matters.

        Conceptually, publish uses the publish/subscribe pattern whereas send uses the request/response pattern.*/