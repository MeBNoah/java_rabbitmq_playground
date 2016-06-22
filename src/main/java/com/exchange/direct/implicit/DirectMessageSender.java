package com.exchange.direct.implicit;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class DirectMessageSender {

    private final static String QUEUE_NAME = "hello_queue";

    public static void main (String ... args) throws IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

}
