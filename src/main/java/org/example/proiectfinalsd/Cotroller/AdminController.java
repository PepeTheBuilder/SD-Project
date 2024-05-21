package org.example.proiectfinalsd.Cotroller;

import org.example.proiectfinalsd.Encoder;
import org.example.proiectfinalsd.Entity.Admin;
import org.example.proiectfinalsd.Entity.User;
import org.example.proiectfinalsd.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookmarkManhwaService bookmarkManhwaService;

    @Autowired
    private BookmarkLightNovelService bookmarkLightNovelService;

    @Autowired
    private BookmarkMangaService bookmarkMangaService;

    @Autowired
    private MangaService mangaService;

    @Autowired
    private ManhwaService manhwaService;

    @Autowired
    private LightNovelService lightNovelService;

    private static Encoder encoder;
    private static Admin adminLoggedIn;
    private static boolean adminLoggedInFlag = false;



    public AdminController() {
        encoder = Encoder.getInstance();
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        Admin admin = adminService.findByUsername(username);
        if (admin != null && admin.getPassword().equals(Encoder.encodingPassword(password))) {
            adminLoggedIn= admin;
            adminLoggedInFlag = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } else {
            adminLoggedInFlag = false;
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/current")
    public ResponseEntity<Admin> getCurrentUser() {
        if(adminLoggedInFlag){
            Admin admin = adminLoggedIn;
            admin.setPassword(null);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        if(adminLoggedInFlag){
            List<User> allUsers = userService.findAll();
            return ResponseEntity.ok(allUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Admin> updateUser(@RequestBody User updatedUser) {
        if (adminLoggedInFlag) {
            Admin userToUpdate = adminService.findByUsername(adminLoggedIn.getUsername());

            if (userToUpdate != null) {
                System.out.println("New user: " + updatedUser);
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    userToUpdate.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    userToUpdate.setPassword(Encoder.encodingPassword(updatedUser.getPassword()));
                }
                adminService.save(userToUpdate);
                return ResponseEntity.ok(userToUpdate);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser() {
        if (adminLoggedInFlag) {
            adminLoggedInFlag = false;
            adminLoggedIn = null;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @PutMapping("/update_user") // by admin
    public ResponseEntity<User> updateUserByAdmin(@RequestBody User updatedUser) {
        if(!adminLoggedInFlag || adminLoggedIn == null) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = null;
        System.out.println("New user: " + updatedUser);
        if (updatedUser.getId() != null) {
            existingUser = userService.findById(updatedUser.getId());
        } else if (updatedUser.getUsername() != null) {
            existingUser = userService.findByName(updatedUser.getUsername());
        }

        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());

            userService.save(existingUser);

            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
