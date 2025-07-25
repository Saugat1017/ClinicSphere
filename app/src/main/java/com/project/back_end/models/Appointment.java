package com.project.back_end.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.persistence.Transient;
import org.jetbrains.annotations.Contract;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull(message = "Doctor is required")
  private Doctor doctor;

  @ManyToOne
  @NotNull(message = "Patient is required")
  private Patient patient;

  @Future(message = "Appointment time must be in the future")
  private LocalDateTime appointmentTime;

  @NotNull(message = "Status is required")
  private int status;

  @org.jetbrains.annotations.NotNull
  @Contract(pure = true)
  @Transient
  private LocalDateTime getEndTime() {
    return appointmentTime.plusHours(1);
  }

  @Transient
  private LocalDate getAppointmentDate() {
    return appointmentTime.toLocalDate();
  }

  @Transient
  private LocalTime getAppointmentTimeOnly() {
    return appointmentTime.toLocalTime();
  }



}

