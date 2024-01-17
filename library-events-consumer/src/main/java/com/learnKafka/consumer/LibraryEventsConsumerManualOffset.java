package com.learnKafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class LibraryEventsConsumerManualOffset implements AcknowledgingMessageListener<Long,String> {

    //KafkaAutoConfiguration -> KafkaAnnotationDrivenConfiguration = kafkaListenerContainerFactory(responsible for @kafkalistener consumer properties)

    @Override
    @KafkaListener(topics = {"library-events"})
    public void onMessage(ConsumerRecord<Long, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Consumer Record: {}",consumerRecord);
        //in order to commit and update _consumer_offsets acknowledge has to be done, else
        //next timewhen retrieving data fom consumer, it will be having last data for offset and read that value as well
        //acknowledgment.acknowledge();
    }
}
