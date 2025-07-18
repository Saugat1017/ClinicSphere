package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // 3. Marks this interface as a Spring Data JPA repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // 2. Custom Query Methods:

    // Find a patient by their email
    Patient findByEmail(String email);

    // Find a patient by either email or phone number
    Patient findByEmailOrPhone(String email, String phone);
}
