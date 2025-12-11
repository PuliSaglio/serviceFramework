package br.com.serviceframework.service.auth;

import br.com.serviceframework.domain.entity.User;

public interface TokenService {
    String generateToken(User user);
}
