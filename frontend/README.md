# Smart Clinic Management System - React Frontend

A modern, responsive React frontend for the Smart Clinic Management System that connects to all Spring Boot APIs.

## 🚀 Features

- **Multi-User Authentication**: Patient, Doctor, and Admin login
- **Patient Dashboard**:
  - View appointments
  - Book new appointments
  - Filter appointments by condition
  - View doctor availability
- **Doctor Dashboard**:
  - View patient appointments
  - Update appointment status
  - Create prescriptions
  - View appointment history
- **Admin Dashboard**:
  - Manage all patients, doctors, and appointments
  - View system statistics
  - Delete records
  - Overview of clinic operations

## 🛠️ Tech Stack

- **React 18** - UI Framework
- **React Router** - Navigation
- **Axios** - HTTP client for API calls
- **React Hook Form** - Form handling
- **React Hot Toast** - Notifications
- **Tailwind CSS** - Styling
- **Lucide React** - Icons

## 📦 Installation

1. **Navigate to the frontend directory:**

   ```bash
   cd frontend
   ```

2. **Install dependencies:**

   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm start
   ```

The app will open at `http://localhost:3000`

## 🔧 Configuration

The frontend is configured to connect to the Spring Boot backend at `http://localhost:8085`. This is set in the `package.json` proxy field and can be modified in `src/services/api.js`.

## 🎯 API Integration

The frontend connects to all the following Spring Boot endpoints:

### Authentication

- `POST /patient/login` - Patient login
- `POST /doctor/login` - Doctor login
- `POST /admin/login` - Admin login
- `POST /patient/register` - Patient registration
- `POST /doctor/register` - Doctor registration

### Patient APIs

- `GET /patient/me/{token}` - Get patient profile
- `GET /patient/appointments/{token}` - Get patient appointments
- `GET /patient/appointments/filter/condition/{token}/{condition}` - Filter appointments

### Doctor APIs

- `GET /doctor/all` - Get all doctors
- `GET /doctor/appointments/{token}` - Get doctor appointments
- `PUT /appointment/{id}` - Update appointment status

### Admin APIs

- `GET /admin/patients` - Get all patients
- `GET /admin/doctors` - Get all doctors
- `GET /admin/appointments` - Get all appointments
- `DELETE /admin/patient/{id}` - Delete patient
- `DELETE /admin/doctor/{id}` - Delete doctor
- `DELETE /admin/appointment/{id}` - Delete appointment

### Appointment APIs

- `POST /appointment/create` - Create new appointment
- `GET /appointment/all` - Get all appointments

### Prescription APIs

- `POST /prescription/create` - Create prescription
- `GET /prescription/doctor/{doctorId}` - Get doctor prescriptions

## 🎨 UI Components

### LoginForm

- Multi-user type selection (Patient/Doctor/Admin)
- Form validation
- Password visibility toggle
- Demo credentials display

### PatientDashboard

- Appointment statistics
- Appointment booking modal
- Appointment filtering
- Doctor selection

### DoctorDashboard

- Patient appointment management
- Appointment status updates
- Prescription creation
- Patient information display

### AdminDashboard

- Tabbed interface (Overview/Patients/Doctors/Appointments)
- System statistics
- Record management
- Delete operations

## 🔐 Authentication Flow

1. User selects their role (Patient/Doctor/Admin)
2. Enters credentials
3. JWT token is stored in localStorage
4. User is redirected to appropriate dashboard
5. Token is automatically included in API requests
6. Unauthorized requests redirect to login

## 📱 Responsive Design

The frontend is fully responsive and works on:

- Desktop computers
- Tablets
- Mobile phones

## 🎯 Demo Credentials

The login form includes demo credentials for testing:

- **Patient**: `patient@example.com` / `password123`
- **Doctor**: `doctor@clinic.com` / `password123`
- **Admin**: `admin` / `admin123`

## 🚀 Production Build

To create a production build:

```bash
npm run build
```

This creates an optimized build in the `build` folder.

## 🔧 Development

### Available Scripts

- `npm start` - Start development server
- `npm build` - Create production build
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

### Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── LoginForm.js
│   │   ├── PatientDashboard.js
│   │   ├── DoctorDashboard.js
│   │   └── AdminDashboard.js
│   ├── context/
│   │   └── AuthContext.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── index.js
│   └── index.css
├── package.json
├── tailwind.config.js
└── postcss.config.js
```

## 🌟 Features Highlights

- **Real-time Updates**: Dashboard data refreshes automatically
- **Error Handling**: Comprehensive error messages and fallbacks
- **Loading States**: Smooth loading indicators
- **Form Validation**: Client-side validation with helpful error messages
- **Responsive Design**: Works perfectly on all device sizes
- **Modern UI**: Clean, professional interface with Tailwind CSS
- **Toast Notifications**: User-friendly success/error messages
- **Protected Routes**: Role-based access control
- **JWT Integration**: Secure authentication with automatic token management

## 🔗 Backend Integration

This frontend is designed to work seamlessly with the Spring Boot backend. Make sure the backend is running on `http://localhost:8085` before using the frontend.

For backend setup instructions, see the main project README.
