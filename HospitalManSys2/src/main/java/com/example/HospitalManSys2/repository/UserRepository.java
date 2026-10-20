package com.example.HospitalManSys2.repository;

import com.example.HospitalManSys2.entity.Role;
import com.example.HospitalManSys2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    Optional<User> findByIdAndRole(Long id, Role role);

    List<User> findByRoleAndActiveTrue(Role role);

}