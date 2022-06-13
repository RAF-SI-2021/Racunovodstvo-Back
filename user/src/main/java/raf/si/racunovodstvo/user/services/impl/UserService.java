package raf.si.racunovodstvo.user.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.user.constants.RedisConstants;
import raf.si.racunovodstvo.user.model.User;
import raf.si.racunovodstvo.user.repositories.UserRepository;
import raf.si.racunovodstvo.user.services.IService;

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
    @CachePut(value = RedisConstants.USER_CACHE, key = "#result.userId")
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable(value = RedisConstants.USER_CACHE, key = "#id")
    public Optional<User> findById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @CacheEvict(value = RedisConstants.USER_CACHE, key = "#id")
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

    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

}
