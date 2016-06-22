package com.exchange.topic;

import com.helper.ConnectionHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by noahispas on 21.06.16.
 */
public class TopicSender {

    public static final String EXCHANGE_NAME = "topic_news";

    public static final String ROUTING_KEY_SPORT_IMPORTANT = "sport.important";
    public static final String ROUTING_KEY_SPORT_UNIMPORTANT = "sport.unimportant";
    public static final String ROUTING_KEY_POLITICS_IMPORTANT = "politics.important";

    public static void main(String ... args) throws IOException, TimeoutException {
        Connection connection = ConnectionHelper.connect();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");


        String importantSportMsg = "important sport msg";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_SPORT_IMPORTANT, null, importantSportMsg.getBytes());
        System.out.println(" [x] Sent '" + ROUTING_KEY_SPORT_IMPORTANT + "':'" + importantSportMsg + "'");

        String unimportantSportMsg = "unimportant sport msg";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_SPORT_UNIMPORTANT, null, unimportantSportMsg.getBytes());
        System.out.println(" [x] Sent '" + ROUTING_KEY_SPORT_UNIMPORTANT + "':'" + unimportantSportMsg + "'");

        String importantPoliticsMsg = "important politics msg";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_POLITICS_IMPORTANT, null, importantPoliticsMsg.getBytes());
        System.out.println(" [x] Sent '" + ROUTING_KEY_POLITICS_IMPORTANT + "':'" + importantPoliticsMsg + "'");

        channel.close();
        connection.close();
    }
}
