package app;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class Application extends Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        EventBus eventBus = vertx.eventBus();

        DeploymentOptions deploymentOptions = new DeploymentOptions();

        deploymentOptions.setConfig(new JsonObject().put("port", 8080));

        vertx.deployVerticle("app.MyVerticle", deploymentOptions);
    }
}

