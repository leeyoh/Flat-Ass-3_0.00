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

import javax.persistence.EntityManager;
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
    /**
     *
     * @param userId
     * @return
     */
    public List<ReadingListDTO> deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"user doesn't exist"));
        List<ReadingList> readingLists = readingListRepository.findByUser(user);
        List<ReadingListDTO> resDTO = readingLists.stream().map(r -> mapper.map(r, ReadingListDTO.class)).toList();
        for(ReadingList rl : readingLists){
            rl.getBookSet().forEach(book -> {
                book.getReadingSet().remove(rl);
            });
            readingListRepository.delete(rl);
        }
        userRepository.delete(user);
        log.trace(user.toString());
        return resDTO;
    }
    /**
     * Given a user ID and Reading list id, return all the books in that list.
     * - If the user doesn't exist, throw a user not found status.
     * - If the list id doesn't exist, throw a list not found status
     * @param userId
     * @return List<ReadingListDTO>
     * example:
     * [
     *      {
     *          title: "",
     *          page: "",
     *          author: ""
     *      },
     *      {
     *          title: "",
     *          page: "",
     *          author: ""
     *      }
     */
    public List<BookListDTO> getReadingList(Long userId, Long readingListId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Doesn't Exist"));
        ReadingList readingList = readingListRepository.findById(readingListId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Reading List Doesn't Exist"));
        //Check to see if the reading list belongs to the user
        if(!readingList.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Reading List Doesn't Belong to User");
        }
        log.trace(user.toString());
        log.trace(readingList.toString());
        return readingList.getBookSet().stream().map(r -> mapper.map(r, BookListDTO.class)).toList();
    }
    /**
     * Given a user ID, return names of all the reading lists associated with the user.
     * - If the user doesn't exist, throw a user not found status.
     * @param userId
     * @return List<ReadingListDTO>
     * example:
     * [
     *      {
     *          id: 1
     *          name: "homework"
     *      },
     *      {
     *          id: 2
     *          name: "fun stuff"
     *      },
     */
    public List<ReadingListDTO> getReadingLists(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"user doesn't exist"));
        log.trace(user.toString());
        return readingListRepository.findByUser(user).stream().map(r -> mapper.map(r, ReadingListDTO.class)).toList();
    }

    /**
     * Given an user ID, and ReadingList. Create a reading list for the user.
     * - If the user doesn't exist, return an error.
     * - If the reading list doesn't exist create it.
     * - If a duplicate reading list "name" exists, overwrite it.
     * - If a user delete a book, the book is removed from the reading list.
     * @param userId
     * @param readingListDTO
     * @return List<BookListDTO>
     *   A list of books that wasn't pre-registered.
     *   example:
     *     [
     *         {
     *             "title": "22 Seconds",
     *             "pages":400,
     *             "author":"James Patterson"
     *         },
     *         {
     *             "title": "The Hunger Games",
     *             "pages":374,
     *             "author": "Suzanne Collins"
     *         }
     *     ]
     * }
     */
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
