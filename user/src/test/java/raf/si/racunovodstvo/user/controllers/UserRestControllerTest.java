package raf.si.racunovodstvo.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.requests.UpdateUserRequest;
import raf.si.racunovodstvo.user.services.impl.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @InjectMocks
    private UserRestController userRestController;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final Long MOCK_ID = 1L;


    @Test
    void getAllUsers() {
        given(userService.findAll()).willReturn(new ArrayList<>());
        ResponseEntity<?> responseEntity = userRestController.getAllUsers();

        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void getUserById() {
        User user = new User();
        given(userService.findById(MOCK_ID)).willReturn(Optional.of(user));
        ResponseEntity<?> responseEntity = userRestController.getUserById(MOCK_ID);

        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void getUserByIdException() {
        User user = new User();
        given(userService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userRestController.getUserById(MOCK_ID));
    }

    @Test
    void getLoginUser() {

    }

    @Test
    void createUser() {
        User user = new User();
        List<User> userList = new ArrayList<>();
        given(userService.findAll()).willReturn(userList);
        ResponseEntity<?> responseEntity = userRestController.createUser(user);

        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void createUserException() {
        User user = new User();
        user.setUsername("username");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        given(userService.findAll()).willReturn(userList);

        ResponseEntity<?> responseEntity = userRestController.createUser(user);

        assertEquals(responseEntity.getStatusCodeValue(), 403);
    }

    @Test
    void updateUser() {
        User user = new User();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(MOCK_ID);
        given(userService.findById(MOCK_ID)).willReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = userRestController.updateUser(updateUserRequest);

        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
    void updateUserException() {
        User user = new User();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(MOCK_ID);
        given(userService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userRestController.updateUser(updateUserRequest));
    }

    @Test
    void deleteUser() {
        User user = new User();
        given(userService.findById(MOCK_ID)).willReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = userRestController.deleteUser(MOCK_ID);

        assertEquals(responseEntity.getStatusCodeValue(), 204);
    }

    @Test
    void deleteUserException() {
        given(userService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userRestController.deleteUser(MOCK_ID));
    }
}