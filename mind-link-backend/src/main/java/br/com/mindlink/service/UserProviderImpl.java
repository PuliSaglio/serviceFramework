package br.com.mindlink.service;

import br.com.serviceframework.domain.entity.User;
import br.com.mindlink.repository.UserRepository;
import br.com.serviceframework.service.auth.UserProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProviderImpl implements UserProvider {

    private final UserRepository userRepository;

    public UserProviderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserDetailsByEmail(String email) {
        return Optional.ofNullable(userRepository.findUserDetailsByEmail(email));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findUserByEmail(email));
    }
}
