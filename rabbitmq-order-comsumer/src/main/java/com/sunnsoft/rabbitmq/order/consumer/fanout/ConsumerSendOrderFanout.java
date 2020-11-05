package com.sunnsoft.rabbitmq.order.consumer.fanout;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.rabbitmq.client.Channel;
import com.sunnsoft.rabbitmq.order.commons.utils.JsonUtils;
import com.sunnsoft.rabbitmq.order.commons.utils.PrintUtils;
import com.sunnsoft.rabbitmq.order.config.fanout.FanoutRabbitConfig;
import com.sunnsoft.rabbitmq.order.data.model.entity.TorderUser;
import com.sunnsoft.rabbitmq.order.data.service.ITorderUserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

/**
 * 死信队列被触发的情况：(最好使用手动ack)
 *  1.消息被拒绝（basic.reject或basic.nack）并且requeue=false.
 *  2.消息TTL过期
 *  3.队列达到最大长度（队列满了，无法再添加数据到mq中）
 * 死信队列被触发的执行流程：
 * 生产者   -->  消息 --> 交换机  --> 队列  --> 变成死信  --> DLX交换机 -->队列 --> 消费者
 *
 */
@Component
public class ConsumerSendOrderFanout {

    @Autowired
    private ITorderUserService torderUserService;

    /**
     * 死信队列被触发的情况：(最好使用手动ack)
     *  1.消息被拒绝（basic.reject或basic.nack）并且requeue=false.
     *  2.消息TTL过期
     *  3.队列达到最大长度（队列满了，无法再添加数据到mq中）
     * 死信队列被触发的执行流程：
     * 生产者   -->  消息 --> 交换机  --> 队列  --> 变成死信  --> DLX交换机 -->队列 --> 消费者
     *
     */
    //派单消息队列：外卖小哥张三
    @RabbitHandler
    @RabbitListener(queues = FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_ZHANGSAN,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckZhangsan(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        Long deliveryTag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        PrintUtils.print(this.getClass(), "-->processForManualAckZhangsan()->>(张三派单交换机)deliveryTag:"+ deliveryTag+"\nmessageId:" + messageId);
        //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
        //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
        try {
            /**
             * PS:外卖小哥接到单后，直接插入一条数据
             * 如果插入失败，那么说明已经被别的外卖小哥接了，那么直接放弃消费。
             * （因为数据库中的订单编号设置为唯一约束，从而保证一个订单只被一个外卖小哥接收）
             *
             */
            //int i = 1 / 0;
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(1L);
            torderUserService.insert(torderUser);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckZhangsan()->>外卖小哥张三接单成功，订单编号"+orderNum);
        }catch (DuplicateKeyException e){
            //e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckZhangsan()->>(张三派单交换机)外卖小哥张三接单失败,丢弃该消息,订单"+orderNum+"已被别的外卖小哥抢走");
        }catch (Exception e){
            //e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckZhangsan()->>(张三派单交换机)出现异常，丢弃该消息,交给DLX交换机进行消费...");
        }
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

    /**
     * 死信队列被触发的情况：(最好使用手动ack)
     *  1.消息被拒绝（basic.reject或basic.nack）并且requeue=false.
     *  2.消息TTL过期
     *  3.队列达到最大长度（队列满了，无法再添加数据到mq中）
     * 死信队列被触发的执行流程：
     * 生产者   -->  消息 --> 交换机  --> 队列  --> 变成死信  --> DLX交换机 -->队列 --> 消费者
     *
     */
    //派单消息队列：外卖小哥李四
    @RabbitHandler
    @RabbitListener(queues = FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_LISI,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckLisi(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        Long deliveryTag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        PrintUtils.print(this.getClass(), "-->processForManualAckLisi()->>(李四派单交换机)deliveryTag:"+ deliveryTag+"\nmessageId:" + messageId);
        //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
        //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
        try {
            /**
             * PS:外卖小哥接到单后，直接插入一条数据
             * 如果插入失败，那么说明已经被别的外卖小哥接了，那么直接放弃消费。
             * （因为数据库中的订单编号设置为唯一约束，从而保证一个订单只被一个外卖小哥接收）
             *
             */
            //int i = 1 / 0;
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(2L);
            torderUserService.insert(torderUser);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckLisi()->>外卖小哥李四接单成功，订单"+orderNum);
        }catch (DuplicateKeyException e){
           // e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckLisi()->>(李四派单交换机)外卖小哥李四接单失败，丢弃该消息,订单"+orderNum+"已被别的外卖小哥抢走");
        }catch (Exception e){
           // e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckLisi()->>(李四派单交换机)出现异常，丢弃该消息,交给DLX交换机进行消费...");
        }
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

    /**
     * 死信队列被触发的情况：(最好使用手动ack)
     *  1.消息被拒绝（basic.reject或basic.nack）并且requeue=false.
     *  2.消息TTL过期
     *  3.队列达到最大长度（队列满了，无法再添加数据到mq中）
     * 死信队列被触发的执行流程：
     * 生产者   -->  消息 --> 交换机  --> 队列  --> 变成死信  --> DLX交换机 -->队列 --> 消费者
     *
     */
    //派单消息队列：外卖小哥王老五
    @RabbitHandler
    @RabbitListener(queues = FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_WLW,containerFactory = "rabbitListenerContainerFactory")
    public void processForManualAckWlw(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取全局MessageID
        String messageId = message.getMessageProperties().getMessageId();
        //获取投递的消息
        String msg = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JsonUtils.jsonToPojo(msg, JSONObject.class);
        Long orderNum = Long.valueOf(jsonObject.getString("orderNum"));
        Long deliveryTag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        PrintUtils.print(this.getClass(), "-->processForManualAckWlw()->>(王老五派单交换机)deliveryTag:"+ deliveryTag+"\nmessageId:" + messageId);
        //Long deliveryTag2=message.getMessageProperties().getDeliveryTag();
        //PrintUtils.print(this.getClass(), "-->processForManualAckForDLX()->>deliveryTag2:"+ deliveryTag2);
        try {
            /**
             * PS:外卖小哥接到单后，直接插入一条数据
             * 如果插入失败，那么说明已经被别的外卖小哥接了，那么直接放弃消费。
             * （因为数据库中的订单编号设置为唯一约束，从而保证一个订单只被一个外卖小哥接收）
             *
             */
            //int i = 1 / 0;
            TorderUser torderUser=new TorderUser();
            torderUser.setOrderNum(orderNum);
            torderUser.setUserId(3L);
            torderUserService.insert(torderUser);
            // 手动签收,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            PrintUtils.print(this.getClass(),"->>processForManualAckWlw()->>外卖小哥王老五接单成功，订单"+orderNum);
        }catch (DuplicateKeyException e){
           // e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckWlw()->>(王老五派单交换机)外卖小哥王老五接单失败，丢弃该消息，订单"+orderNum+"已被别的外卖小哥抢走");
        }catch (Exception e){
            //e.printStackTrace();
            PrintUtils.print(this.getClass(),"->>processForManualAckWlw()->>(王老五派单交换机)出现异常，丢弃该消息,交给DLX交换机进行消费...");
        }
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
