package org.example.proiectfinalsd.Cotroller;

import org.example.proiectfinalsd.Encoder;
import org.example.proiectfinalsd.Entity.*;
import org.example.proiectfinalsd.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
/**
 * AdminController handles all admin-related operations such as login, user management, and adding new titles.
 */
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

    @Autowired
    private AuthorService authorService;

    private static Encoder encoder;
    private static Admin adminLoggedIn;
    private static boolean adminLoggedInFlag = false;

    private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());

    public AdminController() {encoder = Encoder.getInstance();}
    /**
     * Logs in an admin user.
     *
     * POST /api/admin/login
     * Request:
     * @param username The admin's username
     * @param password The admin's password
     *
     * Responses:
     * 200 OK - Login successful
     * 401 UNAUTHORIZED - Invalid username or password
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        LOGGER.info("Attempting to log in user: " + username);
        Admin admin = adminService.findByUsername(username);
        if (admin != null && admin.getPassword().equals(Encoder.encodingPassword(password))) {
            adminLoggedIn = admin;
            adminLoggedInFlag = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            LOGGER.info("Login successful for user: " + username);
            return ResponseEntity.ok(response);
        } else {
            adminLoggedInFlag = false;
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            LOGGER.warning("Login failed for user: " + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Gets the currently logged-in admin user.
     *
     * GET /api/admin/current
     *
     * Responses:
     * 200 OK - Returns the current admin user
     * 404 NOT FOUND - No admin user is logged in
     */
    @GetMapping("/current")
    public ResponseEntity<Admin> getCurrentUser() {
        if (adminLoggedInFlag) {
            Admin admin = adminLoggedIn;
            admin.setPassword(null);
            return ResponseEntity.ok(admin);
        } else {
            LOGGER.warning("No admin user is currently logged in.");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets a list of all users.
     *
     * GET /api/admin/all
     *
     * Responses:
     * 200 OK - Returns a list of all users
     * 403 FORBIDDEN - No admin user is logged in
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        if (adminLoggedInFlag) {
            List<User> allUsers = userService.findAll();
            return ResponseEntity.ok(allUsers);
        } else {
            LOGGER.warning("Access denied. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Updates the currently logged-in admin user.
     *
     * POST /api/admin/update
     * Request:
     * @param updatedUser The updated admin user details
     *
     * Responses:
     * 200 OK - Admin user updated successfully
     * 401 UNAUTHORIZED - No admin user is logged in
     */
    @PostMapping("/update")
    public ResponseEntity<Admin> updateUser(@RequestBody Admin updatedUser) {
        if (adminLoggedInFlag) {
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                adminLoggedIn.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                adminLoggedIn.setPassword(Encoder.encodingPassword(updatedUser.getPassword()));
            }
            adminService.save(adminLoggedIn);
            LOGGER.info("Admin user updated successfully.");
            return ResponseEntity.ok(new Admin());
        } else {
            LOGGER.warning("Unauthorized update attempt. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Logs out the currently logged-in admin user.
     *
     * POST /api/admin/logout
     *
     * Responses:
     * 200 OK - Logout successful
     * 401 UNAUTHORIZED - No admin user is logged in
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser() {
        if (adminLoggedInFlag) {
            adminLoggedInFlag = false;
            adminLoggedIn = null;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout successful");
            LOGGER.info("Admin user logged out successfully.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in");
            LOGGER.warning("Logout attempt failed. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Updates a user by the admin.
     *
     * PUT /api/admin/update_user
     * Request:
     * @param updatedUser The updated user details
     *
     * Responses:
     * 200 OK - User updated successfully
     * 404 NOT FOUND - User not found
     * 403 FORBIDDEN - No admin user is logged in
     */
    @PutMapping("/update_user")
    public ResponseEntity<User> updateUserByAdmin(@RequestBody User updatedUser) {
        if (!adminLoggedInFlag || adminLoggedIn == null) {
            LOGGER.warning("Unauthorized update attempt. No admin user is logged in.");
            return ResponseEntity.notFound().build();
        }
        User existingUser = null;
        if (updatedUser.getId() != null) {
            existingUser = userService.findById(updatedUser.getId());
        } else if (updatedUser.getUsername() != null) {
            existingUser = userService.findByName(updatedUser.getUsername());
        }

        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            userService.save(existingUser);
            LOGGER.info("User updated successfully: " + updatedUser.getUsername());
            return ResponseEntity.ok(existingUser);
        } else {
            LOGGER.warning("User not found: " + updatedUser.getUsername());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a new Manga.
     *
     * POST /api/admin/newManga
     * Request:
     * @param name The name of the manga
     * @param author The author of the manga
     * @param genre The genre of the manga
     * @param chapters The number of chapters in the manga
     *
     * Responses:
     * 200 OK - Manga added successfully
     * 401 UNAUTHORIZED - No admin user is logged in
     */
    @PostMapping("/newManga")
    public ResponseEntity<Map<String, Object>> addManga(@RequestParam String name,
                                                        @RequestParam String author,
                                                        @RequestParam String genre,
                                                        @RequestParam int chapters) {
        Manga manga = new Manga();
        manga.setName(name);
        manga.setAuthor(authorService.findByName(author));
        manga.setGenre(genre);
        manga.setChapters(chapters);
        if (adminLoggedInFlag) {
            mangaService.save(manga);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manga added successfully");
            LOGGER.info("Manga added successfully: " + name);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No admin logged in");
            LOGGER.warning("Unauthorized attempt to add manga. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Adds a new Manhwa.
     *
     * POST /api/admin/newManhwa
     * Request:
     * @param name  The name of the manhwa
     * @param author The author of the manhwa
     *  @param genre    The genre of the manhwa
     *  @param chapters The number of chapters in the manhwa
     *  Responses:
     *  200 OK - Manhwa added successfully
     *  401 UNAUTHORIZED - No admin user is logged in
     */
    @PostMapping("/newManhwa")
    public ResponseEntity<Map<String, Object>> addManhwa(@RequestParam String name,
                                                        @RequestParam String author,
                                                        @RequestParam String genre,
                                                        @RequestParam int chapters) {
        Manhwa manhwa = new Manhwa();
        manhwa.setName(name);
        manhwa.setAuthor(authorService.findByName(author));
        manhwa.setGenre(genre);
        manhwa.setChapters(chapters);
//        System.out.println(manhwa);
        if(adminLoggedInFlag){
            manhwaService.save(manhwa);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manga added successfully");
            LOGGER.info("Manga added successfully: " + name);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No admin logged in");
            LOGGER.warning("Unauthorized attempt to add manga. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    /**
     * Adds a new Light Novel.
     *
     * POST /api/admin/newLightNovel
     * Request:
     * @param name The name of the light novel
     * @param author The author of the light novel
     * @param genre The genre of the light novel
     * @param chapters The number of chapters in the light novel
     *
     * Responses:
     * 200 OK - Manga added successfully
     * 401 UNAUTHORIZED - No admin user is logged in
     */
    @PostMapping("/newLightNovel")
    public ResponseEntity<Map<String, Object>> addLightNovel(@RequestParam String name,
                                                         @RequestParam String author,
                                                         @RequestParam String genre,
                                                         @RequestParam int chapters) {
        LightNovel lightNovel = new LightNovel();
        lightNovel.setName(name);
        lightNovel.setAuthor(authorService.findByName(author));
        lightNovel.setGenre(genre);
        lightNovel.setChapters(chapters);
        if(adminLoggedInFlag){
            lightNovelService.save(lightNovel);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manga added successfully");
            LOGGER.info("Manga added successfully: " + name);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No admin logged in");
            LOGGER.warning("Unauthorized attempt to add manga. No admin user is logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }



}
