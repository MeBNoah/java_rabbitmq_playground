package com.exchange.direct.implicit;

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

    private final static String QUEUE_NAME = "hello_queue";

    public static void main (String ... args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        //Wird hier ebenfalls deklariert um sicher zu stellen dass sie da ist bevor wir nachrichten von ihr konsumieren
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. ");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }

}
