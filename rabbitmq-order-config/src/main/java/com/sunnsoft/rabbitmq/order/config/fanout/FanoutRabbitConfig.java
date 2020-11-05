package com.sunnsoft.rabbitmq.order.config.fanout;

import com.sunnsoft.rabbitmq.order.config.dlx.direct.DirectDLXRabbitConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : hkm
 * @CreateTime : 2020/10/22
 * @Description :fanout交换机
 **/
@Configuration
public class FanoutRabbitConfig {

    //派单消息队列(张三)
    public static final String SEND_ORDER_QUEUE_NAME_ZHANGSAN = "fanout_queue_send_order_zhangsan";
    //派单消息队列(李四)
    public static final String SEND_ORDER_QUEUE_NAME_LISI = "fanout_queue_send_order_lisi";
    //派单消息队列(王老五)
    public static final String SEND_ORDER_QUEUE_NAME_WLW = "fanout_queue_send_order_wlw";
    //补单消息队列
    public static final String REPL_ORDER_QUEUE_NAME = "fanout_queue_repl_order";
    public static final String EXCHANGE_NAME = "fanout_order_exchange";
    /**
     * 业务消息队列和死信交换机绑定的标识符
     */
    public static final String DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";
    /**
     * 业务消息队列和死信交换机的绑定键的标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 定义优先级队列
     */
    public static final String DEAD_LETTER_MAX_PRIORITY = "x-max-prioritye";
    /**
     * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
     */
    public static final String DEAD_LETTER_MESSAGE_TTL = "x-message-ttl";


