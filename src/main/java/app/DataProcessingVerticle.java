package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.Map;

public class DataProcessingVerticle extends AbstractVerticle {

    UserStorage storage = new UserStorage();

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("/all", getAllUsers());

        eventBus.consumer("/create", createUser());

        eventBus.consumer("/read", readUser());

        eventBus.consumer("/update", updateUser());

        eventBus.consumer("/delete", deleteUser());

        System.out.println("DataVerticle has started.");
    }

    private Handler<Message<Object>> deleteUser() {
        return message -> {
            try {
                String parameter = (String) message.body();
                int id = Integer.parseInt(parameter.substring(parameter.indexOf("=") + 1));
                boolean result = storage.delete(id);
                message.reply("Successfully: " + result);
            } catch (RuntimeException e) {
                message.reply("ERROR" + e.getMessage());
            }
        };
    }

    private Handler<Message<Object>> updateUser() {
        return message -> {
            try {
                String[] params = ((String) message.body()).split("&");
                int id = Integer.parseInt(params[0].substring(params[0].indexOf("=") + 1));
                String name = params[1].substring(params[1].indexOf("=") + 1);
                int age = Integer.parseInt(params[2].substring(params[2].indexOf("=") + 1));
                boolean result = storage.update(id, name, age);
                message.reply("Successfully: " + result);
            } catch (RuntimeException e) {
                message.reply("ERROR: " + e.getMessage());
            }
        };
    }

    private Handler<Message<Object>> readUser() {
        return message -> {
            try {
                String parameter = (String) message.body();
                int id = Integer.parseInt(parameter.substring(parameter.indexOf("=") + 1));
                User result = storage.read(id);
                message.reply(result.toString());
            } catch (RuntimeException e) {
                message.reply("ERROR" + e.getMessage());
            }
        };
    }

    private Handler<Message<Object>> createUser() {
        return message -> {
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
        };
    }

    private Handler<Message<Object>> getAllUsers() {
        return message -> {
            Map<Integer, User> result = storage.getAll();
            message.reply(result.toString());
        };
    }

    @Override
    public void stop() throws Exception {

    }
}
