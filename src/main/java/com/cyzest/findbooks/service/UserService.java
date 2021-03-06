package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.User;
import com.cyzest.findbooks.dao.UserRepository;
import com.cyzest.findbooks.model.DefaultAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registerUser(String id, String password) throws Exception {

        if (userRepository.existsById(id)) {
            throw new IllegalAccessException();
        }

        User user = new User();

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegDate(LocalDateTime.now());

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String id) {
        return userRepository.findById(id)
                .map(DefaultAuthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(id + " not found"));
    }

}
