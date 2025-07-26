package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Name is required")
  @Size(min = 3, max = 100)
  private String name;

  @NotNull(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotNull(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters long")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotNull(message = "Phone is required")
  @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
  private String phone;

  @NotNull(message = "Address is required")
  @Size(max = 255)
  private String address;

}