    //派单消息队列(张三)
    @Bean
    public Queue fanoutQueueSendOrderForZhangsan() {

        Map<String, Object> map = new HashMap<>();
        /**
         * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
         */
        //map.put(FanoutRabbitConfig.DEAD_LETTER_MESSAGE_TTL, DirectDLXRabbitConfig.DEAD_LETTER_MESSAGE_TTL);
        /**
         * key:业务消息队列和死信交换机绑定的标识符(DLX)  value:死信交换机的名称
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_EXCHANGE_KEY, DirectDLXRabbitConfig.DIRECT_ORDER_DLX_EXCHANGE_NAME);
        /**
         * key:业务消息队列和死信交换机的绑定键的标识符(DLK) value:业务消息队列和死信交换机的绑定键
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_ROUTING_KEY, DirectDLXRabbitConfig.ZHANGSAN_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
        /**
         * 定义优先级队列，消息最大优先级为15，优先级范围为0-15，数字越大优先级越高
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_MAX_PRIORITY, DirectDLXRabbitConfig.DEAD_LETTER_MAX_PRIORITY);
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_ZHANGSAN,true,false, false,map);
        //return QueueBuilder.durable(FanoutRabbitConfig.SMS_QUEUE_NAME).withArguments(map).build();
    }

    //派单消息队列(李四)
    @Bean
    public Queue fanoutQueueSendOrderForLisi() {

        Map<String, Object> map = new HashMap<>();
        /**
         * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
         */
        //map.put(FanoutRabbitConfig.DEAD_LETTER_MESSAGE_TTL, DirectDLXRabbitConfig.DEAD_LETTER_MESSAGE_TTL);
        /**
         * key:业务消息队列和死信交换机绑定的标识符(DLX)  value:死信交换机的名称
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_EXCHANGE_KEY, DirectDLXRabbitConfig.DIRECT_ORDER_DLX_EXCHANGE_NAME);
        /**
         * key:业务消息队列和死信交换机的绑定键的标识符(DLK) value:业务消息队列和死信交换机的绑定键
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_ROUTING_KEY, DirectDLXRabbitConfig.LISI_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
        /**
         * 定义优先级队列，消息最大优先级为15，优先级范围为0-15，数字越大优先级越高
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_MAX_PRIORITY, DirectDLXRabbitConfig.DEAD_LETTER_MAX_PRIORITY);
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_LISI,true,false, false,map);
        //return QueueBuilder.durable(FanoutRabbitConfig.SMS_QUEUE_NAME).withArguments(map).build();
    }

    //派单消息队列(王老五)
    @Bean
    public Queue fanoutQueueSendOrderForWlw() {

        Map<String, Object> map = new HashMap<>();
        /**
         * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
         */
        //map.put(FanoutRabbitConfig.DEAD_LETTER_MESSAGE_TTL, DirectDLXRabbitConfig.DEAD_LETTER_MESSAGE_TTL);
        /**
         * key:业务消息队列和死信交换机绑定的标识符(DLX)  value:死信交换机的名称
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_EXCHANGE_KEY, DirectDLXRabbitConfig.DIRECT_ORDER_DLX_EXCHANGE_NAME);
        /**
         * key:业务消息队列和死信交换机的绑定键的标识符(DLK) value:业务消息队列和死信交换机的绑定键
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_ROUTING_KEY, DirectDLXRabbitConfig.WLW_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
        /**
         * 定义优先级队列，消息最大优先级为15，优先级范围为0-15，数字越大优先级越高
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_MAX_PRIORITY, DirectDLXRabbitConfig.DEAD_LETTER_MAX_PRIORITY);
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(FanoutRabbitConfig.SEND_ORDER_QUEUE_NAME_WLW,true,false, false,map);
        //return QueueBuilder.durable(FanoutRabbitConfig.SMS_QUEUE_NAME).withArguments(map).build();
    }

    //补单消息队列
    @Bean
    public Queue fanoutQueueReplOrder() {
        Map<String, Object> map = new HashMap<>();
        /**
         * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
         */
        //map.put(FanoutRabbitConfig.DEAD_LETTER_MESSAGE_TTL, DirectDLXRabbitConfig.DEAD_LETTER_MESSAGE_TTL);
        /**
         * key:业务消息队列和死信交换机绑定的标识符(DLX)  value:死信交换机的名称
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_EXCHANGE_KEY, DirectDLXRabbitConfig.DIRECT_ORDER_DLX_EXCHANGE_NAME);
        /**
         * key:业务消息队列和死信交换机的绑定键的标识符(DLK) value:业务消息队列和死信交换机的绑定键
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_ROUTING_KEY, DirectDLXRabbitConfig.DIRECT_REPL_ORDER_DLX_ROUTING_KEY);
        /**
         * 定义优先级队列，消息最大优先级为15，优先级范围为0-15，数字越大优先级越高
         */
        map.put(FanoutRabbitConfig.DEAD_LETTER_MAX_PRIORITY, DirectDLXRabbitConfig.DEAD_LETTER_MAX_PRIORITY);
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(FanoutRabbitConfig.REPL_ORDER_QUEUE_NAME,true,false, false,map);
        //return QueueBuilder.durable(FanoutRabbitConfig.EMAIL_QUEUE_NAME).withArguments(map).build();
    }

    //fanout交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        //  return new fanoutExchange("TestfanoutExchange",true,true);
        return new FanoutExchange(FanoutRabbitConfig.EXCHANGE_NAME,true,false);
    }

    //绑定  将派单消息队列(张三)和交换机绑定, 不用设置绑定键，即使设置了也没用
    @Bean
    public Binding bindingFanoutSendOrderForZhangsan() {
        return BindingBuilder.bind(fanoutQueueSendOrderForZhangsan()).to(fanoutExchange());
    }

    //绑定  将派单消息队列（李四）和交换机绑定, 不用设置绑定键，即使设置了也没用
    @Bean
    public Binding bindingFanoutSendOrderForLisi() {
        return BindingBuilder.bind(fanoutQueueSendOrderForLisi()).to(fanoutExchange());
    }

    //绑定  将派单消息队列（王老五）和交换机绑定, 不用设置绑定键，即使设置了也没用
    @Bean
    public Binding bindingFanoutSendOrderForWlw() {
        return BindingBuilder.bind(fanoutQueueSendOrderForWlw()).to(fanoutExchange());
    }

    //绑定  将补单消息队列和交换机绑定, 不用设置绑定键，即使设置了也没用
    @Bean
    public Binding bindingFanoutReplOrder() {
        return BindingBuilder.bind(fanoutQueueReplOrder()).to(fanoutExchange());
    }

}