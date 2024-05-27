package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.BookmarkLightNovel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkLightNovelRepository extends JpaRepository<BookmarkLightNovel, Long> {
    List<BookmarkLightNovel> findByUserId(Long userId);
}
