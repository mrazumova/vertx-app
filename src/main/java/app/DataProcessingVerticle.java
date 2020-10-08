package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.util.Map;

public class DataProcessingVerticle extends AbstractVerticle {

    UserStorage storage = new UserStorage();

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("/all", message -> {
            Map<Integer, User> result = storage.getAll();
            message.reply(result.toString());
        });

        eventBus.consumer("/create", message -> {
            System.out.println("create");
            String[] params = ((String) message.body()).split("&");
            try {
                int id = Integer.parseInt(params[0].substring(params[0].indexOf("=") + 1));
                String name = params[1].substring(params[1].indexOf("=") + 1);
                int age = Integer.parseInt(params[2].substring(params[2].indexOf("=") + 1));
                boolean result = storage.create(id, name, age);
                message.reply("Successfully: " + result);
            } catch (RuntimeException e) {
                message.reply("ERROR" + e.getMessage());
            }
        });

        eventBus.consumer("/read", message -> {
            try {
                String parameter = (String) message.body();
                int id = Integer.parseInt(parameter.substring(parameter.indexOf("=") + 1));
                User result = storage.read(id);
                message.reply(result.toString());
            } catch (RuntimeException e) {
                message.reply("ERROR" + e.getMessage());
            }
        });

        eventBus.consumer("/update", message -> {
            try {
                String[] params = ((String) message.body()).split("&");
                int id = Integer.parseInt(params[0].substring(params[0].indexOf("=") + 1));
                String name = params[1].substring(params[1].indexOf("=") + 1);
                int age = Integer.parseInt(params[2].substring(params[2].indexOf("=") + 1));
                boolean result = storage.update(id, name, age);
                message.reply("Successfully: " + result);
            } catch (RuntimeException e){
                message.reply("ERROR: " + e.getMessage());
            }
        });

        eventBus.consumer("/delete", message -> {
            try {
                String parameter = (String) message.body();
                int id = Integer.parseInt(parameter.substring(parameter.indexOf("=") + 1));
                boolean result = storage.delete(id);
                message.reply("Successfully: " + result);
            } catch (RuntimeException e) {
                message.reply("ERROR" + e.getMessage());
            }
        });

        System.out.println("DataVerticle has started.");
    }

    @Override
    public void stop() throws Exception {

    }
}
