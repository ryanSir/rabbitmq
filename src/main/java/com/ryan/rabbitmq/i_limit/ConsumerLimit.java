package com.ryan.rabbitmq.i_limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 自定义消费者监听
 *
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ConsumerLimit {

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

        //4. 声明交换机和队列，然后进行绑定设置，最后指定路由Key
        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.#";
        String queueName = "test_qos_queue";

        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //5. 限流方式  第一件事autoAck设置为false
        // prefetchSize:消息大小限制，0表示不限制；prefetchCount:3，每次处理三条消息，并返回ack，才会继续接受消息，一般设置为1，一条一条处理； global:限制级别，false：consumer级别 ture：channel级别
        channel.basicQos(0,1,false);
        channel.basicConsume(queueName, false, new MyConsumerLimit(channel));


    }

}
