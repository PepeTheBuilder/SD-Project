package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.Manga;
import org.example.proiectfinalsd.Entity.Manhwa;
import org.example.proiectfinalsd.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManhwaRepository extends JpaRepository<Manhwa, Long> {
}
