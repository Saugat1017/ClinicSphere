package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final Service sharedService;


    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(@Valid @RequestBody Prescription prescription,
                                              @PathVariable String token) {

        if (!sharedService.validateToken(token, "doctor")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        ResponseEntity<?> response = prescriptionService.savePrescription(prescription);

        if (response.getStatusCode().is2xxSuccessful()) {
            appointmentService.changeStatus(prescription.getAppointmentId(), 1, token);
        }

        return response;
    }


    @GetMapping("/get/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId,
                                             @PathVariable String token) {

        if (!sharedService.validateToken(token, "doctor")) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}
