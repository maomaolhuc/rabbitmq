package com.tz.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lhucstart
 * @date 2022-10-31 11:26
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info(" 交换机已经收到 id  为:{} 的消息", id);
        } else {
            log.info(" 交换机还未收到 id  为:{} 消息, 由于原因:{}", id, cause);
        }
    }


    /**
     * Returned message callback.
     *
     * @param returned the returned message and metadata.
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        Message message = returned.getMessage();
        String exchange = returned.getExchange();
        String replyText = returned.getReplyText();
        String routingKey = returned.getRoutingKey();
        log.error(" 消 息 {},  被 交 换 机 {} 退 回 ， 退 回 原 因 :{},  路 由 key:{}", new
                String(message.getBody()), exchange, replyText, routingKey);
    }
}
