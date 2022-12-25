package com.datmt.rabbitmq.exchanges.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        fanoutExchangePublish();
    }

    private static final String FANOUT_EXCHANGE_NAME = "datmt-fanout-exchange";

    private static void fanoutExchangePublish() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        // create a connection
        Connection connection = factory.newConnection();

        // create a channel
        Channel channel = connection.createChannel();

        // create a direct exchange
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
        channel.queueDeclare("datmt-fanout-queue-1", false, false, false, null);
        channel.queueBind("datmt-fanout-queue-1", FANOUT_EXCHANGE_NAME,"fanout-binding-key-1");

        channel.queueDeclare("datmt-fanout-queue-2", false, false, false, null);
        channel.queueBind("datmt-fanout-queue-2", FANOUT_EXCHANGE_NAME,"fanout-binding-key-2");


        channel.basicPublish(FANOUT_EXCHANGE_NAME, "throw-away-routing-key", null, "Hello DatMT's fans".getBytes());

        // close the channel and connection
        channel.close();
        connection.close();
    }
}
