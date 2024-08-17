package com.example.demo.controllers;

import com.example.demo.TestUltis;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    public static final String USER_NAME = "test";
    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUltis.injectObjects(userController, "userRepository", userRepo);
        TestUltis.injectObjects(userController, "cartRepository", cartRepo);
        TestUltis.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testCreateUser() {
        when(encoder.encode("password")).thenReturn("hasshedpassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_NAME, user.getUsername());
        assertEquals("hasshedpassword", user.getPassword());
        assertEquals(0,user.getId());
    }

    @Test
    public void testFindByUserId() {
        User testUser = new User();
        testUser.setUsername(USER_NAME);
        testUser.setPassword("password");
        testUser.setConfirmPassword("password");
        when(userRepo.findById(0L)).thenReturn(Optional.of(testUser));
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        User userResponse = userResponseEntity.getBody();
        assertNotNull(userResponseEntity);
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertNotNull(userResponse);
        assertEquals(USER_NAME, userResponse.getUsername());
        assertEquals("password", userResponse.getPassword());
        assertEquals(0,userResponse.getId());
    }

    @Test
    public void testFindByUserName() {
        User testUser = new User();
        testUser.setUsername(USER_NAME);
        testUser.setPassword("password");
        testUser.setConfirmPassword("password");
        when(userRepo.findByUsername(USER_NAME)).thenReturn(testUser);
        ResponseEntity<User> userResponseEntity = userController.findByUserName(USER_NAME);
        User userResponse = userResponseEntity.getBody();
        assertNotNull(userResponseEntity);
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertNotNull(userResponse);
        assertEquals(USER_NAME, userResponse.getUsername());
        assertEquals("password", userResponse.getPassword());
        assertEquals(0,userResponse.getId());
    }

    @Test
    public void testFindByUserNameNotFound() {
        User testUser = new User();
        testUser.setUsername(USER_NAME);
        testUser.setPassword("password");
        testUser.setConfirmPassword("password");
        ResponseEntity<User> userResponseEntity = userController.findByUserName(USER_NAME);
        User userResponse = userResponseEntity.getBody();
        assertNotNull(userResponseEntity);
        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
        assertNull(userResponse);
    }

    @Test
    public void testPasswordNotEnoughLength() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword("test");
        createUserRequest.setConfirmPassword("test");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(user);
    }
    @Test
    public void testPasswordNotMatch() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("passwordd");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(user);
    }
    @Test
    public void testNoPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(user);
    }
}
