package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import com.project.back_end.DTO.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final Service sharedService;

    // 1. Check doctor's availability on a specific date
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token
    ) {
        if (!sharedService.validateToken(token, user)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        List<LocalTime> availability = doctorService.getDoctorAvailability(doctorId, date);
        return ResponseEntity.ok(availability);
    }

    // 2. Get all doctors
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorService.getDoctors());
        return ResponseEntity.ok(response);
    }

    // 3. Save/register a new doctor (admin only)
    @PostMapping("/save/{token}")
    public ResponseEntity<?> saveDoctor(
            @PathVariable String token,
            @Valid @RequestBody Doctor doctor
    ) {
        if (!sharedService.validateToken(token, "admin")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int result = doctorService.saveDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.status(201).body("Doctor saved successfully");
            case -1 -> ResponseEntity.status(409).body("Doctor already exists");
            default -> ResponseEntity.status(500).body("Error saving doctor");
        };
    }

    // 4. Doctor login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@Valid @RequestBody Login loginDTO) {
        String result = doctorService.validateDoctor(loginDTO.getEmail(), loginDTO.getPassword());
        if (result.equals("Invalid email") || result.equals("Invalid password")) {
            return ResponseEntity.status(401).body(result);
        }
        Map<String, String> response = new HashMap<>();
        response.put("token", result);
        return ResponseEntity.ok(response);
    }

    // 5. Update doctor info (admin only)
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable String token,
            @Valid @RequestBody Doctor doctor
    ) {
        if (!sharedService.validateToken(token, "admin")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int result = doctorService.updateDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.ok("Doctor updated successfully");
            case -1 -> ResponseEntity.status(404).body("Doctor not found");
            default -> ResponseEntity.status(500).body("Update failed");
        };
    }

    // 6. Delete doctor by ID (admin only)
    @DeleteMapping("/delete/{doctorId}/{token}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token
    ) {
        if (!sharedService.validateToken(token, "admin")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int result = doctorService.deleteDoctor(doctorId);
        return switch (result) {
            case 1 -> ResponseEntity.ok("Doctor deleted successfully");
            case -1 -> ResponseEntity.status(404).body("Doctor not found");
            default -> ResponseEntity.status(500).body("Deletion failed");
        };
    }

    // 7. Filter doctors by name and specialty
    @GetMapping("/filter")
    public ResponseEntity<?> filterDoctors(
            @RequestParam String name,
            @RequestParam String specialty
    ) {
        List<Doctor> results = doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        return ResponseEntity.ok(results);
    }
}
