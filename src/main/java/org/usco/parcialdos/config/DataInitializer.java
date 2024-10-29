package org.usco.parcialdos.config;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.usco.parcialdos.model.Role;
import org.usco.parcialdos.model.UserEntity;
import org.usco.parcialdos.repository.RoleRepository;
import org.usco.parcialdos.repository.UserRepository;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role rector = roleRepository.findByName("RECTOR")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Role docente = roleRepository.findByName("DOCENTE")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Role estudiante = roleRepository.findByName("ESTUDIANTE")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        UserEntity rectorUser = UserEntity.builder()
                .username("nidia")
                .password(passwordEncoder.encode("rectora"))
                .name("Nidia Guzmán")
                .roles(Set.of(rector))
                .build();

        userRepository.save(rectorUser);

        UserEntity docenteUser1 = UserEntity.builder()
                .username("docente1")
                .password(passwordEncoder.encode("docente1"))
                .name("Docente 1")
                .roles(Set.of(docente))
                .build();

        userRepository.save(docenteUser1);

        UserEntity docenteUser2 = UserEntity.builder()
                .username("docente2")
                .password(passwordEncoder.encode("docente2"))
                .name("Docente 2")
                .roles(Set.of(docente))
                .build();

        userRepository.save(docenteUser2);

        UserEntity docenteUser3 = UserEntity.builder()
                .username("docente3")
                .password(passwordEncoder.encode("docente3"))
                .name("Docente 3")
                .roles(Set.of(docente))
                .build();

        userRepository.save(docenteUser3);

        UserEntity estudianteUser1 = UserEntity.builder()
                .username("estudiante1")
                .password(passwordEncoder.encode("estudiante1"))
                .name("Estudiante 1")
                .roles(Set.of(estudiante))
                .build();

        userRepository.save(estudianteUser1);
    }
}
