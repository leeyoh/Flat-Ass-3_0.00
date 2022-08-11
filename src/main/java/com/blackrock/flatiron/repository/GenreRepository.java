package com.blackrock.flatiron.repository;
import com.blackrock.flatiron.model.Author;
import com.blackrock.flatiron.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
