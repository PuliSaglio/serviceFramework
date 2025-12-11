package br.com.mindlink.security;

import br.com.serviceframework.domain.DTO.AuthDTO;
import br.com.serviceframework.domain.DTO.AuthResponseDTO;
import br.com.serviceframework.domain.DTO.UserDTO;
import br.com.serviceframework.domain.DTO.UserRegisterDTO;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.domain.enumerations.Perfis;
import br.com.mindlink.domain.entity.ClientePerfil;
import br.com.mindlink.domain.entity.PrestadorPerfil;
import br.com.mindlink.repository.UserRepository;
import br.com.mindlink.service.ClienteServiceImpl;
import br.com.mindlink.service.PrestadorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.mindlink.security.TokenService;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClienteServiceImpl clienteService;

    @Autowired
    private PrestadorServiceImpl prestadorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(AuthDTO authData) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    authData.email(),
                    authData.senha()
            );

            var auth = authenticationManager.authenticate(authToken);

            User user = (User) auth.getPrincipal();

            String token = tokenService.generateToken(user);

            UserDTO userDTO = new UserDTO(user);

            if (user.getPerfil() == Perfis.CLIENTE) {
                Cliente cliente = clienteService.buscarPorUserId(user.getId());
                if (cliente != null && cliente.getPerfilUsuario() != null) {
                    userDTO.setNome(cliente.getPerfilUsuario().getNome());
                    userDTO.setProfileId(cliente.getId());
                }
            } else if (user.getPerfil() == Perfis.PRESTADOR) {
                Prestador prestador = prestadorService.buscarPorUserId(user.getId());
                if (prestador != null && prestador.getPerfilPrestador() != null) {
                    // TODO: Entender por que o nome não está vindo do perfil
                    // userDTO.setNome(prestador.getPerfilPrestador().getNome());
                    userDTO.setProfileId(prestador.getId());
                }
            }

            return new AuthResponseDTO(userDTO, token);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos.");
        }
    }

    public UserDTO registerUser(UserRegisterDTO registerData) {
        try {
            User user = registerData.toEntity();

            validateNewUser(user);

            String encryptedPassword = passwordEncoder.encode(user.getSenha());
            user.setSenha(encryptedPassword);

            userRepository.save(user);

            PrestadorPerfil prestadorPerfil = new PrestadorPerfil();
            prestadorPerfil.setNome(registerData.getNome());
            prestadorPerfil.setCpfCnpj(registerData.getCpfCnpj());
            prestadorPerfil.setTelefone(registerData.getTelefone());

            ClientePerfil clientePerfil = new ClientePerfil();
            clientePerfil.setNome(registerData.getNome());
            clientePerfil.setCpfCnpj(registerData.getCpfCnpj());
            clientePerfil.setTelefone(registerData.getTelefone());

            if (user.getPerfil() == Perfis.CLIENTE) {
                clienteService.criarCliente(user, clientePerfil);
            } else {
                prestadorService.criarPrestador(user, prestadorPerfil);
            }

            return new UserDTO(user);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao registrar usuário.");
        }

    }

    public UserDTO updateUser(User user) {
        validateExistingUser(user);

        String encryptedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encryptedPassword);

        userRepository.save(user);

        return new UserDTO(user);
    }

    private void validateNewUser(User user) throws Exception {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserName é obrigatório.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório.");
        }
        if (user.getSenha() == null || user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }
        if (user.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 6 caracteres.");
        }
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso.");
        }
    //        if (userRepository.findByCpfCnpf(user.getUsername()) != null) {
    //            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserName já está em uso.");
    //        }
        //TODO: Validar CPF/CNPJ único
    }

    private void validateExistingUser(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente.");
        }
        if (user.getSenha() == null || user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }
        if (user.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 6 caracteres.");
        }
    }
}
