package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.BookmarkManhwa;
import org.example.proiectfinalsd.Repository.BookmarkManhwaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkManhwaService {

    @Autowired
    private BookmarkManhwaRepository repository;

    public List<BookmarkManhwa> findAll() {
        return repository.findAll();
    }

    public BookmarkManhwa findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BookmarkManhwa save(BookmarkManhwa bookmarkManhwa) {
        return repository.save(bookmarkManhwa);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
