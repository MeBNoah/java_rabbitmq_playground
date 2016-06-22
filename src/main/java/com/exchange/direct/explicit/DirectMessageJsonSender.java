package com.exchange.direct.explicit;

import com.google.gson.Gson;
import com.helper.ConnectionHelper;
import com.helper.ExecutorMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 22.06.16.
 */
public class DirectMessageJsonSender {

    public static final String EXCHANGE_NAME = "execute";
    public static final String ROUTING_KEY = "executor_msg";

    public static void main(String ... args) throws java.io.IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        Gson gson = new Gson();
        ExecutorMessage executorMessage = new ExecutorMessage(ExecutorMessage.Command.dothat, "this is the payload");
        String executorMessageJson = gson.toJson(executorMessage);

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null,  executorMessageJson.getBytes());
        System.out.println(" [x] Sent '" + ROUTING_KEY + "':'" + executorMessageJson + "'");

        channel.close();
        connection.close();
    }

}
