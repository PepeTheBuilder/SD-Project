package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.BookmarkManhwa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkManhwaRepository extends JpaRepository<BookmarkManhwa, Long> {
    List<BookmarkManhwa> findByUserId(Long userId);

}
