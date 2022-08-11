package com.blackrock.flatiron.service;

import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.dto.CreateBookDTO;
import com.blackrock.flatiron.model.Book;
import com.blackrock.flatiron.model.Genre;
import com.blackrock.flatiron.repository.AuthorRepository;
import com.blackrock.flatiron.repository.BookRepository;
import com.blackrock.flatiron.repository.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    private ModelMapper mapper;

    public List<BookDTO> getBooks(){
        return bookRepository.findAll().stream().map(
                book -> {
                    BookDTO bookDTO = mapper.map(book, BookDTO.class);
                    String author = book.getAuthor().getName();
                    bookDTO.setAuthor(author);
                    bookDTO.setGenres(book.getGenreSet().stream().map(g -> g.getName()).toList());
                    return bookDTO;
                }
        ).toList();
    }
    public BookDTO createBook(CreateBookDTO bookDTO){
        Book book = mapper.map(bookDTO, Book.class);
        book.setAuthor(authorRepository.findByName(bookDTO.getAuthor()));
        Set<Genre> genreSet = book.getGenreSet();
        Book finalBook = book;
        //Foreach genre the book belongs to
        bookDTO.getGenre().forEach(genreName -> {
            Genre genre = genreRepository.findByName(genreName);
            if(genre == null){
                //if the genre doesn't exist, create it.
                genre = new Genre();
                genre.setName(genreName);
                genre.getBookSet().add(finalBook);
                genreRepository.save(genre);
            } else {
                //if the genre exits, update it with the new book
                genre.getBookSet().add(finalBook);
            }
            genreSet.add(genre);
        });
        try{
            book = bookRepository.save(book);
        } catch( Exception e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"validation errors",e);
        }
        BookDTO resBookDTO = mapper.map(book, BookDTO.class);
        resBookDTO.setAuthor(book.getAuthor().getName());
        resBookDTO.setGenres(book.getGenreSet().stream().map(val-> val.getName()).toList());
        return resBookDTO;
    }


}
