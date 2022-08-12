package com.blackrock.flatiron.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateReadingListDTO {
    private String username;
    private String reading_list_name;
    private List<BookListDTO> reading_list;


    @Override
    public String toString() {
        return "CreateReadingListDTO{" +
                "username='" + username + '\'' +
                ", reading_list_name='" + reading_list_name + '\'' +
                ", reading_list=" + reading_list +
                '}';
    }
}

