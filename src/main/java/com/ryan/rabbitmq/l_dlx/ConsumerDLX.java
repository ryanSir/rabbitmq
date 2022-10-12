package com.ryan.rabbitmq.l_dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * DLX
 *
 * @author ryan
 * @version Id: Consumer, v 0.1 2022/10/10 1:53 PM ryan Exp $
 */
public class ConsumerDLX {

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

        //4. 声明交换机和队列，然后进行绑定设置，最后指定路由Key ==> 这里只是一个普通的配置
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        // 这个argument属性，要声明到队列上
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dlx.exchange");

        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 要进行死信队列的声明
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue","dlx.exchange","#");

        //5. 限流方式  第一件事autoAck设置为false
        channel.basicConsume(queueName, true, new MyConsumerDLX(channel));


    }

}
