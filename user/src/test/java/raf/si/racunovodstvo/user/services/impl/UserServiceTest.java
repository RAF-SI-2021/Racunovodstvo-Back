package raf.si.racunovodstvo.user.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private static final Long MOCK_ID = 1L;
    private static final String USERNAME = "user1";

    @Test
    void save() {
        User user = new User();
        given(userRepository.save(user)).willReturn(user);

        assertEquals(user, userService.save(user));
    }

    @Test
    void findById() {
        User user = new User();
        given(userRepository.findByUserId(MOCK_ID)).willReturn(Optional.of(user));

        Optional<User> optionalUser = userRepository.findByUserId(MOCK_ID);
        if (optionalUser.isPresent()){
            System.out.println("Nasao sam usera sa id-em " + MOCK_ID);
            System.out.println(optionalUser.get().getUsername());
        }

        assertEquals(user, userService.findById(MOCK_ID).get());
    }

    @Test
    void findAll() {
        List<User> userList = new ArrayList<>();
        given(userRepository.findAll()).willReturn(userList);

        assertEquals(userList, userService.findAll());
    }

    @Test
    void deleteById() {
        userService.deleteById(MOCK_ID);
        then(userRepository).should(times(1)).deleteById(MOCK_ID);
    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(new String());
        user.setPermissions(new ArrayList<>());
        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(user));

        assertEquals(user.getUsername(), userService.loadUserByUsername(USERNAME).getUsername());
    }

    @Test
    void testLoadUserByUsernameNonValidUser() {
        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(USERNAME));
    }
}