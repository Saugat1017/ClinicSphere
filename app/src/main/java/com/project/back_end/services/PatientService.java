package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.DTO.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // 1. Create a new patient
    @Transactional
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 2. Get appointments for current patient
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getPatientAppointments(String token) {
        try {
            Patient patient = getCurrentPatient(token);
            List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
            return convertToDTOs(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 3. Filter appointments by condition ("past" or "future")
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByCondition(String token, String condition) {
        try {
            Patient patient = getCurrentPatient(token);
            int status = condition.equalsIgnoreCase("past") ? 1 : 0;
            List<Appointment> appointments = appointmentRepository
                    .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patient.getId(), status);
            return convertToDTOs(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 4. Filter appointments by doctor name
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctor(String token, String doctorName) {
        try {
            Patient patient = getCurrentPatient(token);
            List<Appointment> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientId(doctorName, patient.getId());
            return convertToDTOs(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 5. Filter by doctor and condition
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctorAndCondition(String token, String doctorName, String condition) {
        try {
            Patient patient = getCurrentPatient(token);
            int status = condition.equalsIgnoreCase("past") ? 1 : 0;
            List<Appointment> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientIdAndStatus(doctorName, patient.getId(), status);
            return convertToDTOs(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 6. Get current patient info
    public Patient getPatientDetails(String token) {
        return getCurrentPatient(token);
    }

    // 🔐 Private helper — extract patient from token
    private Patient getCurrentPatient(String token) {
        String email = tokenService.extractEmail(token);
        return patientRepository.findByEmail(email);
    }

    // 🔁 Convert list of Appointment to AppointmentDTO
    private List<AppointmentDTO> convertToDTOs(List<Appointment> appointments) {
        return appointments.stream().map(appointment -> AppointmentDTO.builder()
                .id(appointment.getId())
                .doctorName(appointment.getDoctor().getName())
                .status(appointment.getStatus())
                .build()).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
