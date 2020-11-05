package com.sunnsoft.rabbitmq.order.producer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * PS:scanBasePackages：表示既会扫描该模块的包，也会扫描该模块所依赖的其他模块的包
 */
@SpringBootApplication(scanBasePackages = "com.sunnsoft.rabbitmq.order.*")
@MapperScan(basePackages={"com.sunnsoft.rabbitmq.order.data.mapper"})
@EnableRabbit
//开启定时调度任务的功能
@EnableScheduling
public class RabbitMQOrderProducer {
    public static void main(String[] args) {
        try {
            SpringApplication.run(RabbitMQOrderProducer.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
