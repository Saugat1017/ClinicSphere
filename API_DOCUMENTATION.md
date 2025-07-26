# API Documentation

## Base URL

`http://localhost:8085`

## Authentication

All protected endpoints require a JWT token in the URL path parameter `{token}`.

## Endpoints

### Health Check

- **GET** `/health`
- **Description**: Check application health status
- **Response**: Application status and timestamp

### Patient Endpoints

#### Register Patient

- **POST** `/patient/register`
- **Body**:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "1234567890",
  "address": "123 Main St, City"
}
```

- **Response**: Success/error message

#### Patient Login

- **POST** `/patient/login`
- **Body**:

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

- **Response**: JWT token

#### Get Patient Details

- **GET** `/patient/me/{token}`
- **Response**: Patient information

#### Get Patient Appointments

- **GET** `/patient/appointments/{token}`
- **Response**: List of appointments

#### Filter Appointments by Condition

- **GET** `/patient/appointments/filter/condition/{token}/{condition}`
- **Parameters**: `condition` can be "past" or "future"
- **Response**: Filtered appointments

#### Filter Appointments by Doctor

- **GET** `/patient/appointments/filter/doctor/{token}/{name}`
- **Parameters**: `name` is doctor's name
- **Response**: Filtered appointments

#### Filter by Doctor and Condition

- **GET** `/patient/appointments/filter/{token}/{doctorName}/{condition}`
- **Response**: Filtered appointments

### Doctor Endpoints

#### Register Doctor

- **POST** `/doctor/register`
- **Body**:

```json
{
  "name": "Dr. Smith",
  "specialty": "Cardiology",
  "email": "smith@clinic.com",
  "password": "password123",
  "phone": "1234567890",
  "availableTimes": ["09:00", "10:00", "11:00"]
}
```

#### Doctor Login

- **POST** `/doctor/login`
- **Body**:

```json
{
  "email": "smith@clinic.com",
  "password": "password123"
}
```

### Admin Endpoints

#### Admin Login

- **POST** `/admin/login`
- **Body**:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

## Error Responses

### Validation Error (400)

```json
{
  "fieldName": "Error message"
}
```

### Authentication Error (401)

```json
"Invalid email or password"
```

### Authorization Error (403)

```json
"Invalid or expired token"
```

### Server Error (500)

```json
{
  "error": "An unexpected error occurred",
  "message": "Error details"
}
```

## Data Models

### Patient

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "address": "123 Main St, City"
}
```

### Doctor

```json
{
  "id": 1,
  "name": "Dr. Smith",
  "specialty": "Cardiology",
  "email": "smith@clinic.com",
  "phone": "1234567890",
  "availableTimes": ["09:00", "10:00", "11:00"]
}
```

### Appointment

```json
{
  "id": 1,
  "doctor": {
    "id": 1,
    "name": "Dr. Smith"
  },
  "patient": {
    "id": 1,
    "name": "John Doe"
  },
  "appointmentTime": "2024-01-15T10:00:00",
  "status": 0
}
```

## Status Codes

- `0`: Pending
- `1`: Completed
- `2`: Cancelled
