package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service sharedService;

    // 1. Book an appointment (patient only)
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token, @Valid @RequestBody Appointment appointment) {
        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        int result = appointmentService.bookAppointment(appointment, token);
        return result == 1 ? ResponseEntity.ok("Appointment booked successfully")
                : ResponseEntity.status(500).body("Booking failed");
    }

    // 2. Update an appointment (patient only)
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token, @Valid @RequestBody Appointment appointment) {
        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String result = appointmentService.updateAppointment(appointment.getId(), appointment, token);
        return ResponseEntity.ok(result);
    }

    // 3. Cancel an appointment (patient only)
    @DeleteMapping("/cancel/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        if (!sharedService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String result = appointmentService.cancelAppointment(id, token);
        return ResponseEntity.ok(result);
    }

    // 4. Doctor views appointments (filtered by date and patient name)
    @GetMapping("/view/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String patientName
    ) {
        if (!sharedService.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments = appointmentService.getAppointments(token, start, end, patientName);
        return ResponseEntity.ok(appointments);
    }

    // 5. Doctor changes appointment status (e.g., to mark as completed or cancelled)
    @PutMapping("/status/{id}/{status}/{token}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable int status, @PathVariable String token) {
        if (!sharedService.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        appointmentService.changeStatus(id, status, token);
        return ResponseEntity.ok("Status updated");
    }
}
