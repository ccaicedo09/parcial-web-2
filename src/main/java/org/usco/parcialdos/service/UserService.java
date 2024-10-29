package org.usco.parcialdos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.usco.parcialdos.model.UserEntity;
import org.usco.parcialdos.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Set<UserEntity> getAllTeachers() {
        return userRepository.findByRolesName("DOCENTE");
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
    }
}
