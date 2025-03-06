package ru.anoshindanil.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    //метод register(User user) будет сохранять юзера, кодировать пароль и сохранять в базу через UserService
    //чтобы кодировал использовать PasswordEncoder

    //метод login(LoginRequestDto request(username,password)) из базы достаем юзера, иначе кидаем exception. Потом сверяем пароли. Если верные, возвращаю токен в теле запроса
}
