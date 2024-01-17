package com.learnKafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnKafka.kafkaProducer.LibraryEventProducer;
import com.learnKafka.model.LibraryEvent;
import com.learnKafka.model.LibraryEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/save")
    public ResponseEntity saveLibraryEvents(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        //Implement KAFKA Producer
        if (ObjectUtils.isEmpty(libraryEvent.getLibraryEventId())) {

            //let know user o consumer side, its a new data, insert in DB
            libraryEvent.setType(LibraryEventType.NEW);

            //sendLibraryEvent method is asynchronous in nature, as it depends on ListenableFuture in Kafka Template
            //libraryEventProducer.sendLibraryEvent(libraryEvent);

            //Sync Approach
            //libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);

            //Async Approach-2
            libraryEventProducer.sendLibraryEventAsync_Approach2(libraryEvent);
            return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Library Event Id/Book Id not found !!");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLibraryEvents(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        //for ordering of events in partition, always pass the key in msg
        if (!ObjectUtils.isEmpty(libraryEvent.getLibraryEventId())) {
            libraryEvent.setType(LibraryEventType.UPDATE);
            libraryEventProducer.sendLibraryEventAsync_Approach2(libraryEvent);
            return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Provide LibraryEventID/BookId !!");
    }
}
