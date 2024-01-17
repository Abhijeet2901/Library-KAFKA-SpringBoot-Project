package com.learnKafka.kafkaProducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.model.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventProducer {
    @Autowired
    KafkaTemplate<Long, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        //ASYNC Approach
        Long key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        //returns value of ListenableFuture type
        ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.sendDefault(key, value); //sendDefault sends message to default topic mentioned in application.yml

        //we add callback, to handle callback/result we receive from consumer
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                //when msg fail to publish
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                //when msg successfully publish
                handleSuccess(key, value, result);
            }
        });
    }

    public void sendLibraryEventAsync_Approach2(LibraryEvent libraryEvent) throws JsonProcessingException {
        //ASYNC Approach
        Long key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        String topic = "library-events";

        //preparing data to be published on topic
        ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, topic);

        //returns value of ListenableFuture type
        //send can be used to send data to multiple topics, with same instance of Kafka Template
        //ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(topic,key, value);

        //sending data by preparing Producer Record
        ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(producerRecord);

        //we add callback, to handle callback/result we receive from consumer
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                //when msg fail to publish
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                //when msg successfully publish
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Long, String> buildProducerRecord(Long key, String value, String topic) {
        //Adding header to or msg, is adding meta data to our msg
        List<Header> headerList=List.of(new RecordHeader("Application-Id","AD12345".getBytes()));

        return new ProducerRecord<Long,String>(topic, null, key, value,headerList);
    }

    public SendResult<Long, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        //SYNC Approach
        Long key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        SendResult<Long, String> sendresult = null;

        try {
            sendresult = kafkaTemplate.sendDefault(key, value).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("InterruptedException/ExecutionException in Sending Message:{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception in Sending Message:{}", e.getMessage());
            throw e;
        }
        return sendresult;
    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error Sending Message:{}", ex.getMessage());

        try {
            throw ex;
            //same as consumer, store failed records in Db, and create a Spring Scheduler to feth data and send again
        } catch (Throwable e) {
            log.error("Error in OnFailure() : {}", e.getMessage());
        }

    }

    public void handleSuccess(Long key, String value, SendResult<Long, String> result) {
        log.info("Messge sent successfully for the key:{}, and the vaue is :{}, to the partition:{}", key, value, result.getRecordMetadata().partition());
    }
}
