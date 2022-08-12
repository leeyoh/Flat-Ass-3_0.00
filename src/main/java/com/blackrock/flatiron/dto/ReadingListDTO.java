package com.blackrock.flatiron.dto;

import lombok.Data;

@Data
public class ReadingListDTO {
    private String name;
    private Long id;

    @Override
    public String toString() {
        return "ReadingListDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
