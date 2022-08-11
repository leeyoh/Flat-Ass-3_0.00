package com.blackrock.flatiron.controller.api;


import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.dto.CreateBookDTO;
import com.blackrock.flatiron.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")

public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public List<BookDTO> getBooks(){
        return bookService.getBooks();
    }

    @PostMapping
    public BookDTO createBook(@RequestBody CreateBookDTO bookDTO){
        return bookService.createBook(bookDTO);
    }

}
