package raf.si.racunovodstvo.user.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import raf.si.racunovodstvo.user.requests.LoginRequest;
import raf.si.racunovodstvo.user.utils.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void loginTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        assertEquals(200, authController.login(request).getStatusCodeValue());
        then(jwtUtil).should(times(1)).generateToken("username");
    }

    @Test
    void loginExceptionTest() {
        AuthenticationException authenticationException = Mockito.mock(AuthenticationException.class);
        LoginRequest request = new LoginRequest();
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(authenticationException);

        assertEquals(401, authController.login(request).getStatusCodeValue());
    }

    @Test
    void accessTest() {
        ResponseEntity<?> responseEntity = authController.access();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
