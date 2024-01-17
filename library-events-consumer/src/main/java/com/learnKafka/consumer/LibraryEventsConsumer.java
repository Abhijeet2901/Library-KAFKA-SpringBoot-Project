package com.learnKafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnKafka.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryEventsConsumer {

    @Autowired
    LibraryEventService libraryEventService;

    //KafkaAutoConfiguration -> KafkaAnnotationDrivenConfiguration = kafkaListenerContainerFactory(responsible for @kafkalistener consumer properties)
    //by Concurrency, we can create multiple instances of consumer/KafkaMessageListenerContainer in single application
//    ,concurrency = "3" -> setting concurrency in config -> bean creation
    @KafkaListener(topics = {"library-events"},groupId = "library-events-listener-group",concurrency = "3")
    public void onMessage(ConsumerRecord<Long,String> consumerRecord) throws JsonProcessingException {
        libraryEventService.processLibraryEvent(consumerRecord);
        log.info("Consumer Record: {}",consumerRecord);
    }
}
