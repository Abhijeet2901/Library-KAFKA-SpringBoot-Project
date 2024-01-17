package com.learnKafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class LibraryEventsRetryConsumer {
//    @KafkaListener(topics = {"${topics.retry}"},groupId = "retry-listener-group")
//    public void onMessage(ConsumerRecord<Long,String> consumerRecord) throws JsonProcessingException {
//        log.info("Consumer Record in retry consumer: {}",consumerRecord);
//    }
}
