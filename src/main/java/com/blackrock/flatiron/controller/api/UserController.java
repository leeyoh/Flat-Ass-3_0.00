package com.blackrock.flatiron.controller.api;
import com.blackrock.flatiron.dto.*;
import com.blackrock.flatiron.service.GenreService;
import com.blackrock.flatiron.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public UserDTO createUser(@RequestBody CreateUserDTO userDTO){
        return userService.createUser(userDTO);
    }
    @PostMapping("/{id}/reading_lists")
    public List<BookListDTO> createReadingList(@PathVariable Long id, @RequestBody CreateReadingListDTO readingListDTO){
        return userService.createReadingList(id, readingListDTO);
    }
}
