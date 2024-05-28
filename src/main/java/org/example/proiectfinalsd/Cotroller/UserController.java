package org.example.proiectfinalsd.Cotroller;

import org.example.proiectfinalsd.Encoder;
import org.example.proiectfinalsd.Entity.*;
import org.example.proiectfinalsd.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * UserController handles all user-related operations such as registration, login, and bookmark management etc.
 */
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
    static User userLoggedIn;
    static boolean userLoggedInFlag = false;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    public UserController() {
        encoder = Encoder.getInstance();
    }

    /**
     * Registers a new user.
     *
     * POST /api/users/register
     * Request:
     * @param user The user details
     *
     * Responses:
     * User object - User registered successfully
     * RuntimeException - This email already exists
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        LOGGER.info("Registering new user: " + user.toString());
        if (userService.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("This email already exists");
        }

        String passEncoded = Encoder.encodingPassword(user.getPassword());
        LOGGER.info("Encoded password: " + passEncoded);
        user.setPassword(passEncoded);
        return userService.save(user);
    }

    /**
     * Logs in a user.
     *
     * POST /api/users/login
     * Request:
     * @param username The user's username
     * @param password The user's password
     *
     * Responses:
     * 200 OK - Login successful
     * 401 UNAUTHORIZED - Invalid username or password
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        LOGGER.info("Attempting to log in user: " + username);
        User user = userService.findByName(username);
        if (user != null && user.getPassword().equals(Encoder.encodingPassword(password))) {
            userLoggedIn = user;
            userLoggedInFlag = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            LOGGER.info("Login successful for user: " + username);
            return ResponseEntity.ok(response);
        } else {
            userLoggedInFlag = false;
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            LOGGER.warning("Login failed for user: " + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Gets the currently logged-in user.
     *
     * GET /api/users/current
     *
     * Responses:
     * 200 OK - Returns the current user
     * 404 NOT FOUND - No user is logged in
     */
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        if (userLoggedInFlag) {
            User user = userLoggedIn;
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            LOGGER.warning("No user is currently logged in.");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the currently logged-in user.
     *
     * POST /api/users/update
     * Request:
     * @param updatedUser The updated user details
     *
     * Responses:
     * 200 OK - User updated successfully
     * 401 UNAUTHORIZED - No user is logged in
     */
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        if (userLoggedInFlag) {
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                userLoggedIn.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                userLoggedIn.setPassword(Encoder.encodingPassword(updatedUser.getPassword()));
            }
            userService.save(userLoggedIn);
            return ResponseEntity.ok(new User());

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Logs out the currently logged-in user.
     *
     * POST /api/users/logout
     *
     * Responses:
     * 200 OK - Logout successful
     * 401 UNAUTHORIZED - No user is logged in
     */
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

    /**
     * Retrieves all Manga.
     *
     * GET /api/users/ShowManga
     *
     * Responses:
     * List of manga to print
     */
    @GetMapping("/ShowManga")
    public List<NovelToPrint> showManga() {
        return getListNovelToPrint(mangaService.findAll());
    }

    /**
     * Retrieves all Manhwa.
     *
     * GET /api/users/ShowManhwa
     *
     * Responses:
     * List of manhwa to print
     */
    @GetMapping("/ShowManhwa")
    public List<NovelToPrint> showManhwa() {
        return getListNovelToPrint(manhwaService.findAll());
    }

    /**
     * Retrieves all Light Novels.
     *
     * GET /api/users/ShowLightNovel
     *
     * Responses:
     * List of light novels to print
     */
    @GetMapping("/ShowLightNovel")
    public List<NovelToPrint> showLightNovel() {
        return getListNovelToPrint(lightNovelService.findAll());
    }

    /**
     * Retrieves all Manga bookmarks of the logged-in user.
     *
     * GET /api/users/ShowMyBookmarkManga
     *
     * Responses:
     * 200 OK - List of bookmarks
     */
    @GetMapping("/ShowMyBookmarkManga")
    public ResponseEntity<List<BookmarkToPrint>> showMyBookmarkManga() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkMangaService.findByUserId(userLoggedIn.getId())));
    }

    /**
     * Retrieves all Manhwa bookmarks of the logged-in user.
     *
     * GET /api/users/ShowMyBookmarkManhwa
     *
     * Responses:
     * 200 OK - List of bookmarks
     */
    @GetMapping("/ShowMyBookmarkManhwa")
    public ResponseEntity<List<BookmarkToPrint>> showMyBookmarkManhwa() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkManhwaService.findByUserId(userLoggedIn.getId())));
    }

    /**
     * Retrieves all Light Novel bookmarks of the logged-in user.
     *
     * GET /api/users/ShowMyBookmarkLightNovel
     *
     * Responses:
     * 200 OK - List of bookmarks
     */
    @GetMapping("/ShowMyBookmarkLightNovel")
    public ResponseEntity<List<BookmarkToPrint>> showMyBookmarkLightNovel() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkLightNovelService.findByUserId(userLoggedIn.getId())));
    }

    /**
     * Adds a Manhwa to the logged-in user's list.
     *
     * POST /api/users/AddManhwaTOMyList
     * Request:
     * @param title The title of the Manhwa
     * @param userLastChapter The last chapter read by the user
     * @param userReadingStatus The reading status of the user
     * @param userScore The score given by the user
     *
     * Responses:
     * 200 OK - Manhwa added successfully
     * 401 UNAUTHORIZED - No user logged in or other error
     */
    @PostMapping("/AddManhwaTOMyList")
    public ResponseEntity<Map<String, Object>> addManhwaToMyList(@RequestParam("title") String title,
                                                                 @RequestParam("userLastChapter") Integer userLastChapter,
                                                                 @RequestParam("userReadingStatus") String userReadingStatus,
                                                                 @RequestParam("userScore") Float userScore) {
        BookmarkToPrint bookmarkToPrint = new BookmarkToPrint(title, userLastChapter, userReadingStatus, userScore);
        LOGGER.info("Adding Manhwa to list: " + bookmarkToPrint);
        BookmarkManhwa bookmarkManhwa = new BookmarkManhwa(bookmarkToPrint, userLoggedIn);
        bookmarkManhwa.setManhwa(manhwaService.findByName(bookmarkToPrint.name));

        if (addToMyList("Manhwa", bookmarkManhwa)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manhwa added to your list");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in or other error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Adds a Manga to the logged-in user's list.
     *
     * POST /api/users/AddMangaTOMyList
     * Request:
     * @param title The title of the Manga
     * @param userLastChapter The last chapter read by the user
     * @param userReadingStatus The reading status of the user
     * @param userScore The score given by the user
     *
     * Responses:
     * 200 OK - Manga added successfully
     * 401 UNAUTHORIZED - No user logged in or other error
     */
    @PostMapping ("/AddMangaTOMyList")
    public ResponseEntity<Map<String, Object>> addMangaToMyList(@RequestParam("title") String title,
                                                                @RequestParam("userLastChapter") Integer userLastChapter,
                                                                @RequestParam("userReadingStatus") String userReadingStatus,
                                                                @RequestParam("userScore") Float userScore) {
        BookmarkToPrint bookmarkToPrint = new BookmarkToPrint(title, userLastChapter, userReadingStatus, userScore);

        BookmarkManhwa bookmarkManhwa = new BookmarkManhwa(bookmarkToPrint, userLoggedIn);
        bookmarkManhwa.setUser(userLoggedIn);
        bookmarkManhwa.setUserScore(bookmarkToPrint.userScore);
        bookmarkManhwa.setUserLastChapter(bookmarkToPrint.userLastChapter);
        bookmarkManhwa.setUserReadingStatus(bookmarkToPrint.userReadingStatus);
        bookmarkManhwa.setManhwa(manhwaService.findByName(bookmarkToPrint.name));

        if (addToMyList("Manga", bookmarkManhwa)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manga added to your list");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in or other error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Adds a Light Novel to the logged-in user's list.
     *
     * POST /api/users/AddLightNovelTOMyList
     * Request:
     * @param title The title of the Light Novel
     * @param userLastChapter The last chapter read by the user
     * @param userReadingStatus The reading status of the user
     * @param userScore The score given by the user
     *
     * Responses:
     * 200 OK - Light Novel added successfully
     * 401 UNAUTHORIZED - No user logged in or other error
     */
    @PostMapping("/AddLightNovelTOMyList")
    public ResponseEntity<Map<String, Object>> addLightNovelToMyList(@RequestParam("title") String title,
                                                                     @RequestParam("userLastChapter") Integer userLastChapter,
                                                                     @RequestParam("userReadingStatus") String userReadingStatus,
                                                                     @RequestParam("userScore") Float userScore) {
        BookmarkToPrint bookmarkToPrint = new BookmarkToPrint(title, userLastChapter, userReadingStatus, userScore);
        BookmarkLightNovel bookmarkLightNovel = new BookmarkLightNovel();
        bookmarkLightNovel.setUser(userLoggedIn);
        bookmarkLightNovel.setUserScore(bookmarkToPrint.userScore);
        bookmarkLightNovel.setUserLastChapter(bookmarkToPrint.userLastChapter);
        bookmarkLightNovel.setUserReadingStatus(bookmarkToPrint.userReadingStatus);
        bookmarkLightNovel.setLightNovel(lightNovelService.findByName(bookmarkToPrint.name));

        if (addToMyList("LightNovel", bookmarkLightNovel)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Light Novel added to your list");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in or other error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private <T> ArrayList<BookmarkToPrint> getBookmarkToPrintList(List<T> listOfObjects) {
        ArrayList<BookmarkToPrint> listBookmarkToPrint = new ArrayList<>();
        for (T object : listOfObjects) {
            switch (object.getClass().getSimpleName()) {
                case "BookmarkManhwa" -> listBookmarkToPrint.add(((BookmarkManhwa) object).getBookmarkToPrint());
                case "BookmarkManga" -> listBookmarkToPrint.add(((BookmarkManga) object).getBookmarkToPrint());
                case "BookmarkLightNovel" -> listBookmarkToPrint.add(((BookmarkLightNovel) object).getBookmarkToPrint());
                default -> throw new IllegalStateException("Unexpected value: " + object.getClass().getSimpleName());
            }
        }
        return listBookmarkToPrint;
    }

    private <T> ArrayList<NovelToPrint> getListNovelToPrint(List<T> listOfObjects) {
        ArrayList<NovelToPrint> listNovelToPrint = new ArrayList<>();
        for (T object : listOfObjects) {
            switch (object.getClass().getSimpleName()) {
                case "Manhwa" -> listNovelToPrint.add(((Manhwa) object).getNovelToPrint());
                case "Manga" -> listNovelToPrint.add(((Manga) object).getNovelToPrint());
                case "LightNovel" -> listNovelToPrint.add(((LightNovel) object).getNovelToPrint());
                default -> throw new IllegalStateException("Unexpected value: " + object.getClass().getSimpleName());
            }
        }
        return listNovelToPrint;
    }

    private boolean addToMyList(String category, Object object) {
        if (userLoggedInFlag) {
            return switch (category) {
                case "Manga" -> {
                    bookmarkMangaService.save((BookmarkManga) object);
                    yield true;
                }
                case "Manhwa" -> {
                    bookmarkManhwaService.save((BookmarkManhwa) object);
                    yield true;
                }
                case "LightNovel" -> {
                    bookmarkLightNovelService.save((BookmarkLightNovel) object);
                    yield true;
                }
                default -> false;
            };
        } else {
            return false;
        }
    }
}
