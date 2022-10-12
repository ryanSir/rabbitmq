package com.ryan.rabbitmq.j_ack;

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
public class MyConsumerLimit extends DefaultConsumer {

    private Channel channel;

    public MyConsumerLimit(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("----- consumer message -----");
        System.out.println("body: " + new String(body));

        if ((Integer) properties.getHeaders().get("num") == 0) {
            // requeue:true 失败的消息会重新回退到队列，生产环境要设为false
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            channel.basicAck(envelope.getDeliveryTag(),false);
        }
    }


}
