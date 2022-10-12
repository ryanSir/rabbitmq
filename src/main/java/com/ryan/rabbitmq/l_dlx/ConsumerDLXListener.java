package com.ryan.rabbitmq.l_dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * DLX 监听
 *
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ConsumerDLXListener {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("20.50.54.194");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("my_rabbit");

        //2. 通过链接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3. 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        String exchangeName = "dlx.exchange";
        String routingKey = "#";
        String queueName = "dlx.queue";

        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //5. 限流方式  第一件事autoAck设置为false
        channel.basicConsume(queueName, true, new MyConsumerDLX(channel));

    }

}
