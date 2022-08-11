package com.blackrock.flatiron.service;
import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.dto.CreateUserDTO;
import com.blackrock.flatiron.dto.UserDTO;
import com.blackrock.flatiron.model.User;
import com.blackrock.flatiron.repository.AuthorRepository;
import com.blackrock.flatiron.repository.BookRepository;
import com.blackrock.flatiron.repository.GenreRepository;
import com.blackrock.flatiron.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    private ModelMapper mapper;
    public UserDTO createUser(CreateUserDTO userDTO){
        User user = mapper.map(userDTO, User.class);
        userRepository.save(user);
        return mapper.map(user,UserDTO.class);
    }
    public List<BookDTO> createReadingList(Long userId, List<Long> bookIdList){
        return null;
    }
}
