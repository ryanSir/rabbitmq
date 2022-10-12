package com.ryan.rabbitmq.e_message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Procuder, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ProducerMessage {

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

        Map<String,Object> headers = new HashMap<>();
        headers.put("my1","111");
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                // 持久化模式2，服务重启消息不会丢失
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                // 10秒钟，消息没有被消费，消息自动移除
                .expiration("10000")
                // 自定义属性可以放在headers里
                .headers(headers)
                .build();

        //4. 通过Chanel发送数据
        String message = "hello rabbitmq!";
        for (int i = 0; i < 5; i++) {
            // 1. exchange如果没有指定，会默认使用第一个default exchange，2. routingKey: 找和路由key同名的queue
            channel.basicPublish("", "test001", properties, message.getBytes());
        }

        //5. 记得要关闭连接
        channel.close();
        connection.close();
    }
}
