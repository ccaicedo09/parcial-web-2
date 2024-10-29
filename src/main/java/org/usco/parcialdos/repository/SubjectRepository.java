package org.usco.parcialdos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usco.parcialdos.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
