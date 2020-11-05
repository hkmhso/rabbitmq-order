package com.sunnsoft.rabbitmq.order.producer.fanout;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.rabbitmq.order.commons.utils.GenerateOrderId;
import com.sunnsoft.rabbitmq.order.commons.utils.PrintUtils;
import com.sunnsoft.rabbitmq.order.config.RabbitConfirmAndReturn;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.service.ITorderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 生产者发送消息出去之后，不知道到底有没有发送到RabbitMQ服务器，
 * 默认是不知道的。而且有的时候我们在发送消息之后，后面的逻辑出问题了，我们不想要发送之前的消息了，需要撤回该怎么做。
 *   解决方案:
 *   1.AMQP 事务机制
 *   2.Confirm 模式
 * 事务模式:
 *  txSelect  将当前channel设置为transaction模式
 *  txCommit  提交当前事务
 *  txRollback  事务回滚
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class ProducerPlaceOrderFanout {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ITorderService torderService;

    @Autowired
    private RabbitConfirmAndReturn rabbitConfirmAndReturn;

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 生产者：用户开始下单
     * confirm消息确认机制：
     * 根据交换机名称和路由键精确匹配到对应的消息队列
     * @param exchangeName 交换机名称
     */
    public void placeOrderForConfirm(String exchangeName) throws Exception{
        PrintUtils.print(this.getClass(),"->>sendOrderForConfirm()->>有用户下单了，开始进行派单。。。。。");
        JSONObject jsonObject = new JSONObject();
        Long orderNum=Long.valueOf(GenerateOrderId.getRandomOrderId());
        try {
            jsonObject.put("orderNum", orderNum);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        String msg = jsonObject.toString();
        String messageId=UUID.randomUUID() + "";
        Message message = MessageBuilder.withBody(msg.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(messageId).build();
        // 构建回调返回的数据
        CorrelationData correlationData = new CorrelationData(messageId);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        //设置确认消息是否发送到交换机(Exchange)回调函数
        rabbitTemplate.setConfirmCallback(rabbitConfirmAndReturn);
        //设置确认消息是否发送到队列(Queue)回调函数
        rabbitTemplate.setReturnCallback(rabbitConfirmAndReturn);
        //PS:发送消息,该操作是异步的
        rabbitTemplate.convertAndSend(exchangeName, "", message,correlationData);
        Torder torder=new Torder();
        torder.setOrderNum(orderNum);
        /**
         * PS:用户下单成功，往订单表中插入数据
         */
        torderService.insert(torder);
        /**
         * PS:此时有种情况：消息已经发送出去成功了，但是此时出现异常，事务需要回滚。
         * 所以会导致数据库中不存在该订单记录，但是消费者还是会继续消费该订单记录，
         * 这样肯定是不可以的，所以需要利用一个补单队列继续补单操作。
         */
        int i=1/0;
    }

}
