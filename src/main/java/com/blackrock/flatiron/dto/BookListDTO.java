package com.blackrock.flatiron.dto;

import lombok.Data;

@Data
public class BookListDTO{
    private String title;
    private int pages;
    private String author;
    @Override
    public String toString() {
        return "BookListDTO{" +
                "title='" + title + '\'' +
                ", pages=" + pages +
                ", author='" + author + '\'' +
                '}';
    }
}
