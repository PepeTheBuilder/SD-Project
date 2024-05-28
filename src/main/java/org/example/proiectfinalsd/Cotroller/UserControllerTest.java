package org.example.proiectfinalsd.Cotroller;
//
//import org.example.proiectfinalsd.Cotroller.UserController;
//import org.example.proiectfinalsd.Encoder;
//import org.example.proiectfinalsd.Entity.BookmarkToPrint;
//import org.example.proiectfinalsd.Entity.User;
//import org.example.proiectfinalsd.Services.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//
public class UserControllerTest {

}
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserService userService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testRegisterUser_Success() {
//        User user = new User();
//        user.setEmail("test@email.com");
//        user.setPassword("password");
//
//        when(userService.findByEmail(user.getEmail())).thenReturn(null);
//        when(userService.save(user)).thenReturn(user);
//
//        User registeredUser = userController.registerUser(user);
//
//        assertEquals(user, registeredUser);
//    }
//
//    @Test
//    public void testRegisterUser_EmailExists() {
//        User user = new User();
//        user.setEmail("test@email.com");
//        user.setPassword("password");
//
//        when(userService.findByEmail(user.getEmail())).thenReturn(new User());
//
//        assertThrows(RuntimeException.class, () -> userController.registerUser(user));
//    }
//
//    @Test
//    public void testLoginUser_Success() {
//        String username = "test_user";
//        String password = "password";
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("encodedPassword");
//
//        when(userService.findByName(username)).thenReturn(user);
//        when(Encoder.encodingPassword(password)).thenReturn("encodedPassword");
//
//        Map<String, Object> expectedResponse = new HashMap<>();
//        expectedResponse.put("success", true);
//        expectedResponse.put("message", "Login successful");
//
//        ResponseEntity<Map<String, Object>> responseEntity = userController.loginUser(username, password);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse, responseEntity.getBody());
//    }
//
//    @Test
//    public void testLoginUser_InvalidCredentials() {
//        String username = "test_user";
//        String password = "wrongPassword";
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("encodedPassword");
//
//        when(userService.findByName(username)).thenReturn(user);
//        when(Encoder.encodingPassword(password)).thenReturn("wrongEncodedPassword");
//
//        Map<String, Object> expectedResponse = new HashMap<>();
//        expectedResponse.put("success", false);
//        expectedResponse.put("message", "Invalid username or password");
//
//        ResponseEntity<Map<String, Object>> responseEntity = userController.loginUser(username, password);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
//        assertEquals(expectedResponse, responseEntity.getBody());
//    }
//
//    @Test
//    public void testGetCurrentUser_Success() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("test_user");
//
//        UserController.userLoggedIn = user;
//        UserController.userLoggedInFlag = true;
//
//        ResponseEntity<User> responseEntity = userController.getCurrentUser();
//
//        user.setPassword(null);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(user, responseEntity.getBody());
//    }
//
//    @Test
//    public void testGetCurrentUser_NotLoggedIn() {
//        userController.userLoggedInFlag = false;
//
//        ResponseEntity<User> responseEntity = userController.getCurrentUser();
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    }
//
//}