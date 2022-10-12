package com.ryan.rabbitmq.i_limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 自定义消费者监听
 *
 * @author ryan
 * @version Id: MyConsumer, v 0.1 2022/10/12 9:33 AM ryan Exp $
 */
public class MyConsumerLimit extends DefaultConsumer {

    private Channel channel;

    public MyConsumerLimit(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("----- consumer message -----");
        System.out.println("consumerTage: " + consumerTag);
        System.out.println("envelope: " + envelope);
        System.out.println("properties: " + properties);
        System.out.println("body: " + new String(body));

        // 表示消息已经处理完了，可以继续接收消息
        // multiple:false 不批量签收，和消费端设置的QOS相关
        channel.basicAck(envelope.getDeliveryTag(), false);
    }


}
