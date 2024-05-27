package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.LightNovel;
import org.example.proiectfinalsd.Entity.Manga;
import org.example.proiectfinalsd.Entity.User;
import org.example.proiectfinalsd.Repository.LightNovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LightNovelService {
    @Autowired
    private LightNovelRepository lightNovelRepository;

    public LightNovel findById(Long id) {
        return lightNovelRepository.findById(id).orElse(null);
    }

    public List<LightNovel> findAll() {
        return lightNovelRepository.findAll();
    }

    public LightNovel save(LightNovel lightNovel) {
        return lightNovelRepository.save(lightNovel);
    }

    public void deleteById(Long id) {
        lightNovelRepository.deleteById(id);
    }


}
