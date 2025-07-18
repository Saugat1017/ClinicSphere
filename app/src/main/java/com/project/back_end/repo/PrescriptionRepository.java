package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marks this interface as a Spring Data MongoDB repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {


    // Find all prescriptions linked to a specific appointment ID
    List<Prescription> findByAppointmentId(Long appointmentId);
}
