package com.blackrock.flatiron.dto;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CreateBookDTO {
    private String title;
    private int pages;
    private Date published;
    private String author;
    private List<String> genre;
}
