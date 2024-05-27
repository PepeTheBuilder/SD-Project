package org.example.proiectfinalsd.Cotroller;

import org.example.proiectfinalsd.Encoder;
import org.example.proiectfinalsd.Entity.*;
import org.example.proiectfinalsd.Repository.UserRepository;
import org.example.proiectfinalsd.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        User user = userService.findByName(username);
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
    @GetMapping("/ShowManga")
    public List<NovelToPrint> showManga() {
        return getListNovelToPrint(mangaService.findAll());
    }
    @GetMapping("/ShowManhwa")
    public List<NovelToPrint> showManhwa() {
        return getListNovelToPrint(manhwaService.findAll());
    }
    @GetMapping("/ShowLightNovel")
    public List<NovelToPrint>  showLightNovel() {
        return getListNovelToPrint(lightNovelService.findAll());
    }
    @GetMapping("/ShowMyBookmarkManga")
    public ResponseEntity<List<BookmarkToPrint>>  showMyBookmarkManga() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkMangaService.findByUserId(userLoggedIn.getId())));
    }
    @GetMapping("/ShowMyBookmarkManhwa")
    public ResponseEntity<List<BookmarkToPrint>>  showMyBookmarkManhwa() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkManhwaService.findByUserId(userLoggedIn.getId())));
    }
    @GetMapping("/ShowMyBookmarkLightNovel")
    public ResponseEntity<List<BookmarkToPrint>>  showMyBookmarkLightNovel() {
        return ResponseEntity.ok(getBookmarkToPrintList(bookmarkLightNovelService.findByUserId(userLoggedIn.getId())));
    }
    @PostMapping("/AddManhwaTOMyList")
    public ResponseEntity<Map<String, Object>> addManhwaToMyList(@RequestParam BookmarkToPrint bookmarkToPrint) {
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
    @PostMapping ("/AddMangaTOMyList")
    public ResponseEntity<Map<String, Object>> addMangaToMyList(@RequestParam BookmarkToPrint bookmarkToPrint) {
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
    @PostMapping("/AddLightNovelTOMyList")
    public ResponseEntity<Map<String, Object>> addLightNovelToMyList(@RequestParam BookmarkToPrint bookmarkToPrint) {

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
                case "BookmarkManga" -> listBookmarkToPrint.add(((BookmarkManga) object).getBookmarkToPrint()) ;
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
                case "Manga" -> listNovelToPrint.add(((Manga) object).getNovelToPrint()) ;
                case "LightNovel" -> listNovelToPrint.add(((LightNovel) object).getNovelToPrint());
                default -> throw new IllegalStateException("Unexpected value: " + object.getClass().getSimpleName());
            }
        }
        return listNovelToPrint;
    }

    private boolean addToMyList (String category, Object object) {
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


