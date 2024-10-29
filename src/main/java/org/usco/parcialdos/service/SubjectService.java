package org.usco.parcialdos.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.usco.parcialdos.model.Subject;
import org.usco.parcialdos.model.UserEntity;
import org.usco.parcialdos.repository.SubjectRepository;
import org.usco.parcialdos.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public Subject createSubject(String subjectName, String subjectDescription, Integer subjectClassroom, LocalTime startTime, LocalTime endTime, Long teacherId) {
        UserEntity teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + teacherId + " not found"));

        Subject newSubject = new Subject();
        newSubject.setSubjectName(subjectName);
        newSubject.setSubjectDescription(subjectDescription);
        newSubject.setSubjectClassroom(subjectClassroom);
        newSubject.setStartTime(startTime);
        newSubject.setEndTime(endTime);
        newSubject.setTeacher(teacher);

        return subjectRepository.save(newSubject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public void deleteSubject(Long subjectId) {
        subjectRepository.deleteById(subjectId);
    }

    public Subject getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Subject with id " + subjectId + " not found"));
    }

    public void updateSubject(Long id, Subject updatedSubject) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject with id " + id + " not found"));

        existingSubject.setSubjectName(updatedSubject.getSubjectName());
        existingSubject.setSubjectDescription(updatedSubject.getSubjectDescription());
        existingSubject.setSubjectClassroom(updatedSubject.getSubjectClassroom());
        existingSubject.setStartTime(updatedSubject.getStartTime());
        existingSubject.setEndTime(updatedSubject.getEndTime());
        existingSubject.setTeacher(updatedSubject.getTeacher());

        subjectRepository.save(existingSubject);
    }
}
