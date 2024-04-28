package com.example.finaldemo.repository;

import  com.example.finaldemo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT  email FROM public.users WHERE user_id = ?1", nativeQuery = true)
    String findUserEmailById(Long id);

    Boolean existsUserByEmail(String email);

    Optional<User> findByEmailContainingIgnoreCase(String email);

    @Query(value = "SELECT * FROM public.users WHERE user_id = ?1", nativeQuery = true)
    Page<User> findUserById(Long userId, Pageable pageable);
}
