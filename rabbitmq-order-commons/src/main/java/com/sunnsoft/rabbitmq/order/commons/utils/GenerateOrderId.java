package com.sunnsoft.rabbitmq.order.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName GenerateOrderId
 * @Description 随机生成订单号
 * @Author HKM
 * @Date 2020/11/2 10:46
 * @Version 1.0
 **/
public class GenerateOrderId {

    public static String getRandomOrderId() {
        //考虑高并发可以添加userId
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

}
