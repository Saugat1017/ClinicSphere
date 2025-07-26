# 🏥 Smart Clinic Management System - Complete Setup Guide

## 🎯 Overview

This guide will help you set up the complete Smart Clinic Management System with:

- **Spring Boot Backend** (Java 21, MySQL, MongoDB)
- **React Frontend** (Modern UI with all API integrations)

## 📋 Prerequisites

### Required Software

- **Java 21** - [Download here](https://adoptium.net/)
- **Node.js 16+** - [Download here](https://nodejs.org/)
- **MySQL 8.0+** - [Download here](https://dev.mysql.com/downloads/)
- **MongoDB 7.0+** - [Download here](https://www.mongodb.com/try/download/community)

### For macOS (using Homebrew)

```bash
# Install Java 21
brew install openjdk@21

# Install Node.js
brew install node

# Install MySQL
brew install mysql

# Install MongoDB
brew tap mongodb/brew
brew install mongodb-community@7.0
```

## 🚀 Quick Start (Automated)

### 1. **Start Backend**

```bash
# Set up databases and start Spring Boot
./setup-mysql.sh
cd app
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export MONGODB_URI=mongodb://localhost:27017/prescriptions
export JWT_SECRET=your-super-secret-jwt-key
mvn spring-boot:run
```

### 2. **Start Frontend**

```bash
# In a new terminal
./start-frontend.sh
```

## 🔧 Manual Setup (Step by Step)

### Step 1: Database Setup

#### MySQL Setup

```bash
# Start MySQL service
brew services start mysql

# Secure MySQL (set password)
mysql_secure_installation

# Create database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS cms;"
```

#### MongoDB Setup

```bash
# Start MongoDB service
brew services start mongodb/brew/mongodb-community@7.0

# Verify MongoDB is running
mongosh --eval "db.runCommand('ping')"
```

### Step 2: Backend Setup

```bash
# Navigate to app directory
cd app

# Set environment variables
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export MONGODB_URI=mongodb://localhost:27017/prescriptions
export JWT_SECRET=your-super-secret-jwt-key-change-this-in-production

# Build and run
mvn clean compile
mvn spring-boot:run
```

**Backend will be available at:** `http://localhost:8085`

### Step 3: Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

**Frontend will be available at:** `http://localhost:3000`

## 🎯 Testing the System

### 1. **Health Check**

```bash
curl http://localhost:8085/health
```

### 2. **Frontend Access**

Open `http://localhost:3000` in your browser

### 3. **Demo Credentials**

- **Patient**: `patient@example.com` / `password123`
- **Doctor**: `doctor@clinic.com` / `password123`
- **Admin**: `admin` / `admin123`

## 📊 System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend│    │  Spring Boot    │    │   Databases     │
│   (Port 3000)   │◄──►│   (Port 8085)   │◄──►│  MySQL + MongoDB│
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔗 API Endpoints

### Authentication

- `POST /patient/login` - Patient login
- `POST /doctor/login` - Doctor login
- `POST /admin/login` - Admin login
- `POST /patient/register` - Patient registration
- `POST /doctor/register` - Doctor registration

### Patient APIs

- `GET /patient/me/{token}` - Get profile
- `GET /patient/appointments/{token}` - Get appointments
- `GET /patient/appointments/filter/*` - Filter appointments

### Doctor APIs

- `GET /doctor/all` - Get all doctors
- `GET /doctor/appointments/{token}` - Get appointments
- `PUT /appointment/{id}` - Update appointment

### Admin APIs

- `GET /admin/patients` - Get all patients
- `GET /admin/doctors` - Get all doctors
- `GET /admin/appointments` - Get all appointments
- `DELETE /admin/*` - Delete operations

### Health Check

- `GET /health` - System health status

## 🎨 Frontend Features

### Patient Dashboard

- ✅ View all appointments
- ✅ Book new appointments
- ✅ Filter appointments by condition
- ✅ View doctor availability
- ✅ Real-time updates

### Doctor Dashboard

- ✅ View patient appointments
- ✅ Update appointment status
- ✅ Create prescriptions
- ✅ View appointment history
- ✅ Patient information display

### Admin Dashboard

- ✅ Manage all patients, doctors, appointments
- ✅ System statistics overview
- ✅ Delete operations
- ✅ Tabbed interface for organization

### Login System

- ✅ Multi-user type selection
- ✅ Form validation
- ✅ Password visibility toggle
- ✅ Demo credentials display
- ✅ JWT token management

## 🔐 Security Features

- **Password Hashing**: BCrypt encryption
- **JWT Authentication**: Token-based security
- **Input Validation**: Client and server-side validation
- **CORS Configuration**: Secure frontend integration
- **Environment Variables**: No hardcoded secrets

## 🛠️ Development

### Backend Development

```bash
cd app
mvn spring-boot:run
```

### Frontend Development

```bash
cd frontend
npm start
```

### Database Management

```bash
# MySQL
mysql -u root -p cms

# MongoDB
mongosh
use prescriptions
```

## 📁 Project Structure

```
smart-clinic-system/
├── app/                          # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/project/back_end/
│   │       ├── controllers/      # REST Controllers
│   │       ├── models/          # Entity Models
│   │       ├── services/        # Business Logic
│   │       ├── repo/            # Data Access
│   │       └── config/          # Configuration
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                     # React Frontend
│   ├── src/
│   │   ├── components/          # React Components
│   │   ├── context/             # React Context
│   │   ├── services/            # API Services
│   │   └── App.js
│   ├── public/
│   └── package.json
├── setup-mysql.sh               # MySQL setup script
├── start-frontend.sh            # Frontend start script
└── README.md
```

## 🚨 Troubleshooting

### Common Issues

#### 1. **MySQL Connection Error**

```bash
# Error: "Public Key Retrieval is not allowed"
# Solution: Add to application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/cms?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

#### 2. **MongoDB Connection Error**

```bash
# Error: "Connection refused"
# Solution: Start MongoDB service
brew services start mongodb/brew/mongodb-community@7.0
```

#### 3. **Frontend Build Error**

```bash
# Error: "Module not found"
# Solution: Install dependencies
cd frontend
npm install
```

#### 4. **Port Already in Use**

```bash
# Check what's using the port
lsof -i :8085  # Backend
lsof -i :3000  # Frontend

# Kill the process
kill -9 <PID>
```

#### 5. **JWT Token Issues**

```bash
# Clear browser storage
# Or check JWT_SECRET environment variable
echo $JWT_SECRET
```

### Logs and Debugging

#### Backend Logs

```bash
# View Spring Boot logs
cd app
mvn spring-boot:run

# Check application.properties for logging level
logging.level.com.project.back_end=DEBUG
```

#### Frontend Logs

```bash
# View React logs in browser console
# Or check terminal where npm start is running
```

#### Database Logs

```bash
# MySQL logs
tail -f /opt/homebrew/var/log/mysql/mysql.log

# MongoDB logs
tail -f /opt/homebrew/var/log/mongodb/mongo.log
```

## 🎉 Success Indicators

### Backend Success

- ✅ Spring Boot starts without errors
- ✅ Database tables created automatically
- ✅ Health check returns: `{"status":"UP"}`
- ✅ No compilation errors

### Frontend Success

- ✅ React app opens at `http://localhost:3000`
- ✅ Login form displays with user type selection
- ✅ Can log in with demo credentials
- ✅ Dashboard loads with data

### Database Success

- ✅ MySQL connection established
- ✅ MongoDB connection established
- ✅ Tables/collections created automatically
- ✅ No connection errors in logs

## 🔄 Updates and Maintenance

### Backend Updates

```bash
cd app
git pull
mvn clean compile
mvn spring-boot:run
```

### Frontend Updates

```bash
cd frontend
git pull
npm install
npm start
```

### Database Backups

```bash
# MySQL backup
mysqldump -u root -p cms > backup.sql

# MongoDB backup
mongodump --db prescriptions --out backup/
```

## 📞 Support

If you encounter issues:

1. **Check the logs** for error messages
2. **Verify prerequisites** are installed correctly
3. **Ensure databases** are running
4. **Check environment variables** are set
5. **Restart services** if needed

## 🎯 Next Steps

After successful setup:

1. **Explore the dashboards** with different user types
2. **Create test data** by registering new users
3. **Test all features** (appointments, prescriptions, etc.)
4. **Customize the system** for your needs
5. **Deploy to production** when ready

---

**🎉 Congratulations! Your Smart Clinic Management System is now fully operational!**
