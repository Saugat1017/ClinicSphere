package com.project.back_end.controllers;

import com.project.back_end.DTO.AppointmentDTO;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final Service sharedService;

    // 1. Patient Registration
    @PostMapping("/register")
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
        // Check if already exists by email or phone
        if (patientService.getAllPatients().stream()
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(patient.getEmail()) ||
                        p.getPhone().equalsIgnoreCase(patient.getPhone()))) {
            return ResponseEntity.status(409).body("Patient already exists");
        }

        int result = patientService.createPatient(patient);
        return result == 1
                ? ResponseEntity.status(201).body("Patient registered successfully")
                : ResponseEntity.status(500).body("Failed to register patient");
    }

    // 2. Patient Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return sharedService.validatePatientLogin(login.getEmail(), login.getPassword());
    }

    // 3. Get Patient Details
    @GetMapping("/me/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        Patient patient = patientService.getPatientDetails(token);
        return ResponseEntity.ok(patient);
    }

    // 4. Get All Appointments of Logged-in Patient
    @GetMapping("/appointments/{token}")
    public ResponseEntity<?> getPatientAppointments(@PathVariable String token) {
        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        List<AppointmentDTO> appointments = patientService.getPatientAppointments(token);
        return ResponseEntity.ok(appointments);
    }

    // 5. Filter Appointments by Condition (past or future)
    @GetMapping("/appointments/filter/condition/{token}/{condition}")
    public ResponseEntity<?> filterByCondition(
            @PathVariable String token,
            @PathVariable String condition) {

        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        List<AppointmentDTO> result = patientService.filterByCondition(token, condition);
        return ResponseEntity.ok(result);
    }

    // 6. Filter Appointments by Doctor
    @GetMapping("/appointments/filter/doctor/{token}/{name}")
    public ResponseEntity<?> filterByDoctor(
            @PathVariable String token,
            @PathVariable String name) {

        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        List<AppointmentDTO> result = patientService.filterByDoctor(token, name);
        return ResponseEntity.ok(result);
    }

    // 7. Filter by Both Doctor and Condition
    @GetMapping("/appointments/filter/{token}/{doctorName}/{condition}")
    public ResponseEntity<?> filterByDoctorAndCondition(
            @PathVariable String token,
            @PathVariable String doctorName,
            @PathVariable String condition) {

        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        List<AppointmentDTO> result = patientService.filterByDoctorAndCondition(token, doctorName, condition);
        return ResponseEntity.ok(result);
    }
}
