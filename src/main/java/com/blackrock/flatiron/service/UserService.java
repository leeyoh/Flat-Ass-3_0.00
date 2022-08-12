package com.blackrock.flatiron.service;
import com.blackrock.flatiron.dto.*;
import com.blackrock.flatiron.model.Author;
import com.blackrock.flatiron.model.Book;
import com.blackrock.flatiron.model.ReadingList;
import com.blackrock.flatiron.model.User;
import com.blackrock.flatiron.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class UserService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReadingListRepository readingListRepository;
    @Autowired
    private ModelMapper mapper;
    public UserDTO createUser(CreateUserDTO userDTO){
        User user =  userRepository.findByUsername(userDTO.getUsername());
        if(user != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username already exists");
        }
        user = mapper.map(userDTO, User.class);
        userRepository.save(user);
        return mapper.map(user,UserDTO.class);
    }
    public List<BookListDTO> createReadingList(Long userId, CreateReadingListDTO readingListDTO){
        //Searches for the user, if they don't exist return
        User user =  userRepository.findByUsername(readingListDTO.getUsername());
        if(user == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username doesn't exist");
        }

        //Condition for if the reading list already exists.
        ReadingList readingList = readingListRepository.findByNameAndUser(readingListDTO.getReading_list_name(),user);
        if(readingList == null){
            readingList = new ReadingList();
            readingList.setName(readingListDTO.getReading_list_name());
        } 

        //checks to see if the books exist in the collection.
        //if they don't exist in the collection, add it to the return list.
        List<BookListDTO> missingBooksDTO = new ArrayList<BookListDTO>();

        ReadingList finalReadingList = readingList;
        readingListDTO.getReading_list().forEach(booklistDTO -> {

            //If the Book already "exits" throw an error
            Book book = bookRepository.findByTitleAndPagesAndAuthor(
                    booklistDTO.getTitle(),
                    booklistDTO.getPages(),
                    authorRepository.findByName(booklistDTO.getAuthor()));
            if(book == null){
                log.trace("Book Missing: " + booklistDTO.getTitle() + " Pages: "+ booklistDTO.getPages() + " Author: " + booklistDTO.getAuthor());
                missingBooksDTO.add(booklistDTO);
            } else{
                log.trace("Book Found: " + booklistDTO.getTitle() + " Pages: "+ booklistDTO.getPages() + " Author: " + booklistDTO.getAuthor());
                finalReadingList.getBookSet().add(book);
            }
        });
        readingList.setBookSet(finalReadingList.getBookSet());
        readingList.setUser(user);
        readingListRepository.save(readingList);
        return missingBooksDTO;
    }
}
