package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;



    // 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try{
            appointmentRepository.save(appointment);
            return 1;

        }catch(Exception e){
            return 0;

        }

    }
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedData, Long patientId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) return "Appointment not found";

        Appointment existing = optionalAppointment.get();

        if (!existing.getPatient().getId().equals(patientId)) {
            return "Unauthorized: patient mismatch";
        }

        List<Appointment> overlapping = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                updatedData.getDoctor().getId(),
                updatedData.getAppointmentTime().plusMinutes(30),
                updatedData.getAppointmentTime().plusMinutes(30)
        );
        if(!overlapping.isEmpty()) {
            return "Appointment time is not available";
        }
        existing.setAppointmentTime(updatedData.getAppointmentTime());
        existing.setDoctor(updatedData.getDoctor());
        appointmentRepository.save(existing);
        return "Updated!";
    }

    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) return "Appointment not found";
        Appointment existing = optionalAppointment.get();
       if(!existing.getPatient().getId().equals(patientId)){
           return "Unauthorized: patient mismatch";
       }
       appointmentRepository.delete(existing);
       return "Appointment cancelled!";
    }
    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, LocalDateTime dayStart, LocalDateTime dayEnd, String patientName) {
        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, dayStart, dayEnd
            );
        }
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, dayStart, dayEnd);
    }

    // 8. Change Status
    @Transactional
    public void changeStatus(Long appointmentId, int newStatus) {
        appointmentRepository.updateStatus(newStatus, appointmentId);
    }
}

