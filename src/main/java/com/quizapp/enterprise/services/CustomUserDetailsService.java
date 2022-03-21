package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.CustomUserDetails;
import com.quizapp.enterprise.models.User;
import com.quizapp.enterprise.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find user with that email.");
        }

        return new CustomUserDetails(user);
    }
}
