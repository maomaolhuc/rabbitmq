package com.tz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RequestMapping("ttl")
@RestController
public class SendMsgController {


    @Resource
    private RabbitTemplate rabbitTemplate;


    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info("  当 前 时 间 ： {},  发 送 一 条 延 迟 {}  毫 秒 的 信 息 给 队 列 delayed.queue:{}", new
                Date(), delayTime, message);
    }


    @GetMapping("sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info(" 当前时间：{}, 发送一条时长{} 毫秒 TTL  信息给队列 C:{}", new Date(), ttlTime, message);
    }


    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info(" 当前时间：{}, 发送一条信息给两个 TTL  队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", " 消息来自 ttl 为 为 10S  的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", " 消息来自 ttl 为 为 40S  的队列: " + message);
    }


}
