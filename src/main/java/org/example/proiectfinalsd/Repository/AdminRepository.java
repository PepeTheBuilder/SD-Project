package org.example.proiectfinalsd.Repository;

import org.example.proiectfinalsd.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByName(String username);
}
