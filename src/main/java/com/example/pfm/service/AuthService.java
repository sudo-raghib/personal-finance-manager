package com.example.pfm.service;

import com.example.pfm.dto.auth.RegisterRequest;
import com.example.pfm.dto.auth.MessageResponse;
import com.example.pfm.entity.User;
import com.example.pfm.exceptions.ConflictException;
import com.example.pfm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder enc;

    public MessageResponse register(RegisterRequest req){
        if(repo.findByEmail(req.username()).isPresent())
            throw new ConflictException("Email already registered");
        User u = User.builder()
                .email(req.username())
                .password(enc.encode(req.password()))
                .fullName(req.fullName())
                .phoneNumber(req.phoneNumber())
                .build();
        repo.save(u);
        return new MessageResponse("User registered successfully", u.getId());
    }
}
