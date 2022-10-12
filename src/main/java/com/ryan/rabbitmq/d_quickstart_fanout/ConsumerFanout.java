package com.ryan.rabbitmq.d_quickstart_fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
@SuppressWarnings("all")
public class ConsumerFanout {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("20.50.54.194");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("my_rabbit");

        // 是否支持重连，每三秒执行一次
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        //2. 通过链接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3. 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4. 声明
        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        String queueName = "test_fanout_queue";
        String routingKey = ""; // 路由key不设置
        // 声明交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        // 声明队列 durable:是否持久化，ture,即使服务器重启，队列也不会被消失， exclusive：独占，ture,顺序消费 autoDelete:脱离了exchange，会自动删除
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);

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
