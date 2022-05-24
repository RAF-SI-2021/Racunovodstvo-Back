package raf.si.racunovodstvo.preduzece.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    RestTemplate restTemplate;

    private static final String URL = "http://user/auth/access";

    private static final String[] EXCLUDED_URLS = {"/v3/api-docs", "/swagger-ui.html", "/swagger-ui/"};

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            if (!skipFilter(httpServletRequest.getRequestURL().toString())) {
                String authHeader = httpServletRequest.getHeader("Authorization");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity request = new HttpEntity(headers);

                // baca izuzetak ako nije ispravak token
                ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (HttpClientErrorException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
        }

    }

    private Boolean skipFilter(String url) {
        return Arrays.stream(EXCLUDED_URLS).sequential().anyMatch(exUrl -> url.contains(exUrl));
    }
}
