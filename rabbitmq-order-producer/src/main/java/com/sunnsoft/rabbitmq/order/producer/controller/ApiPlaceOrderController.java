package com.sunnsoft.rabbitmq.order.producer.controller;

import com.sunnsoft.rabbitmq.order.config.fanout.FanoutRabbitConfig;
import com.sunnsoft.rabbitmq.order.producer.fanout.ProducerPlaceOrderFanout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟用户下单，然后外卖小哥接单的场景
 * @author yzc
 * @date 2020/6/28 14:58
 */
@RestController
public class ApiPlaceOrderController {

    @Autowired
    private ProducerPlaceOrderFanout producerPlaceOrderFanout;

    /**
     * 模拟用户下单，然后外卖小哥接单的场景
     * @return
     */
    @GetMapping("/api/placeOrder")
    public String placeOrder() {
        try {
            producerPlaceOrderFanout.placeOrderForConfirm(FanoutRabbitConfig.EXCHANGE_NAME);
            return "用户下单成功，且插入订单成功";
        } catch (Exception e) {
            //e.printStackTrace();
            return "用户下单成功，但插入订单失败，需要补单";
        }
    }
}