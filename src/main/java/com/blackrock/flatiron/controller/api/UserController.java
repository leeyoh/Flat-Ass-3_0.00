package com.blackrock.flatiron.controller.api;
import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.dto.CreateUserDTO;
import com.blackrock.flatiron.dto.UserDTO;
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
    public List<BookDTO> createReadingList(@PathVariable Long id, @RequestBody List<Long> booklist){
        return userService.createReadingList(id, booklist);
    }

}
