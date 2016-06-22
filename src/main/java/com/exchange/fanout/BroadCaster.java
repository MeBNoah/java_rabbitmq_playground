package com.exchange.fanout;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class BroadCaster {
    public static final String EXCHANGE_NAME = "news";
    private static final String BROADCAST_MSG = "public announcement";

    public static void main(String ... args) throws IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.basicPublish(EXCHANGE_NAME, "", null, BROADCAST_MSG.getBytes());
        System.out.println(" [x] Sent '" + BROADCAST_MSG + "'");

        channel.close();
        connection.close();
    }
}
