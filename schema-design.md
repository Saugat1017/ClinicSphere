# Smart Clinic System: Database Schema Design

## MySQL Database Design

### 1. `patients` Table

| Column Name | Data Type         | Constraints                 | Description                        |
| ----------- | ----------------- | --------------------------- | ---------------------------------- |
| patient_id  | INT               | PRIMARY KEY, AUTO_INCREMENT | Unique patient identifier          |
| first_name  | VARCHAR(50)       | NOT NULL                    | Patient's first name               |
| last_name   | VARCHAR(50)       | NOT NULL                    | Patient's last name                |
| dob         | DATE              | NOT NULL                    | Date of birth                      |
| gender      | ENUM('M','F','O') | NOT NULL                    | Gender (M=Male, F=Female, O=Other) |
| email       | VARCHAR(100)      | NOT NULL, UNIQUE            | Patient's email address            |
| phone       | VARCHAR(20)       |                             | Contact number                     |
| address     | VARCHAR(255)      |                             | Home address                       |

### 2. `doctors` Table

| Column Name | Data Type    | Constraints                 | Description              |
| ----------- | ------------ | --------------------------- | ------------------------ |
| doctor_id   | INT          | PRIMARY KEY, AUTO_INCREMENT | Unique doctor identifier |
| first_name  | VARCHAR(50)  | NOT NULL                    | Doctor's first name      |
| last_name   | VARCHAR(50)  | NOT NULL                    | Doctor's last name       |
| specialty   | VARCHAR(100) | NOT NULL                    | Medical specialty        |
| email       | VARCHAR(100) | NOT NULL, UNIQUE            | Doctor's email address   |
| phone       | VARCHAR(20)  |                             | Contact number           |

### 3. `appointments` Table

| Column Name      | Data Type                                 | Constraints                 | Description                       |
| ---------------- | ----------------------------------------- | --------------------------- | --------------------------------- |
| appointment_id   | INT                                       | PRIMARY KEY, AUTO_INCREMENT | Unique appointment identifier     |
| patient_id       | INT                                       | NOT NULL, FOREIGN KEY       | References `patients(patient_id)` |
| doctor_id        | INT                                       | NOT NULL, FOREIGN KEY       | References `doctors(doctor_id)`   |
| appointment_time | DATETIME                                  | NOT NULL                    | Scheduled date and time           |
| status           | ENUM('scheduled','completed','cancelled') | NOT NULL                    | Appointment status                |
| notes            | TEXT                                      |                             | Additional notes                  |

### 4. `admin` Table

| Column Name   | Data Type             | Constraints                 | Description             |
| ------------- | --------------------- | --------------------------- | ----------------------- |
| admin_id      | INT                   | PRIMARY KEY, AUTO_INCREMENT | Unique admin identifier |
| username      | VARCHAR(50)           | NOT NULL, UNIQUE            | Admin username          |
| password_hash | VARCHAR(255)          | NOT NULL                    | Hashed password         |
| email         | VARCHAR(100)          | NOT NULL, UNIQUE            | Admin email address     |
| role          | ENUM('super','staff') | NOT NULL                    | Admin role              |

<!--
Justification:
- Used INT and AUTO_INCREMENT for primary keys for simplicity and performance.
- Used ENUM for fields with limited options (gender, status, role) for data integrity.
- Foreign keys in `appointments` ensure referential integrity.
- Unique constraints on emails and usernames to prevent duplicates.
-->

---

## MongoDB Collection Design

### Collection: `prescriptions`

A prescription is often best modeled as a document because it may contain nested medication lists, dosage instructions, and references to both patient and doctor.

#### Example Document

```json
{
  "_id": "665a1b2c3d4e5f6a7b8c9d0e",
  "patient_id": 101, // Reference to MySQL patients.patient_id
  "doctor_id": 12, // Reference to MySQL doctors.doctor_id
  "date_issued": "2024-06-01T10:30:00Z",
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration": "7 days"
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "frequency": "as needed",
      "duration": "5 days"
    }
  ],
  "notes": "Take with food. Return for follow-up in 1 week.",
  "pharmacy": {
    "name": "City Pharmacy",
    "address": "123 Main St, Springfield"
  }
}
```

<!--
Justification:
- Embedded array for medications allows flexible number of drugs per prescription.
- Nested pharmacy object for extensibility (e.g., contact info).
- patient_id and doctor_id reference relational data, supporting hybrid SQL/NoSQL design.
-->
