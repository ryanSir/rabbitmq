package com.ryan.rabbitmq.a_quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class Consumer {

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

        //4. 声明一个队列,启动后会自动添加到管控台   durable:是否持久化，ture,即使服务器重启，队列也不会被消失， exclusive：独占，ture,顺序消费 autoDelete:脱离了exchange，会自动删除
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        //5. 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6. 设置Channel
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端：" + msg);
        }

    }

}
