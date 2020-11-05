package com.sunnsoft.rabbitmq.order.consumer.fanout;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sunnsoft.rabbitmq.order.commons.utils.JsonUtils;
import com.sunnsoft.rabbitmq.order.commons.utils.PrintUtils;
import com.sunnsoft.rabbitmq.order.config.fanout.FanoutRabbitConfig;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.service.ITorderService;
import org.apache.tomcat.util.security.PrivilegedGetTccl;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ConsumerReplOrderFanout {

    @Autowired
    private ITorderService torderService;

    /**
     * 死信队列被触发的情况：(最好使用手动ack)
     *  1.消息被拒绝（basic.reject或basic.nack）并且requeue=false.
     *  2.消息TTL过期
     *  3.队列达到最大长度（队列满了，无法再添加数据到mq中）
     * 死信队列被触发的执行流程：
     * 生产者   -->  消息 --> 交换机  --> 队列  --> 变成死信  --> DLX交换机 -->队列 --> 消费者
     *
     */
    //补单消息队列
    @RabbitHandler
    @RabbitListener(queues = FanoutRabbitConfig.REPL_ORDER_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAck(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        PrintUtils.print(this.getClass(), "-->processForManualAck()->>(补单交换机)获取生产者消息：\n messageId:" + messageId + "    订单编号:" + orderNum);
        Long deliveryTag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        PrintUtils.print(this.getClass(), "-->processForManualAck()->>deliveryTag:"+ deliveryTag);
        //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
        //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
        try {
            /**
             * 补单机制：先根据订单编号查询是否存在订单信息，如果没有，则进行补单机制，否则不补单
             */
            Torder torder = torderService.selectByOrderId(orderNum);
            if(torder==null){
                PrintUtils.print(this.getClass(), "-->processForManualAck()->>(补单交换机)订单不存在，进行补单");
                torder=new Torder();
                torder.setOrderNum(orderNum);
                /**
                 * PS:用户下单成功，往订单表中插入数据
                 */
                torderService.insert(torder);
                PrintUtils.print(this.getClass(), "-->processForManualAck()->>(补单交换机)补单成功，补单的订单编号为:"+orderNum);
            }else{
                PrintUtils.print(this.getClass(), "-->processForManualAck()->>(补单交换机)不用补单，订单已存在");
            }
            //int i = 1 / 0;
            // 手动ack
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
        }catch (Exception e){
            e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAck()->>(补单交换机)出现异常，补单失败，丢弃该消息,交给DLX交换机进行补单....");
            //PS:如果出现异常，丢弃该消息，从而消息会交给DLX交换机进行消费
            /**
             * 第一个参数依然是当前消息到的数据的唯一id;
             * 第二个参数是指是否针对多条消息；如果是true，也就是说一次性针对当前通道的消息的tagID小于当前这条消息的，都拒绝确认。
             * 第三个参数是指是否重新入列，也就是指不确认的消息是否重新丢回到队列里面去。
             * 同样使用不确认后重新入列这个确认模式要谨慎，因为这里也可能因为考虑不周出现消息一直被重新丢回去的情况，导致积压。
             * PS:特别注意：第三个参数必须设置为false,这样才会将信息从业务消息队列中移除掉，死信交换机才会进行触发，
             *            从而接收并消费丢弃的消息；如果设置为true，那么消费者就会执行重试机制，一直不停的消费，
             *            知道没有异常发生，才会消费成功，从而将信息从业务消息队列中移除掉。
             */
            channel.basicNack(deliveryTag, true, false);
        }
    }

}
