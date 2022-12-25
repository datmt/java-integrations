package com.datmt.rabbitmq.exchanges.direct;

import com.rabbitmq.client.Channel;
import helpers.ConnectionHelper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class DirectExchange {
    private static final String DIRECT_EXCHANGE_NAME = "datmt-direct-exchange";


    public static void main(String[] args) throws IOException, TimeoutException {
    }

    private static void consumeMessagesFromQueue() throws IOException, TimeoutException {

        var connection = ConnectionHelper.createConnection();
        // create a channel
        Channel channel = connection.createChannel();

        channel.basicConsume("datmt-direct-queue-1", true, (consumerTag, message) -> {
            System.out.println("Received message: " + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println("Consumer cancelled");
        });

        // close the channel and connection
        channel.close();
        connection.close();
    }

    private static void sendMessageToDirectExchange() throws IOException, TimeoutException {

        var connection = ConnectionHelper.createConnection();

        // create a channel
        Channel channel = connection.createChannel();

        // create a direct exchange
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, "direct");
        channel.queueDeclare("datmt-direct-queue-1", false, false, false, null);
        channel.queueBind("datmt-direct-queue-1", DIRECT_EXCHANGE_NAME, "datmt.com-routing-key");

        channel.basicPublish(DIRECT_EXCHANGE_NAME, "datmt.com-routing-key", null, "Hello DatMT".getBytes());

        // close the channel and connection
        channel.close();
        connection.close();
    }

}
