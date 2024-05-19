package org.example.proiectfinalsd.Services;

import org.example.proiectfinalsd.Entity.Admin;
import org.example.proiectfinalsd.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }
}
