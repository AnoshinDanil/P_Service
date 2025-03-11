package ru.anoshindanil.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anoshindanil.accountservice.dto.LoginRequestDto;
import ru.anoshindanil.accountservice.dto.TokenResponseDto;
import ru.anoshindanil.accountservice.entity.User;
import ru.anoshindanil.accountservice.security.AuthService;
import ru.anoshindanil.accountservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        User userByUsername = userService.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), userByUsername.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        String token = authService.generateToken(userByUsername.getUsername());
        tokenResponseDto.setToken(token);

        return ResponseEntity.ok().body(tokenResponseDto);
    }
    //метод register(User user) будет сохранять юзера, кодировать пароль и сохранять в базу через UserService
    //чтобы кодировал использовать PasswordEncoder

    //метод login(LoginRequestDto request(username,password)) из базы достаем юзера, иначе кидаем exception. Потом сверяем пароли. Если верные, возвращаю токен в теле запроса
}
