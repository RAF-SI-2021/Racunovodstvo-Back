package rs.raf.userservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.userservice.model.User;
import rs.raf.userservice.repositories.UserRepository;
import rs.raf.userservice.services.IService;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IService<User, Long> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> myUser = this.userRepository.findByUsername(username);
        if(!myUser.isPresent()) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }
        return new org.springframework.security.core.userdetails.User(myUser.get().getUsername(), myUser.get().getPassword(),
                myUser.get().getPermissions());
    }

}
