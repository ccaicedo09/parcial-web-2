package org.usco.parcialdos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.usco.parcialdos.model.Subject;
import org.usco.parcialdos.model.UserEntity;
import org.usco.parcialdos.service.SubjectService;
import org.usco.parcialdos.service.UserService;

import java.nio.file.AccessDeniedException;
import java.time.LocalTime;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Tag(name = "Materias", description = "Controlador para gestionar materias")
public class SubjectController {
    private final SubjectService subjectService;
    private final UserService userService;

    @GetMapping("/new")
    @Operation(summary = "Mostrar formulario para crear una nueva materia")
    public String showSubjectForm(Model model) {
        Set<UserEntity> teachers = userService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "subject-form";
    }

    @PostMapping("/create-subject")
    @Operation(summary = "Crear una nueva materia")
    public String createSubject(@RequestParam("subjectName") String subjectName,
                                @RequestParam("subjectDescription") String subjectDescription,
                                @RequestParam("subjectClassroom") Integer subjectClassroom,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime,
                                @RequestParam("teacherId") Long teacherId,
                                Model model) {

        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            Subject newSubject = subjectService.createSubject(subjectName, subjectDescription, subjectClassroom, start, end, teacherId);
            model.addAttribute("subject", newSubject);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/home")
    @Operation(summary = "Mostrar la lista de materias")
    public String showHome(Model model, @AuthenticationPrincipal UserEntity loggedUser) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("loggedUser", loggedUser);
        return "home";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Eliminar una materia por ID")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Mostrar formulario para editar una materia")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subject", subject);
        model.addAttribute("teachers", userService.getAllTeachers());
        return "edit-form";
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "Actualizar una materia por ID")
    public String updateSubject(@Parameter(description = "ID de la materia a actualizar", required = true)
                                    @PathVariable("id") Long subjectId,
                                @Parameter(description = "Nombre de la materia", required = true) @RequestParam("subjectName") String subjectName,
                                @Parameter(description = "Descripción de la materia", required = true) @RequestParam("subjectDescription") String subjectDescription,
                                @Parameter(description = "Número del aula", required = true) @RequestParam("subjectClassroom") Integer subjectClassroom,
                                @Parameter(description = "Hora de inicio en formato HH:MM", required = true) @RequestParam("startTime") String startTime,
                                @Parameter(description = "Hora de fin en formato HH:MM", required = true) @RequestParam("endTime") String endTime,
                                @Parameter(description = "ID del profesor", required = true) @RequestParam("teacherId") Long teacherId) {

        UserEntity teacher = userService.getUserById(teacherId);

        Subject updatedSubject = new Subject();
        updatedSubject.setSubjectName(subjectName);
        updatedSubject.setSubjectDescription(subjectDescription);
        updatedSubject.setSubjectClassroom(subjectClassroom);
        updatedSubject.setStartTime(LocalTime.parse(startTime));
        updatedSubject.setEndTime(LocalTime.parse(endTime));
        updatedSubject.setTeacher(teacher);

        subjectService.updateSubject(subjectId, updatedSubject);

        return "redirect:/home";
    }

    @GetMapping("/change-time/{id}")
    public String showChangeTimeForm(@PathVariable("id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subject", subject);
        return "change-time-form";
    }

    @PostMapping("/update-time/{id}")
    @Operation(summary = "Actualizar el horario de una materia por ID")
    public String updateSubjectTime(
            @Parameter(description = "ID de la materia a actualizar", required = true) @PathVariable("id") Long subjectId,
            @Parameter(description = "Hora de inicio en formato HH:MM", required = true) @RequestParam("startTime") String startTime,
            @Parameter(description = "Hora de fin en formato HH:MM", required = true) @RequestParam("endTime") String endTime,
            @AuthenticationPrincipal UserEntity loggedUser) throws AccessDeniedException { // Para obtener el usuario logueado

        Subject subject = subjectService.getSubjectById(subjectId);

        // Verificar si el usuario es el profesor de la materia
        if (!loggedUser.getRoles().stream().anyMatch(role -> role.getName().equals("RECTOR")) && !subject.getTeacher().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para cambiar el horario de esta materia.");
        }

        // Actualizar los horarios
        subject.setStartTime(LocalTime.parse(startTime));
        subject.setEndTime(LocalTime.parse(endTime));
        subjectService.updateSubject(subjectId, subject);

        return "redirect:/home";
    }
}
