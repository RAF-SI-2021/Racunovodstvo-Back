package raf.si.racunovodstvo.user.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import raf.si.racunovodstvo.user.services.impl.UserService;
import raf.si.racunovodstvo.user.utils.JwtUtil;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    private static final String USERNAME_MOCK = "DUMMY_USERNAME";
    private static final String PASSWORD_MOCK = "DUMMY_PASSWORD";
    private static final String TOKEN_MOCK = "Bearer DUMMY_TOKEN";

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalSuccessTest() throws ServletException, IOException {
        UserDetails user = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", TOKEN_MOCK);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();
        String token = TOKEN_MOCK.substring(7);
        given(jwtUtil.validateToken(token, user)).willReturn(true);
        given(jwtUtil.extractUsername(token)).willReturn(USERNAME_MOCK);
        given(userService.loadUserByUsername(USERNAME_MOCK)).willReturn(user);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void doFilterInternalNoAuthHeaderTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        jwtFilter.doFilterInternal(request, response, filterChain);

        then(jwtUtil).should(never()).extractUsername(anyString());
    }

    @Test
    void doFilterInternalInvalidTokenTest() throws ServletException, IOException {
        UserDetails user = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", TOKEN_MOCK);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();
        String token = TOKEN_MOCK.substring(7);
        given(jwtUtil.validateToken(token, user)).willReturn(false);
        given(jwtUtil.extractUsername(token)).willReturn(USERNAME_MOCK);
        given(userService.loadUserByUsername(USERNAME_MOCK)).willReturn(user);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
