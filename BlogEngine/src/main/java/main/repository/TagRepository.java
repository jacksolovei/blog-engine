package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "SELECT * FROM tags WHERE name = :tagName", nativeQuery = true)
    Tag findTagByName(@Param("tagName") String tagName);
}
