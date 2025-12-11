package br.com.mindlink.security;

import br.com.mindlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserDetailsByEmail(email);

        String senhaHash = (user != null) ? user.getPassword() : "USUÁRIO NÃO ENCONTRADO";

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado " + email);
        }

        return user;
    }
}
