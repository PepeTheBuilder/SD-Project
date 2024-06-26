package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.BookmarkManga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkMangaRepository extends JpaRepository<BookmarkManga, Long> {
    List<BookmarkManga> findByUserId(Long userId);
}
