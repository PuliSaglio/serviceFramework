package br.com.musiclink.repository;

import br.com.serviceframework.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserDetailsByEmail(String email);
    User findUserByEmail(String email);
}
