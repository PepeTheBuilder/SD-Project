package org.example.proiectfinalsd.Repository;


import org.example.proiectfinalsd.Entity.LightNovel;
import org.example.proiectfinalsd.Entity.Manga;
import org.example.proiectfinalsd.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LightNovelRepository extends JpaRepository<LightNovel, Long> {

    LightNovel findByName(String name);
}
