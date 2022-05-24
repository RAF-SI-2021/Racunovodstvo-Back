package raf.si.racunovodstvo.knjizenje.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @InjectMocks
    private AuthFilter authFilter;

    @Mock
    private RestTemplate restTemplate;

    private static final String MOCK_REQUEST_URI = "DUMMY_URI";
    private static final String MOCK_TOKEN = "DUMMY_TOKEN";
    private static final String EXCLUDED_REQUEST_URI = "/swagger-ui.html";
    private static final String ERROR_RESPONSE = "The token is not valid.";

    @Test
    void doFilterInternalSuccessTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(MOCK_REQUEST_URI);
        request.addHeader("Authorization", MOCK_TOKEN);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = new MockFilterChain();
        given(restTemplate.exchange(anyString(),
                                    any(HttpMethod.class),
                                    any(HttpEntity.class),
                                    any(Class.class))).willReturn(new ResponseEntity<>(HttpStatus.OK));

        authFilter.doFilterInternal(request, response, filterChain);

        then(response).shouldHaveNoInteractions();
        then(restTemplate).should(times(1)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
    }

    @Test
    void doFilterInternalExceptionTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(MOCK_REQUEST_URI);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = new MockFilterChain();
        given(restTemplate.exchange(anyString(),
                                    any(HttpMethod.class),
                                    any(HttpEntity.class),
                                    any(Class.class))).willThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        authFilter.doFilterInternal(request, response, filterChain);

        then(response).should(times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, ERROR_RESPONSE);
    }

    @Test
    void doFilterInternalExcludedUriTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(EXCLUDED_REQUEST_URI);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = new MockFilterChain();

        authFilter.doFilterInternal(request, response, filterChain);

        then(response).shouldHaveNoInteractions();
        then(restTemplate).shouldHaveNoInteractions();
    }
}
