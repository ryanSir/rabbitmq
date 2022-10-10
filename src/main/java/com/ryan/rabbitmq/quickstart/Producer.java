package com.ryan.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Procuder, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class Producer {

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

        //4. 通过Chanel发送数据
        String message = "hello rabbitmq!";
        for (int i = 0; i < 5; i++) {
            // 1. exchange如果没有指定，会默认使用第一个default exchange，2. routingKey: 找和路由key同名的queue
            channel.basicPublish("", "test001", null, message.getBytes());
        }

        //5. 记得要关闭连接
        channel.close();
        connection.close();
    }
}
