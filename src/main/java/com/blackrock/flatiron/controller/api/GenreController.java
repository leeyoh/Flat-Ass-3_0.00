package com.blackrock.flatiron.controller.api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/genre")

public class GenreController {
    @GetMapping
    public String getTest(){
        return "genre";
    }

}
