package com.ryan.rabbitmq.returnlistener;

import com.rabbitmq.client.*;

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

        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        //5. 发送一条消息
        String message = "hello rabbitmq send return message!";

        // 消息不可达监听，发送消息的时候，mandatory必须设置为true，监听才会生效
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("---- handle return ----");
                System.out.println("replyCode: " + replyCode);
                System.out.println("replyText: " + replyText);
                System.out.println("exchange: " + exchange);
                System.out.println("routingKey: " + routingKey);
                System.out.println("properties: " + properties);
                System.out.println("body: " + new String(body));
            }
        });

//        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

        // mandatory:true 消息路由不到，消息不会自动删除
        channel.basicPublish(exchangeName, routingKeyError, true,null, message.getBytes());


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
