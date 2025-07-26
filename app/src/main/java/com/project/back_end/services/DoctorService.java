package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Transactional(readOnly = true)
    public List<LocalTime> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, startOfDay, endOfDay);

        Set<LocalTime> booked = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        return doctor.getAvailableTimes().stream()
                .filter(t -> !booked.contains(t))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Optional<Doctor> existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing.isPresent()) {
                return -1; // Already exists
            }

            doctorRepository.save(doctor);
            log.info("Doctor registered: {}", doctor.getEmail());
            return 1;
        } catch (Exception e) {
            log.error("Error saving doctor", e);
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor updatedDoctor) {
        return doctorRepository.findById(updatedDoctor.getId())
                .map(existing -> {
                    existing.setName(updatedDoctor.getName());
                    existing.setEmail(updatedDoctor.getEmail());
                    existing.setSpecialty(updatedDoctor.getSpecialty());
                    existing.setAvailableTimes(updatedDoctor.getAvailableTimes());
                    doctorRepository.save(existing);
                    return 1;
                })
                .orElse(-1);
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId))
            return -1;

        appointmentRepository.deleteAllByDoctorId(doctorId);
        doctorRepository.deleteById(doctorId);
        return 1;
    }

    @Transactional(readOnly = true)
    public String validateDoctor(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty())
            return "Invalid email";

        Doctor doctor = doctorOpt.get();
        if (!doctor.getPassword().equals(password))
            return "Invalid password";

        return tokenService.generateToken(email);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    public Doctor getCurrentDoctor(String token) {
        String email = tokenService.extractEmail(token);
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Doctor not found for token"));
    }
}
