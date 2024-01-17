package com.learnKafka.model;

import lombok.Data;

@Data
public class Book {
    private Long bookId;
    private String bookName;
    private String bookAuthor;
}
