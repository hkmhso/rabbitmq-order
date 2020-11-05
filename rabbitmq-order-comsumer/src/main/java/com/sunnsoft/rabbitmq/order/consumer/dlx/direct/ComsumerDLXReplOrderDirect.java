package com.sunnsoft.rabbitmq.order.consumer.dlx.direct;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sunnsoft.rabbitmq.order.commons.utils.JsonUtils;
import com.sunnsoft.rabbitmq.order.commons.utils.PrintUtils;
import com.sunnsoft.rabbitmq.order.config.dlx.direct.DirectDLXRabbitConfig;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.service.ITorderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ComsumerDLXReplOrderDirect {

    @Autowired
    private ITorderService torderService;

    /**
     * 补单死信消息队列
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = DirectDLXRabbitConfig.DIRECT_REPL_ORDER_DLX_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckDLXForReplOrder(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        try {
            // 获取全局MessageID
            String messageId = message.getMessageProperties().getMessageId();
            //获取投递的消息
            String msg = new String(message.getBody(), "UTF-8");
            JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
            Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
            Torder torder=new Torder();
            torder.setOrderNum(orderNum);
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForReplOrder()->>(补单死信交换机)开始进行补单....获取生产者消息：\n messageId:" + messageId + "    补单订单编号:" + orderNum);
            torderService.insert(torder);
            // 手动ack
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
            //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForReplOrder()->>(补单死信交换机)补单成功。。。。");
            channel.basicAck(deliveryTag, true);
        }catch (Exception e){
            e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckDLXForReplOrder()->>(补单死信交换机)出现异常，补单失败。。。。");
        }
    }

}
