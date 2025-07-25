package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    // Book Appointment (patient books for themselves)
    @Transactional
    public int bookAppointment(Appointment appointment, String token) {
        try {
            Patient patient = getCurrentPatient(token);
            appointment.setPatient(patient);
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // Update Appointment (patient can update their own)
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedData, String token) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment existing = optional.get();
        Patient patient = getCurrentPatient(token);

        if (!existing.getPatient().getId().equals(patient.getId())) {
            return "Unauthorized: patient mismatch";
        }

        List<Appointment> overlapping = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                updatedData.getDoctor().getId(),
                updatedData.getAppointmentTime().minusMinutes(30),
                updatedData.getAppointmentTime().plusMinutes(30)
        );
        if (!overlapping.isEmpty()) return "Doctor not available at that time";

        existing.setAppointmentTime(updatedData.getAppointmentTime());
        existing.setDoctor(updatedData.getDoctor());
        appointmentRepository.save(existing);
        return "Updated";
    }

    // Cancel Appointment (patient can cancel their own)
    @Transactional
    public String cancelAppointment(Long appointmentId, String token) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment appointment = optional.get();
        Patient patient = getCurrentPatient(token);

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            return "Unauthorized: patient mismatch";
        }

        appointmentRepository.deleteById(appointmentId);
        return "Cancelled";
    }

    // Doctor sees all their appointments (with optional patient name filter)
    @Transactional
    public List<Appointment> getAppointments(String token, LocalDateTime start, LocalDateTime end, String patientName) {
        Doctor doctor = getCurrentDoctor(token);

        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctor.getId(), patientName, start, end
            );
        }

        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end);
    }

    // Doctor changes appointment status
    @Transactional
    public void changeStatus(Long appointmentId, int newStatus, String token) {
        Doctor doctor = getCurrentDoctor(token);

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isPresent() && optional.get().getDoctor().getId().equals(doctor.getId())) {
            appointmentRepository.updateStatus(newStatus, appointmentId);
        }
    }

    private Patient getCurrentPatient(String token) {
        String email = tokenService.extractEmail(token);
        return patientRepository.findByEmail(email);
    }

    private Doctor getCurrentDoctor(String token) {
        String email = tokenService.extractEmail(token);
        return doctorRepository.findByEmail(email).get(0); // assuming list
    }
}
