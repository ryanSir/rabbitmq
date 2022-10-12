package com.ryan.rabbitmq.i_limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Procuder, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ProducerLimit {

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

        //4. 制定消息投递模式: 消息确认模式
        channel.confirmSelect();

        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";

        //5. 发送一条消息
        String message = "hello rabbitmq send qos message!";

        // mandatory:true 消息路由不到，消息不会自动删除
        for (int i=0;i<5;i++){
            channel.basicPublish(exchangeName, routingKey, true, null, message.getBytes());
        }

        //5. 记得要关闭连接
        channel.close();
        connection.close();
    }
}
