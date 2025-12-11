package br.com.serviceframework.service.auth;

import br.com.serviceframework.domain.entity.User;

import java.util.Optional;

public interface UserProvider {
    Optional<User> findUserDetailsByEmail(String email);
    Optional<User> findUserByEmail(String email);
}