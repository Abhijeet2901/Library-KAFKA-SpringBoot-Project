package com.learnKafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibraryEvent {

    private Long libraryEventId;
    private LibraryEventType type;
    private Book book;
}
