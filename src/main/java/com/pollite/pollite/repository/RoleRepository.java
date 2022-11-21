package com.pollite.pollite.repository;

import com.pollite.pollite.model.auth.Role;
import com.pollite.pollite.model.auth.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
