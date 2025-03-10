package ru.anoshindanil.accountservice.service;

import ru.anoshindanil.accountservice.entity.User;

public interface UserService {
    void register(User user);
    User findByUsername(String username);
}
