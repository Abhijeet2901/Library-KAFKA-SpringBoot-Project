package com.learnKafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "library-event")
public class LibraryEvent {

    @Transient
    public static final String SEQUENCE_NAME = "library-event_sequence";

    @Id
    private Long libraryEventId;

    private LibraryEventType type;

    @NonNull
    private Book book;
}
