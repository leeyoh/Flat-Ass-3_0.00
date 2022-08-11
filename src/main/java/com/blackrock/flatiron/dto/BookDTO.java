package com.blackrock.flatiron.dto;
import lombok.Data;
import java.util.Date;

@Data
public class BookDTO {
    private String title;
    private int pages;
    private Date published;
}

