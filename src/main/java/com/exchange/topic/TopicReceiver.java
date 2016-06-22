package com.exchange.topic;

import com.exchange.fanout.Subscriber;
import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class TopicReceiver {

    private String name;

    private TopicReceiver(String name){
        this.name = name;
    }

    public static void main(String ... args) throws Exception {
        TopicReceiver sportReceiver = new TopicReceiver("sport_message_receiver");
        sportReceiver.listen("sport.*");

        TopicReceiver importantListener =  new TopicReceiver("important_message_receiver");
        importantListener.listen("*.important");
    }

    private void listen(String bindingKey) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TopicSender.EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, TopicSender.EXCHANGE_NAME, bindingKey);

        System.out.println( this.name + " [*] Waiting for messages.");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        new Thread(() -> {
            while(true){
                try {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    System.out.println(TopicReceiver.this.name + " [x] Received '" + message + "'");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
