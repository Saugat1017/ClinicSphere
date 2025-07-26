Smart Clinic Management System
A simple and complete web application for managing clinics. It helps patients, doctors, and admins with registration, appointments, and prescriptions.

Features
Patient registration and login

Doctor management and login

Appointment booking system

Prescription storage

Admin dashboard for managing the system

Secure login using JWT (token-based authentication)

Technologies Used
Backend
Java 17

Spring Boot

Spring Data JPA

Spring Security

MySQL (for main data)

MongoDB (for storing prescriptions)

JWT for authentication

Lombok (to reduce extra code)

Frontend
HTML and CSS

JavaScript

Designed to work on both desktop and mobile

How to Run the Project
Requirements
Java 17 or higher

MySQL 8 or higher

MongoDB installed

Maven installed

Node.js (only if you’re using a development server for frontend)

Steps
Clone the project

bash
Copy
Edit
git clone <your-repository-link>
cd okssu-java-database-capstone-template-main
Set environment variables

You can create a .env file or manually set these:

ini
Copy
Edit
DB_USERNAME=your_mysql_username  
DB_PASSWORD=your_mysql_password  
JWT_SECRET=your_jwt_secret  
MONGODB_URI=mongodb://localhost:27017/prescriptions
Create a MySQL database

pgsql
Copy
Edit
CREATE DATABASE cms;
Start the backend

arduino
Copy
Edit
cd app
mvn spring-boot:run
Open your browser

Visit the app at http://localhost:8085

API Endpoints
Patient
POST /patient/register - Register new patient

POST /patient/login - Patient login

GET /patient/me/{token} - Get patient profile

GET /patient/appointments/{token} - View appointments

Doctor
POST /doctor/register - Register new doctor

POST /doctor/login - Doctor login

GET /doctor/appointments/{token} - View doctor’s appointments

Admin
POST /admin/login - Admin login

GET /admin/dashboard/{token} - View dashboard

Misc
GET /health - Check if app is running

Project Folder Structure
rust
Copy
Edit
app/
├── controllers/     -> REST API controllers
├── models/          -> Entity classes
├── services/        -> Business logic
├── repo/            -> Repositories for database access
├── DTO/             -> Data transfer objects
├── config/          -> Configuration files
├── static/          -> Frontend files
├── templates/       -> Thymeleaf templates (if used)
Security
Passwords are encrypted using BCrypt

JWT used to protect all endpoints

User input is validated

CORS is configured to allow frontend access

Running Tests
Use this command to run tests:

bash
Copy
Edit
mvn test
Contributing
If you want to help:

Fork the repository

Create a new branch

Make your changes

Push and submit a pull request

License
This project is under the MIT License.

Support
If you have questions or find a bug, feel free to open an issue.