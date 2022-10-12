package com.ryan.rabbitmq.b_quickstart_direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * exchange direct 模式
 *
 * @author ryan
 * @version Id: Procuder, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ProducerDirect {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("20.50.54.194");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("my_rabbit");

        //2. 通过链接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3. 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4. 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        //5. 发送
        String message = "hello rabbitmq for direct exchange message!";
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

        //5. 记得要关闭连接
        channel.close();
        connection.close();
    }
}
