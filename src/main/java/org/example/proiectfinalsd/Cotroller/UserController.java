package org.example.proiectfinalsd.Cotroller;

import org.example.proiectfinalsd.Encoder;
import org.example.proiectfinalsd.Entity.Admin;
import org.example.proiectfinalsd.Entity.User;
import org.example.proiectfinalsd.Repository.UserRepository;
import org.example.proiectfinalsd.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
    private static User userLoggedIn;
    private static boolean userLoggedInFlag = false;

    @Autowired
    private UserRepository userRepository;


    public UserController() {
        encoder = Encoder.getInstance();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        System.out.println("New user: " + user.toString() );
        if (userService.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("This email already is used");
        }

        String passEncoded = Encoder.encodingPassword(user.getPassword());
        System.out.println("Encoded password: " + passEncoded);
        user.setPassword(passEncoded);
        return userService.save(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        System.out.println("Username: " + username + " Password: " + encoder.encodingPassword(password));
        User user = userService.findByName(username);
        System.out.println("User: " + user);
        if (user != null && user.getPassword().equals(Encoder.encodingPassword(password))) {
            userLoggedIn= user;
            userLoggedInFlag = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } else {
            userLoggedInFlag = false;
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        if(userLoggedInFlag){
            User user = userLoggedIn;
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        if (userLoggedInFlag) {
            User userToUpdate = userService.findByUsername(userLoggedIn.getUsername());

            if (userToUpdate != null) {
                System.out.println("New user: " + updatedUser);
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    userToUpdate.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    userToUpdate.setPassword(Encoder.encodingPassword(updatedUser.getPassword()));
                }
                userService.save(userToUpdate);
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
        if (userLoggedInFlag) {
            userLoggedInFlag = false;
            userLoggedIn = null;
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



}


