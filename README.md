# Smart Clinic Management System

A comprehensive clinic management system built with Spring Boot and modern web technologies.

## 🏥 Features

- **Patient Management**: Register, login, and manage patient profiles
- **Doctor Management**: Manage doctor profiles and specialties
- **Appointment Scheduling**: Book and manage appointments
- **Prescription Management**: Create and manage prescriptions
- **Admin Dashboard**: Administrative controls and oversight
- **Secure Authentication**: JWT-based authentication system

## 🛠️ Technology Stack

### Backend

- **Spring Boot 3.4.4**: Main framework
- **Spring Data JPA**: Database operations
- **MySQL**: Primary database
- **MongoDB**: For prescription storage
- **Spring Security**: Authentication and authorization
- **JWT**: Token-based authentication
- **Lombok**: Reduce boilerplate code

### Frontend

- **HTML5/CSS3**: Structure and styling
- **Vanilla JavaScript**: Client-side functionality
- **Responsive Design**: Mobile-friendly interface

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+
- Node.js (for development server)

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd okssu-java-database-capstone-template-main
   ```

2. **Set up environment variables**
   Create a `.env` file or set environment variables:

   ```bash
   export DB_USERNAME=your_mysql_username
   export DB_PASSWORD=your_mysql_password
   export JWT_SECRET=your_super_secret_jwt_key
   export MONGODB_URI=mongodb://localhost:27017/prescriptions
   ```

3. **Create MySQL database**

   ```sql
   CREATE DATABASE cms;
   ```

4. **Run the application**

   ```bash
   cd app
   mvn spring-boot:run
   ```

5. **Access the application**
   - Frontend: http://localhost:8085
   - Health Check: http://localhost:8085/health
   - API Base: http://localhost:8085

## 📁 Project Structure

```
app/
├── src/main/java/com/project/back_end/
│   ├── controllers/     # REST API controllers
│   ├── models/         # Entity classes
│   ├── services/       # Business logic
│   ├── repo/          # Data access layer
│   ├── DTO/           # Data Transfer Objects
│   └── config/        # Configuration classes
├── src/main/resources/
│   ├── static/        # Frontend assets
│   └── templates/     # Thymeleaf templates
```

## 🔐 Security Features

- **Password Hashing**: BCrypt encryption for passwords
- **JWT Authentication**: Secure token-based authentication
- **Input Validation**: Comprehensive validation using Jakarta Validation
- **CORS Configuration**: Proper cross-origin resource sharing setup

## 📋 API Endpoints

### Patient Endpoints

- `POST /patient/register` - Register new patient
- `POST /patient/login` - Patient login
- `GET /patient/me/{token}` - Get patient details
- `GET /patient/appointments/{token}` - Get patient appointments

### Doctor Endpoints

- `POST /doctor/register` - Register new doctor
- `POST /doctor/login` - Doctor login
- `GET /doctor/appointments/{token}` - Get doctor appointments

### Admin Endpoints

- `POST /admin/login` - Admin login
- `GET /admin/dashboard/{token}` - Admin dashboard

### Health Check

- `GET /health` - Application health status

## 🧪 Testing

Run tests with Maven:

```bash
mvn test
```

## 🔧 Configuration

Key configuration properties in `application.properties`:

- Database connection settings
- JWT secret and expiration
- Server port (8085)
- CORS origins

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🤝 Support

For support and questions, please open an issue in the repository.
