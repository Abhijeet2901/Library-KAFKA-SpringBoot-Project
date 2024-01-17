package com.learnKafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.model.Book;
import com.learnKafka.model.LibraryEvent;
import com.learnKafka.model.LibraryEventType;
import com.learnKafka.repository.LibraryEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class LibraryEventService {

    @Autowired
    LibraryEventRepository libraryEventRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    public void processLibraryEvent(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);

        switch (libraryEvent.getType()) {
            case NEW:
                try {
                    if (!ObjectUtils.isEmpty(libraryEvent.getBook())) {
                        Book dbBook = bookService.find(libraryEvent.getBook().getBookId());
                        if (ObjectUtils.isEmpty(dbBook)) {
                            Book book = bookService.save(libraryEvent.getBook());
                            if (!ObjectUtils.isEmpty(book)) {
                                libraryEvent.setBook(book);
                                this.save(libraryEvent);
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Exception found:", e.getMessage());
                }
                break;

            case UPDATE:
                try {
                    if (!ObjectUtils.isEmpty(libraryEvent) && !ObjectUtils.isEmpty(libraryEvent.getBook())) {
                        LibraryEvent dbLibraryEvent = libraryEventRepository.findById(libraryEvent.getLibraryEventId()).get();
                        Book dbBook = bookService.find(libraryEvent.getBook().getBookId());
                        if (!ObjectUtils.isEmpty(dbLibraryEvent) && !ObjectUtils.isEmpty(dbBook)) {
                            Book book = bookService.save(libraryEvent.getBook());
                            if (!ObjectUtils.isEmpty(book)) {
                                libraryEvent.setBook(book);
                                this.update(libraryEvent);
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Exception found:", e.getMessage());
                }
                break;

            default:
                log.info("Event Type does not match!!");
                break;
        }
    }

    public LibraryEvent save(LibraryEvent libraryEvent) {
        if (libraryEvent.getType().equals(LibraryEventType.NEW)) {
            LibraryEvent event = new LibraryEvent();
            event.setLibraryEventId(sequenceGeneratorService.getNextSequenceId("library-event_sequence"));
            event.setBook(libraryEvent.getBook());
            return libraryEventRepository.save(event);
        }
        return null;
    }

    public LibraryEvent update(LibraryEvent libraryEvent) {
        if (libraryEvent.getType().equals(LibraryEventType.UPDATE)) {
            LibraryEvent event = new LibraryEvent();
            event.setLibraryEventId(libraryEvent.getLibraryEventId());
            event.setBook(libraryEvent.getBook());
            return libraryEventRepository.save(event);
        }
        return null;
    }
}
