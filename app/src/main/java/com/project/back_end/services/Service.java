package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;

    public boolean validateToken(String token, String username) {
        return tokenService.validateToken(token, username);
    }

    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(401).body("Admin not found");
            }

            if (!admin.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            String token = tokenService.generateToken(username);
            return ResponseEntity.ok().body(token);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public List<Doctor> filterDoctor(String name, String specialty, LocalTime time) {
        List<Doctor> doctors = doctorService.getDoctors();

        return doctors.stream()
                .filter(d -> (name == null || d.getName().toLowerCase().contains(name.toLowerCase())) &&
                        (specialty == null || d.getSpecialty().equalsIgnoreCase(specialty)) &&
                        (time == null || d.getAvailableTimes().contains(time)))
                .toList();
    }

    public int validateAppointment(Long doctorId, LocalDate date, LocalTime requestedTime) {
        try {
            Doctor doctor = doctorService.getDoctors()
                    .stream()
                    .filter(d -> d.getId().equals(doctorId))
                    .findFirst()
                    .orElse(null);

            if (doctor == null)
                return -1;

            List<LocalTime> availableSlots = doctorService.getDoctorAvailability(doctorId, date);
            return availableSlots.contains(requestedTime) ? 1 : 0;

        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<?> validatePatientLogin(String email, String password) {
        try {
            if (patientService.verifyPatientLogin(email, password)) {
                String token = tokenService.generateToken(email);
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public List<Appointment> filterPatient(String token, String condition, String doctorName) {
        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(token, doctorName, condition)
                    .stream().map(dto -> {
                        Appointment a = new Appointment();
                        a.setId(dto.getId());
                        a.setAppointmentTime(dto.getAppointmentTime());
                        a.setStatus(dto.getStatus());
                        return a;
                    }).toList();
        } else if (condition != null) {
            return patientService.filterByCondition(token, condition)
                    .stream().map(dto -> {
                        Appointment a = new Appointment();
                        a.setId(dto.getId());
                        a.setAppointmentTime(dto.getAppointmentTime());
                        a.setStatus(dto.getStatus());
                        return a;
                    }).toList();
        } else if (doctorName != null) {
            return patientService.filterByDoctor(token, doctorName)
                    .stream().map(dto -> {
                        Appointment a = new Appointment();
                        a.setId(dto.getId());
                        a.setAppointmentTime(dto.getAppointmentTime());
                        a.setStatus(dto.getStatus());
                        return a;
                    }).toList();
        } else {
            return patientService.getPatientAppointments(token)
                    .stream().map(dto -> {
                        Appointment a = new Appointment();
                        a.setId(dto.getId());
                        a.setAppointmentTime(dto.getAppointmentTime());
                        a.setStatus(dto.getStatus());
                        return a;
                    }).toList();
        }
    }
}
