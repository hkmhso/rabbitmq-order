package com.sunnsoft.rabbitmq.order.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PS:scanBasePackages：表示既会扫描该模块的包，也会扫描该模块所依赖的其他模块的包
 */
@SpringBootApplication(scanBasePackages="com.sunnsoft.rabbitmq.order.*")
@MapperScan(basePackages={"com.sunnsoft.rabbitmq.order.data.mapper"})
@EnableRabbit
public class RabbitMQOrderConsumer {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQOrderConsumer.class, args);
    }
}
