package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.BookmarkManga;
import org.example.proiectfinalsd.Repository.BookmarkMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkMangaService {

    @Autowired
    private BookmarkMangaRepository repository;

    public List<BookmarkManga> findAll() {
        return repository.findAll();
    }

    public BookmarkManga findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BookmarkManga save(BookmarkManga bookmarkManga) {
        return repository.save(bookmarkManga);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<BookmarkManga> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

}
