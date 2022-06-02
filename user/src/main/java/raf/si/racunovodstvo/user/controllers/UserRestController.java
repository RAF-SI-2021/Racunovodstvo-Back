package raf.si.racunovodstvo.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import raf.si.racunovodstvo.user.model.Preduzece;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.requests.UpdateUserRequest;
import raf.si.racunovodstvo.user.services.impl.UserService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate;

    private String URL = "http://preduzece/api/preduzece/%d";

    public UserRestController(UserService userService, PasswordEncoder passwordEncoder, RestTemplate restTemplate){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    private Preduzece getPreduzeceById(Long id, String token) throws IOException {
        if(id == null){
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity request = new HttpEntity(headers);

        // baca izuzetak ako nije ispravan token
        ResponseEntity<String> response = restTemplate.exchange(String.format(URL, id), HttpMethod.GET, request, String.class);
        String result = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, Preduzece.class);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@RequestParam("userId") Long id){
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        throw new EntityNotFoundException();
    }

    @GetMapping(value = "/loginuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLoginUser(){
        Optional<User> optionalUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!optionalUser.isEmpty()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        throw new EntityNotFoundException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user, @RequestHeader(name="Authorization") String token) throws IOException {
        List<User> allUsers = userService.findAll();
        for(User u : allUsers){
            if (u.getUsername().equals(user.getUsername())){
                return ResponseEntity.status(HttpStatus.valueOf(403)).build();
            }
        }
        if (getPreduzeceById(user.getPreduzeceId(), token) == null){
            return ResponseEntity.status(HttpStatus.valueOf(404)).build();
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest updatedUser, @RequestHeader(name="Authorization") String token) throws IOException {
        Optional<User> optionalUser = userService.findById(updatedUser.getUserId());
        if (optionalUser.isPresent() && getPreduzeceById(updatedUser.getPreduzeceId(), token) != null){
            optionalUser.get().setUsername(updatedUser.getUsername());
            optionalUser.get().setFirstName(updatedUser.getFirstName());
            optionalUser.get().setLastName(updatedUser.getLastName());
            optionalUser.get().setEmail(updatedUser.getEmail());
            optionalUser.get().setPreduzeceId(updatedUser.getPreduzeceId());
            optionalUser.get().setPermissions(updatedUser.getPermissions());
            return ResponseEntity.ok(userService.save(optionalUser.get()));
        }

        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new EntityNotFoundException();
    }

}
