package com.exchange.direct.explicit;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class DirectMessageSender {
    public static final String EXCHANGE_NAME = "selective_news";
    public static final String ROUTING_KEY = "important";
    public static final String SEVERITY_UNIMPORTANT = "unimportant";

    private static final String IMPORTANT_MSG = "very important announcement";
    private static final String UNIMPORTANT_MSG = "blablabla";

    public static void main(String ... args) throws java.io.IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, IMPORTANT_MSG.getBytes());
        System.out.println(" [x] Sent '" + ROUTING_KEY + "':'" + IMPORTANT_MSG + "'");

        channel.basicPublish(EXCHANGE_NAME, SEVERITY_UNIMPORTANT, null, UNIMPORTANT_MSG.getBytes());
        System.out.println(" [x] Sent '" + SEVERITY_UNIMPORTANT + "':'" + UNIMPORTANT_MSG + "'");

        channel.close();
        connection.close();
    }
}
