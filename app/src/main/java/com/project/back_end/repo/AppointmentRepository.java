package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository  // 4. Marks this interface as a Spring Data repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 2. Custom Query Methods:

    // Find all appointments for a doctor within a specific time range
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // Find appointments by doctor and patient name (case-insensitive) within a time range
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);

    // Delete all appointments for a specific doctor
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    // Find all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // Find appointments by patient ID and status, ordered by appointment time
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Custom query using LIKE to filter by doctor's name and patient ID
    @Query("SELECT a FROM Appointment a WHERE a.doctor.name LIKE %:doctorName% AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    // Custom query using LIKE, patient ID, and appointment status
    @Query("SELECT a FROM Appointment a WHERE a.doctor.name LIKE %:doctorName% AND a.patient.id = :patientId AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);

    // Update the status of a specific appointment
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(int status, long id);
}
