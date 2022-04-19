package raf.si.racunovodstvo.user.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String SECRET_MOCK = "secret";
    private static final String WRONG_SECRET_MOCK = "WRONG_SECRET";
    private static final String MALFORMED_TOKEN_MOCK = "Bearer MOCK";
    private static final String USERNAME_MOCK = "DUMMY_USERNAME";
    private static final String PASSWORD_MOCK = "DUMMY_PASSWORD";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET_MOCK);
    }

    @Test
    void extractUsernameTestSuccess() {
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() + 360000))
                         .signWith(SignatureAlgorithm.HS512, SECRET_MOCK)
                         .compact();

        assertEquals(USERNAME_MOCK, jwtUtil.extractUsername(jwt));
    }

    @Test
    void extractUsernameTestSecretFail() {
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() + 360000))
                         .signWith(SignatureAlgorithm.HS512, WRONG_SECRET_MOCK)
                         .compact();

        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(jwt));
    }

    @Test
    void extractUsernameTestTokenExpiredFail() {
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis() - 1000))
                         .setExpiration(new Date(System.currentTimeMillis() - 1000))
                         .signWith(SignatureAlgorithm.HS512, SECRET_MOCK)
                         .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(jwt));
    }

    @Test
    void extractUsernameTestMalformedTokenFail() {
        assertThrows(MalformedJwtException.class, () -> jwtUtil.extractUsername(MALFORMED_TOKEN_MOCK));
    }

    @Test
    void validateTokenTestSuccess() {
        UserDetails userDetails = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() + 100000))
                         .signWith(SignatureAlgorithm.HS512, SECRET_MOCK)
                         .compact();
        assertTrue(jwtUtil.validateToken(jwt, userDetails));
    }

    @Test
    void validateTokenTestExpiredFail() {
        UserDetails userDetails = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() - 1000))
                         .signWith(SignatureAlgorithm.HS512, SECRET_MOCK)
                         .compact();
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(jwt, userDetails));
    }

    @Test
    void validateTokenSecretFail() {
        UserDetails userDetails = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        String jwt = Jwts.builder()
                         .setClaims(new HashMap<>())
                         .setSubject(USERNAME_MOCK)
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() + 100000))
                         .signWith(SignatureAlgorithm.HS512, WRONG_SECRET_MOCK)
                         .compact();
        assertThrows(SignatureException.class, () -> jwtUtil.validateToken(jwt, userDetails));
    }

    @Test
    void validateTokenMalformedFail() {
        UserDetails userDetails = new User(USERNAME_MOCK, PASSWORD_MOCK, new HashSet<>());
        assertThrows(MalformedJwtException.class, () -> jwtUtil.validateToken(MALFORMED_TOKEN_MOCK, userDetails));
    }

    @Test
    void generateTokenTest() {
        String jwt = jwtUtil.generateToken(USERNAME_MOCK);
        String username = Jwts.parser().setSigningKey(SECRET_MOCK).parseClaimsJws(jwt).getBody().getSubject();
        assertEquals(USERNAME_MOCK, username);
    }
}
