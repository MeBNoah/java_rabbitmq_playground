package com.exchange.direct.explicit;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by noahispas on 21.06.16.
 */
public class DirectMessageReceiver {

    private final static String QUEUE_NAME = "executor_queue";

    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {

        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DirectMessageSender.EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //hier kann man auch mehrere routing keys binden
        channel.queueBind(QUEUE_NAME, DirectMessageSender.EXCHANGE_NAME, DirectMessageSender.ROUTING_KEY);

        System.out.println(" [*] Waiting for messages");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
        }
    }
}
