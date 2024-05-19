package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MangaRepository extends JpaRepository<Manga, Long> {
}