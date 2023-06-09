package com.example.sbb2.user.service;

import com.example.sbb2.DataNotFoundException;
import com.example.sbb2.user.entity.SiteUser;
import com.example.sbb2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(String username, String password, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(siteUser);
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = userRepository.findByUsername(username);
        if(siteUser.isPresent())
            return siteUser.get();
        else
            throw new DataNotFoundException("user not found");
    }
}
