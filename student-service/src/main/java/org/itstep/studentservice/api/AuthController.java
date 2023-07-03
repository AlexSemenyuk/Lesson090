package org.itstep.studentservice.api;

import lombok.RequiredArgsConstructor;
import org.itstep.studentservice.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth/token/")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping
    public String getToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }
}

