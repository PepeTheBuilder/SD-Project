package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
