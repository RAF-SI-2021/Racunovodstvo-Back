package raf.si.racunovodstvo.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.HashSet;
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
    private static final String MOCK_USERNAME = "DUMMY_USERNAME";


    @Test
    void getLoginUserSuccessTest() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(MOCK_USERNAME, ""));
        UserDetails user = new org.springframework.security.core.userdetails.User(MOCK_USERNAME, "", new HashSet<>());
        given(userService.loadUserByUsername(MOCK_USERNAME)).willReturn(user);

        org.springframework.security.core.userdetails.User responseUser =
            (org.springframework.security.core.userdetails.User) userRestController.getLoginUser().getBody();

        assertEquals(user, responseUser);
    }

    @Test
    void getLoginUserFailTest() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(MOCK_USERNAME, ""));
        given(userService.loadUserByUsername(MOCK_USERNAME)).willReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userRestController.getLoginUser());
    }

    @Test
    void getAllUsers() {
        given(userService.findAll()).willReturn(new ArrayList<>());
        ResponseEntity<?> responseEntity = userRestController.getAllUsers();

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getUserById() {
        User user = new User();
        given(userService.findById(MOCK_ID)).willReturn(Optional.of(user));
        ResponseEntity<?> responseEntity = userRestController.getUserById(MOCK_ID);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getUserByIdException() {
        given(userService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userRestController.getUserById(MOCK_ID));
    }

    @Test
    void createUser() {
        User user = new User();
        List<User> userList = new ArrayList<>();
        given(userService.findAll()).willReturn(userList);
        ResponseEntity<?> responseEntity = userRestController.createUser(user);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void createUserException() {
        User user = new User();
        user.setUsername("username");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        given(userService.findAll()).willReturn(userList);

        ResponseEntity<?> responseEntity = userRestController.createUser(user);

        assertEquals(403, responseEntity.getStatusCodeValue());
    }

    @Test
    void updateUser() {
        User user = new User();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(MOCK_ID);
        given(userService.findById(MOCK_ID)).willReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = userRestController.updateUser(updateUserRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
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

        assertEquals(204, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteUserException() {
        given(userService.findById(MOCK_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userRestController.deleteUser(MOCK_ID));
    }
}
