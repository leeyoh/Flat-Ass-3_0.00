package com.blackrock.flatiron.repository;
import com.blackrock.flatiron.model.ReadingList;
import com.blackrock.flatiron.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingListRepository extends JpaRepository<ReadingList, Long> {
    ReadingList findByNameAndUser(String name, User user);
}
