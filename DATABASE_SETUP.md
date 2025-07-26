# Database Setup Guide

## 🗄️ MySQL Setup

### 1. **Install MySQL** (if not already installed)

```bash
brew install mysql
```

### 2. **Start MySQL Service**

```bash
brew services start mysql
```

### 3. **Secure MySQL Installation**

```bash
mysql_secure_installation
```

- Follow the prompts to set a root password
- Answer 'Y' to all security questions

### 4. **Create Database**

```bash
# If you set a password:
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS cms;"

# If no password:
mysql -u root -e "CREATE DATABASE IF NOT EXISTS cms;"
```

### 5. **Verify Database Creation**

```bash
mysql -u root -p -e "SHOW DATABASES;"
```

## 🍃 MongoDB Setup

### 1. **Install MongoDB**

```bash
brew tap mongodb/brew
brew install mongodb-community@7.0
```

### 2. **Start MongoDB Service**

```bash
brew services start mongodb/brew/mongodb-community@7.0
```

### 3. **Verify MongoDB is Running**

```bash
mongosh --eval "db.runCommand('ping')"
```

### 4. **Create Database**

```bash
mongosh
use prescriptions
exit
```

## 🔧 Application Configuration

### 1. **Set Environment Variables**

```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password_here
export MONGODB_URI=mongodb://localhost:27017/prescriptions
export JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
```

### 2. **Update application.properties**

The file is already configured with the correct settings:

- MySQL: `localhost:3306/cms`
- MongoDB: `localhost:27017/prescriptions`

### 3. **Test Database Connections**

#### Test MySQL:

```bash
mysql -u root -p -e "USE cms; SHOW TABLES;"
```

#### Test MongoDB:

```bash
mongosh --eval "use prescriptions; db.runCommand('ping')"
```

## 🚀 Running the Application

### 1. **Navigate to App Directory**

```bash
cd app
```

### 2. **Run the Application**

```bash
mvn spring-boot:run
```

### 3. **Test the Application**

```bash
# Health check
curl http://localhost:8085/health

# Frontend
open http://localhost:8085
```

## 🔍 Troubleshooting

### MySQL Issues:

- **Access denied**: Run `mysql_secure_installation` to set password
- **Connection refused**: Start MySQL with `brew services start mysql`
- **Database not found**: Create it manually with `CREATE DATABASE cms;`

### MongoDB Issues:

- **Connection refused**: Start MongoDB with `brew services start mongodb/brew/mongodb-community@7.0`
- **Command not found**: Install MongoDB with `brew install mongodb-community@7.0`

### Application Issues:

- **Port already in use**: Change port in `application.properties`
- **Compilation errors**: Make sure Java 21 is installed and Lombok is working in your IDE

## 📊 Database Schema

### MySQL Tables (Auto-created by JPA):

- `patient` - Patient information
- `doctor` - Doctor information
- `appointment` - Appointment records
- `admin` - Admin users
- `doctor_available_times` - Doctor availability

### MongoDB Collections:

- `prescriptions` - Prescription documents

## 🔐 Security Notes

1. **Change default passwords** in production
2. **Use strong JWT secrets** in production
3. **Enable SSL** for database connections in production
4. **Restrict database access** to application only

## 📝 Quick Commands

```bash
# Start all services
brew services start mysql
brew services start mongodb/brew/mongodb-community@7.0

# Stop all services
brew services stop mysql
brew services stop mongodb/brew/mongodb-community@7.0

# Check service status
brew services list | grep -E "(mysql|mongodb)"

# View logs
tail -f /opt/homebrew/var/log/mysql/mysql.log
tail -f /opt/homebrew/var/log/mongodb/mongo.log
```
