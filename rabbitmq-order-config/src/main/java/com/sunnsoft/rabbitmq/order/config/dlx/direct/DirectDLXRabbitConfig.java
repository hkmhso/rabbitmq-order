package com.sunnsoft.rabbitmq.order.config.dlx.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : hkm
 * @CreateTime : 2020/10/27
 * @Description :死信交换机
 **/
@Configuration
public class DirectDLXRabbitConfig {

    //派单死信消息队列（张三）
    public static final String ZHANGSAN_DIRECT_SEND_ORDER_DLX_QUEUE_NAME = "zhangsan_direct_send_order_queue_dlx";
    public static final String ZHANGSAN_DIRECT_SEND_ORDER_DLX_ROUTING_KEY = "zhangsan_direct_send_order_routing_key_dlx";
    //派单死信消息队列（李四）
    public static final String LISI_DIRECT_SEND_ORDER_DLX_QUEUE_NAME = "lisi_direct_send_order_queue_dlx";
    public static final String LISI_DIRECT_SEND_ORDER_DLX_ROUTING_KEY = "lisi_direct_send_order_routing_key_dlx";
    //派单死信消息队列（王老五）
    public static final String WLW_DIRECT_SEND_ORDER_DLX_QUEUE_NAME = "wlw_direct_send_order_queue_dlx";
    public static final String WLW_DIRECT_SEND_ORDER_DLX_ROUTING_KEY = "wlw_direct_send_order_routing_key_dlx";
    //补单死信消息队列
    public static final String DIRECT_REPL_ORDER_DLX_QUEUE_NAME = "direct_repl_order_queue_dlx";
    public static final String DIRECT_REPL_ORDER_DLX_ROUTING_KEY = "direct_repl_order_routing_key_dlx";
    //死信交换机
    public static final String DIRECT_ORDER_DLX_EXCHANGE_NAME = "direct_order_dlx_exchange";
    /**
     * 定义优先级队列
     */
    public static final Long DEAD_LETTER_MAX_PRIORITY = 15L;
    /**
     * 设置消息发送到消息队列之后多久被丢弃，单位：毫秒
     */
    public static final Long DEAD_LETTER_MESSAGE_TTL = 10000L;

    //派单死信消息队列(张三)
    @Bean
    public Queue directQueueSendOrderDLXForZhangsan() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(DirectDLXRabbitConfig.ZHANGSAN_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,true,false, false);
        //return QueueBuilder.durable(DLXRabbitConfig.DIRECT_EMAIL_DLX_QUEUE_NAME).build();

    }

    //派单死信消息队列(李四)
    @Bean
    public Queue directQueueSendOrderDLXForLisi() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(DirectDLXRabbitConfig.LISI_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,true,false, false);
        //return QueueBuilder.durable(DLXRabbitConfig.DIRECT_EMAIL_DLX_QUEUE_NAME).build();

    }

    //派单死信消息队列(王老五)
    @Bean
    public Queue directQueueSendOrderDLXForWlw() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(DirectDLXRabbitConfig.WLW_DIRECT_SEND_ORDER_DLX_QUEUE_NAME,true,false, false);
        //return QueueBuilder.durable(DLXRabbitConfig.DIRECT_EMAIL_DLX_QUEUE_NAME).build();

    }

    //补单死信消息队列
    @Bean
    public Queue directQueueReplOrderDLX() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，true:只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，默认也是false,这样会消息导致挤压，true:当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new Queue(DirectDLXRabbitConfig.DIRECT_REPL_ORDER_DLX_QUEUE_NAME,true,false, false);
        //return QueueBuilder.durable(DLXRabbitConfig.DIRECT_SMS_DLX_QUEUE_NAME).build();

    }

    //死信交换机
    @Bean
    public DirectExchange directExchangeDLX() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange(DirectDLXRabbitConfig.DIRECT_ORDER_DLX_EXCHANGE_NAME,true,false);
    }

    //绑定  将派单死信消息队列（张三）和死信交换机绑定, 并设置绑定键
    @Bean
    public Binding bindingFanoutDLXSendOrderForZhangsan() {
        return BindingBuilder.bind(directQueueSendOrderDLXForZhangsan()).to(directExchangeDLX()).with(DirectDLXRabbitConfig.ZHANGSAN_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
    }

    //绑定  将派单死信消息队列（李四）和死信交换机绑定, 并设置绑定键
    @Bean
    public Binding bindingFanoutDLXSendOrderForLisi() {
        return BindingBuilder.bind(directQueueSendOrderDLXForLisi()).to(directExchangeDLX()).with(DirectDLXRabbitConfig.LISI_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
    }

    //绑定  将派单死信消息队列（王老五）和死信交换机绑定, 并设置绑定键
    @Bean
    public Binding bindingFanoutDLXSendOrderForWlw() {
        return BindingBuilder.bind(directQueueSendOrderDLXForWlw()).to(directExchangeDLX()).with(DirectDLXRabbitConfig.WLW_DIRECT_SEND_ORDER_DLX_ROUTING_KEY);
    }

    //绑定  将补单死信消息队列和死信交换机绑定, 并设置绑定键
    @Bean
    public Binding bindingFanoutDLXReplOrder() {
        return BindingBuilder.bind(directQueueReplOrderDLX()).to(directExchangeDLX()).with(DirectDLXRabbitConfig.DIRECT_REPL_ORDER_DLX_ROUTING_KEY);
    }


}
