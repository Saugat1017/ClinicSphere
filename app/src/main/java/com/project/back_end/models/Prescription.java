package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Document(collection = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

  @Id
  private String id;

  @NotNull(message = "Patient name is required")
  @Size(min = 3, max = 100)
  private String patientName;

  @NotNull(message = "Appointment ID is required")
  private Long appointmentId;

  @NotNull(message = "Medication is required")
  @Size(min = 3, max = 100)
  private String medication;

  @NotNull(message = "Dosage is required")
  @Size(min = 3, max = 100)
  private String dosage;

  @Size(max = 200)
  private String doctorNotes;

  
}
