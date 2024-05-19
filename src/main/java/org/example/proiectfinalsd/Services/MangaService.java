package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.Manga;
import org.example.proiectfinalsd.Repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MangaService {
    @Autowired
    private MangaRepository mangaRepository;

    public Manga findById(Long id) {
        return mangaRepository.findById(id).orElse(null);
    }

    public List<Manga> findAll() {
        return mangaRepository.findAll();
    }

    public Manga save(Manga manga) {
        return mangaRepository.save(manga);
    }

    public void deleteById(Long id) {
        mangaRepository.deleteById(id);
    }
}
