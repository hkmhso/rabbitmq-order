package com.sunnsoft.rabbitmq.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HKM
 * 消息确认触发回调函数的情况：
 *  ①消息推送到server，但是在server里找不到交换机
 *      结论： ①这种情况触发的是 ConfirmCallback 回调函数。
 *  ②消息推送到server，找到交换机了，但是没找到队列
 *      结论：②这种情况触发的是 ConfirmCallback和RetrunCallback两个回调函数。
 *  ③消息推送到sever，交换机和队列啥都没找到
 *      结论： ③这种情况触发的是 ConfirmCallback 回调函数。
 *  ④消息推送成功
 *      结论： ④这种情况触发的是 ConfirmCallback 回调函数。
 */
@Component
public class RabbitConfirmAndReturn implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{

    /**
     * 确认消息是否正确发送到交换机(Exchange)回调的函数
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("ConfirmCallback:     "+"消息id："+correlationData.getId());
        System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
        System.out.println("ConfirmCallback:     "+"确认情况："+ack);
        System.out.println("ConfirmCallback:     "+"原因："+cause);
        //当找到了交换机的情况返回true，否则返回false
        if (ack) {
            System.out.println("消息发送确认成功");
        } else {
            System.out.println("消息发送确认失败:" + cause);
            //使用确认机制，一直发送，直到消息发送确认成功
            //System.out.println("ConfirmCallback:     "+"producerEmailDirect："+producerEmailDirect);
            //producerEmailDirect.sendForNotScheduled(DirectRabbitConfig.EXCHANGE_NAME);
        }
    }

    /**
     * 确认消息是否正确发送到队列(Queue)回调的函数
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("ReturnCallback:     "+"消息："+message);
        System.out.println("ReturnCallback:     "+"回应码："+replyCode);
        System.out.println("ReturnCallback:     "+"回应信息："+replyText);
        System.out.println("ReturnCallback:     "+"交换机："+exchange);
        System.out.println("ReturnCallback:     "+"路由键："+routingKey);
    }


}
