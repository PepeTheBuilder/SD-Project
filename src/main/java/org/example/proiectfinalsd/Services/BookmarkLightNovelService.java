package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.BookmarkLightNovel;
import org.example.proiectfinalsd.Repository.BookmarkLightNovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkLightNovelService {

    @Autowired
    private BookmarkLightNovelRepository repository;

    public List<BookmarkLightNovel> findAll() {
        return repository.findAll();
    }

    public BookmarkLightNovel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BookmarkLightNovel save(BookmarkLightNovel bookmarkLightNovel) {
        return repository.save(bookmarkLightNovel);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<BookmarkLightNovel> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
