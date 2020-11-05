package com.sunnsoft.rabbitmq.order.consumer.dlx.direct;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sunnsoft.rabbitmq.order.commons.utils.JsonUtils;
import com.sunnsoft.rabbitmq.order.commons.utils.PrintUtils;
import com.sunnsoft.rabbitmq.order.config.dlx.direct.DirectDLXRabbitConfig;
import com.sunnsoft.rabbitmq.order.data.model.entity.TorderUser;
import com.sunnsoft.rabbitmq.order.data.service.ITorderUserService;
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

@Component
public class ComsumerDLXSendOrderDirect {

    @Autowired
    private ITorderUserService torderUserService;

    /**
     * 派单死信消息队列(张三)
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = DirectDLXRabbitConfig.ZHANGSAN_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckDLXForZhangsan(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        PrintUtils.print(this.getClass(),"->>processForManualAckDLXForZhangsan()->>(张三派单死信交换机)开始进行消费....获取生产者消息：\n messageId:" + messageId + "    订单编号:" + orderNum);
        try {
            // 手动ack
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(1L);
            torderUserService.insert(torderUser);
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
            //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForZhangsan()->>(张三派单死信交换机）派单成功。。。。");
        }catch (Exception e){
            //e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForZhangsan()->>(张三派单死信交换机)出现异常，派单失败,订单"+orderNum+"已被别的外卖小哥抢走。。。。");
        }
    }

    /**
     * 派单死信消息队列(李四)
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = DirectDLXRabbitConfig.LISI_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckDLXForLisi(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        PrintUtils.print(this.getClass(),"->>processForManualAckDLXForLisi()->>(李四派单死信交换机)开始进行消费....获取生产者消息：\n messageId:" + messageId + "    订单编号:" + orderNum);
        try {
            // 手动ack
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(2L);
            torderUserService.insert(torderUser);
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
            //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForLisi()->>(李四派单死信交换机）派单成功。。。。");
        }catch (Exception e){
           // e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForLisi()->>(李四派单死信交换机)出现异常，派单失败,订单"+orderNum+"已被别的外卖小哥抢走。。。");
        }
    }

    /**
     * 派单死信消息队列(王老五)
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = DirectDLXRabbitConfig.WLW_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckDLXForWlw(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        PrintUtils.print(this.getClass(),"->>processForManualAckDLXForWlw()->>(王老五派单死信交换机)开始进行消费....获取生产者消息：\n messageId:" + messageId + "    订单编号:" + orderNum);
        try {
            // 手动ack
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(3L);
            torderUserService.insert(torderUser);
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
            //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForWlw()->>(王老五派单死信交换机）派单成功。。。。");
        }catch (Exception e){
            //e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForWlw()->>(王老五派单死信交换机)出现异常，派单失败,订单"+orderNum+"已被别的外卖小哥抢走。。。");
        }
    }

}
