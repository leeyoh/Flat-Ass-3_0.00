package com.blackrock.flatiron.service;

import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.dto.CreateBookDTO;
import com.blackrock.flatiron.model.Author;
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

    /**
     * Remove a book from the table
     * - if the book doesn't exist return a missing status
     * @param id
     */
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Book not Found"));
        book.getGenreSet().forEach(genre -> {
            genre.getBookSet().remove(book);
        });
        book.getReadingSet().forEach(readingList -> {
            readingList.getBookSet().remove(book);
        });
        bookRepository.deleteById(id);
    }
    public BookDTO updateBook(Long id, CreateBookDTO createBookDTO){
        //If the book doesn't exit throw and error
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Book not Found"));

        //look for the new author in the table, if they don't exist create it
        Author author = authorRepository.findByName(createBookDTO.getAuthor());
        if(author == null){
            author = new Author();
            author.setName(createBookDTO.getAuthor());
            authorRepository.save(author);
        }
        book.setAuthor(author);

        //Update the genres, remove the relationship in the join table if requried.
        Set<Genre> genreSet = book.getGenreSet();
        Book finalBook = book;

        genreSet.forEach(genre -> {
            if(!createBookDTO.getGenre().contains(genre.getName())){
                genre.getBookSet().remove(finalBook);
            }
        });

        createBookDTO.getGenre().forEach(genreName -> {
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
        BookDTO resBookDTO = mapper.map(createBookDTO, BookDTO.class);
        resBookDTO.setGenres(createBookDTO.getGenre());
        return resBookDTO;
    }

    public BookDTO getBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Book not Found"));
        BookDTO bookDTO = mapper.map(book, BookDTO.class);
        String author = book.getAuthor().getName();
        bookDTO.setAuthor(author);
        bookDTO.setGenres(book.getGenreSet().stream().map(g -> g.getName()).toList());
        return bookDTO;
    }
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
        //If the Book already "exits" throw an error
        if(bookRepository.findByTitleAndPagesAndAuthor(
                bookDTO.getTitle(),
                bookDTO.getPages(),
                authorRepository.findByName(bookDTO.getAuthor())
        ) != null){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Book Already Exists");
        }

        //Create new book
        Book book = mapper.map(bookDTO, Book.class);

        //set the author of the book
        Author author = authorRepository.findByName(bookDTO.getAuthor());
        if(author == null){
            author = new Author();
            author.setName(bookDTO.getAuthor());
            authorRepository.save(author);
        }
        book.setAuthor(author);

        //Settings Genres
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"validation errors",e);
        }
        BookDTO resBookDTO = mapper.map(book, BookDTO.class);
        resBookDTO.setAuthor(book.getAuthor().getName());
        resBookDTO.setGenres(book.getGenreSet().stream().map(val-> val.getName()).toList());
        return resBookDTO;
    }


}
