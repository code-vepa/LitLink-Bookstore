package com.codevepa.onlinebookstore.service;

import com.codevepa.onlinebookstore.model.Users;
import com.codevepa.onlinebookstore.repository.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UsersRepo repo;

    public MyUserDetailsService(UsersRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));
        return withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
    }
}
