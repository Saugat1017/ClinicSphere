# Database Schema Design

## Overview

The Smart Clinic Management System uses a hybrid database approach:

- **MySQL**: Primary database for user management and appointments
- **MongoDB**: Document storage for prescriptions

## MySQL Schema

### Patient Table

```sql
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    address VARCHAR(255) NOT NULL
);
```

### Doctor Table

```sql
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(10) NOT NULL
);

CREATE TABLE doctor_available_times (
    doctor_id BIGINT,
    available_times TIME,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);
```

### Appointment Table

```sql
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status INT NOT NULL DEFAULT 0,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);
```

### Admin Table

```sql
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
```

## MongoDB Collections

### Prescriptions Collection

```json
{
  "_id": "ObjectId",
  "patientId": "Long",
  "doctorId": "Long",
  "medications": [
    {
      "name": "String",
      "dosage": "String",
      "frequency": "String",
      "duration": "String"
    }
  ],
  "instructions": "String",
  "prescribedDate": "Date",
  "validUntil": "Date"
}
```

## Relationships

1. **Patient ↔ Appointment**: One-to-Many

   - A patient can have multiple appointments
   - Each appointment belongs to one patient

2. **Doctor ↔ Appointment**: One-to-Many

   - A doctor can have multiple appointments
   - Each appointment belongs to one doctor

3. **Doctor ↔ Available Times**: One-to-Many
   - A doctor can have multiple available time slots
   - Each time slot belongs to one doctor

## Indexes

### Recommended Indexes

```sql
-- Patient table
CREATE INDEX idx_patient_email ON patient(email);

-- Doctor table
CREATE INDEX idx_doctor_email ON doctor(email);
CREATE INDEX idx_doctor_specialty ON doctor(specialty);

-- Appointment table
CREATE INDEX idx_appointment_doctor ON appointment(doctor_id);
CREATE INDEX idx_appointment_patient ON appointment(patient_id);
CREATE INDEX idx_appointment_time ON appointment(appointment_time);
CREATE INDEX idx_appointment_status ON appointment(status);
```

## Data Validation Rules

### Patient

- Email must be unique and valid format
- Phone must be exactly 10 digits
- Name must be 3-100 characters
- Password must be at least 6 characters (hashed)

### Doctor

- Email must be unique and valid format
- Phone must be exactly 10 digits
- Specialty must be 3-50 characters
- Available times stored as separate table

### Appointment

- Appointment time must be in the future
- Status: 0 = pending, 1 = completed, 2 = cancelled
- Doctor and patient must exist

## Security Considerations

1. **Password Storage**: All passwords are hashed using BCrypt
2. **JWT Tokens**: Used for authentication with configurable expiration
3. **Input Validation**: All inputs validated using Jakarta Validation
4. **SQL Injection Prevention**: Using JPA/Hibernate with parameterized queries
5. **CORS Configuration**: Properly configured for frontend integration
