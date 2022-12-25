package com.datmt.rabbitmq.exchanges.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicExchange {

    private static final String TOPIC_EXCHANGE_NAME = "datmt-order-topic-exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        sendMessageToTopicExchange();
    }

    private static void sendMessageToTopicExchange() throws IOException, TimeoutException {
        // create a connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        // create a connection
        Connection connection = factory.newConnection();

        // create a channel
        Channel channel = connection.createChannel();

        // create a direct exchange
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, "topic");
        channel.queueDeclare("datmt-topic-order-queue-3", false, false, false, null);
        channel.queueBind("datmt-topic-order-queue-3", TOPIC_EXCHANGE_NAME, "datmt.com.order.#");

//        channel.basicPublish(TOPIC_EXCHANGE_NAME, "datmt-topic-routing-key", null, "Hello DatMT topic".getBytes());
        channel.basicPublish(TOPIC_EXCHANGE_NAME, "datmt.com.order.created", null, "Order created".getBytes());
        channel.basicPublish(TOPIC_EXCHANGE_NAME, "datmt.com.order.complete", null, "Order complete".getBytes());
        channel.basicPublish(TOPIC_EXCHANGE_NAME, "datmt.com.order.complete.then.refund", null, "Order complete then refund".getBytes());

        // close the channel and connection
        channel.close();
        connection.close();
    }
}
