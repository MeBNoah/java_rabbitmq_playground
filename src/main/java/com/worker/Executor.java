package com.worker;


import com.exchange.direct.explicit.DirectMessageSender;
import com.helper.ConnectionHelper;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 22.06.16.
 */
public class Executor {
    private static final String TASK_QUEUE_NAME = "task_queue";
    private String name;

    private Executor(String name){
        this.name = name;
    }

    public static void main(String[] argv) throws Exception {
        Executor e1 = new Executor("exec 1: ");
        e1.listen();
        Executor e2 = new Executor("exec 2: ");
        e2.listen();
        Executor e3 = new Executor("exec 3: ");
        e3.listen();
    }

    private void listen() throws IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(Server.EXCHANGE_NAME, "direct");
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        //hier kann man auch mehrere routing keys binden
        channel.queueBind(TASK_QUEUE_NAME, Server.EXCHANGE_NAME, Server.ROUTING_KEY);

        System.out.println(this.name + " [*] Waiting for messages");

        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(Executor.this.name + " [x] Received '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println(Executor.this.name + " [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
