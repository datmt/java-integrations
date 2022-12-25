package com.datmt.rabbitmq.exchanges.headers;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeadersExchange {
    private static final String HEADER_EXCHANGE_NAME = "datmt-headers-exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        sendMessageToHeadersExchange();
    }

    private static void sendMessageToHeadersExchange() throws IOException, TimeoutException {
        // create a connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        // create a connection
        Connection connection = factory.newConnection();

        // create a channel
        Channel channel = connection.createChannel();

        // create a financialHeaders exchange
        channel.exchangeDeclare(HEADER_EXCHANGE_NAME, "headers");

        //configure financialHeaders
        Map<String, Object> financialHeaders = new HashMap<>();
        financialHeaders.put("department", "financial");

        Map<String, Object> accountingHeaders = new HashMap<>();
        accountingHeaders.put("department", "accounting");


        // declare and bind queue using financialHeaders
        channel.queueDeclare("financial-department-queue", false, false, false, null);
        channel.queueBind("financial-department-queue", HEADER_EXCHANGE_NAME, "", financialHeaders);

        // declare and bind queue using financialHeaders
        channel.queueDeclare("accounting-department-queue", false, false, false, null);
        channel.queueBind("accounting-department-queue", HEADER_EXCHANGE_NAME, "", accountingHeaders);

        // publish message to financialHeaders exchange
        var financialProps = new AMQP.BasicProperties().builder().headers(financialHeaders).build();
        channel.basicPublish(HEADER_EXCHANGE_NAME, "", financialProps, "News for the financial department".getBytes());


        var accountingProps = new AMQP.BasicProperties().builder().headers(accountingHeaders).build();
        channel.basicPublish(HEADER_EXCHANGE_NAME, "", accountingProps, "News for the accounting department".getBytes());

        // close the channel and connection
        channel.close();
        connection.close();

    }

}
