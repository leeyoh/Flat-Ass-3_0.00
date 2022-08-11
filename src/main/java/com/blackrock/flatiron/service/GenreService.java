package com.blackrock.flatiron.service;

import com.blackrock.flatiron.dto.BookDTO;
import com.blackrock.flatiron.model.Genre;
import com.blackrock.flatiron.repository.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    private ModelMapper mapper;

    public List<BookDTO> getBooks(Long id){
        Genre genre = genreRepository.getReferenceById(id);

        return genre.getBookSet().stream().map(
                book -> {
                    BookDTO bookDTO = mapper.map(book, BookDTO.class);
                    String author = book.getAuthor().getName();
                    bookDTO.setAuthor(author);
                    bookDTO.setGenres(book.getGenreSet().stream().map(g -> g.getName()).toList());
                    return bookDTO;
                }
        ).toList();
    }
}
