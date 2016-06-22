package com.exchange.direct.explicit;

import com.google.gson.Gson;
import com.helper.ConnectionHelper;
import com.helper.ExecutorMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 22.06.16.
 */
public class DirectMessageJsonReceiver {

    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {

        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DirectMessageJsonSender.EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        //hier kann man auch mehrere routing keys binden
        channel.queueBind(queueName, DirectMessageJsonSender.EXCHANGE_NAME, DirectMessageJsonSender.ROUTING_KEY);
        System.out.println(" [*] Waiting for messages");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            Gson gson = new Gson();
            ExecutorMessage executorMessage = gson.fromJson(message, ExecutorMessage.class);

            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
            System.out.println("Received Executormessage: " + executorMessage);
        }
    }

}
