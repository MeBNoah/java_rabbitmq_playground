package com.exchange.fanout;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class Subscriber {

    private static final int listener = 10;
    private String name;

    private Subscriber(String name){
        this.name = name;
    }

    public static void main(String ... args) {
        for(int i = 0; i < listener; i++){
            Subscriber subscriber = new Subscriber("subscriber-" + i);
            try {
                subscriber.listen();
            } catch (IOException | TimeoutException e) {
                System.out.print(subscriber.name + "failed");
                e.printStackTrace();
            }
        }
    }

    private void listen() throws IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(BroadCaster.EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, BroadCaster.EXCHANGE_NAME, "");

        System.out.println(this.name + " [*] Waiting for messages.");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        new Thread(() -> {
            while(true){
                try {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    System.out.println(Subscriber.this.name + " [x] Received '" + message + "'");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
