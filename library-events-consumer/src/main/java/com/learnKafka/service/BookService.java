package com.learnKafka.service;

import com.learnKafka.model.Book;
import com.learnKafka.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Book save (Book book){
        if(ObjectUtils.isEmpty(book.getBookId())) {
            book.setBookId(sequenceGeneratorService.getNextSequenceId("book_sequence"));
            return bookRepository.save(book);
        } else{
            return bookRepository.save(book);
        }
    }

    public Book find(Long id){
        return bookRepository.findById(id).get();
    }
}
