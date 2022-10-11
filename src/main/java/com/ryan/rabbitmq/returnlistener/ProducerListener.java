package com.ryan.rabbitmq.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Procuder, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ProducerListener {

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

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        //5. 发送一条消息
        String message = "hello rabbitmq send confirm message!";
        for (int i = 0; i < 5; i++) {
            // 1. exchange如果没有指定，会默认使用第一个default exchange，2. routingKey: 找和路由key同名的queue
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        }

        //6. 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                // 返回失败处理
                System.out.println("------ no ack ------");
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                // 返回成功处理
                System.out.println("------ ack ------");
            }
        });

        // 先不能关闭，否则监听不到
//        //5. 记得要关闭连接
//        channel.close();
//        connection.close();
    }
}
