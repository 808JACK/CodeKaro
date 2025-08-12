package com.example.demo.Repo;

import com.example.demo.Entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepo extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User findUserById(Long userId);

    User findUserByEmail(String email);

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
