package org.usco.parcialdos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usco.parcialdos.model.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Set<UserEntity> findByRolesName(String roleName);
}
