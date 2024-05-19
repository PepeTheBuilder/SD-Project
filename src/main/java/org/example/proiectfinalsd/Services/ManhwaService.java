package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.Manhwa;
import org.example.proiectfinalsd.Repository.ManhwaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManhwaService {
    @Autowired
    private ManhwaRepository manhwaRepository;

    public Manhwa findById(Long id){
        return manhwaRepository.findById(id).orElse(null);
    }

    public List<Manhwa> findAll() {
        return manhwaRepository.findAll();
    }

    public Manhwa save(Manhwa manhwa) {
        return manhwaRepository.save(manhwa);
    }

    public void deleteById(Long id) {
        manhwaRepository.deleteById(id);
    }
}