package com.ryan.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 自定义消费者监听
 *
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ConsumerDefine {

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
        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.save";
        String queueName = "test_consumer_queue";

        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 替换
//        //5. 创建消费者
//        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
//        //6. 设置Channel
//        channel.basicConsume(queueName, true, queueingConsumer);
//        while (true) {
//            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
//            String msg = new String(delivery.getBody());
//            System.out.println("消费端：" + msg);
//        }

        channel.basicConsume(queueName, true, new MyConsumer(channel));


    }

}
