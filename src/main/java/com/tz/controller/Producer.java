package com.tz.controller;

import cn.hutool.core.util.IdUtil;
import com.tz.conf.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author lhucstart
 * @date 2022-10-31 12:28
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class Producer {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MyCallBack myCallBack;


    // 依赖注入 rabbitTemplate 之后再设置它的回调对象
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
        rabbitTemplate.setReturnsCallback(myCallBack);
    }


    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        // 指定消息 id 为 1
        CorrelationData confirmData = new CorrelationData(IdUtil.simpleUUID());
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, confirmData);
        CorrelationData returnData = new CorrelationData(IdUtil.simpleUUID());
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, returnData);
        log.info(" 发送消息内容:{}", message);
    }
}
