package com.ryan.rabbitmq.l_dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 自定义消费者监听,重回队列
 *
 * @author ryan
 * @version Id: MyConsumer, v 0.1 2022/10/12 9:33 AM ryan Exp $
 */
public class MyConsumerDLX extends DefaultConsumer {

    private Channel channel;

    public MyConsumerDLX(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("----- consumer message -----");
        System.out.println("body: " + new String(body));

        channel.basicAck(envelope.getDeliveryTag(), false);
    }


}
