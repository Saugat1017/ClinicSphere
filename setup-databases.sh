#!/bin/bash

echo "🗄️  Setting up Databases for Smart Clinic Management System"
echo "=========================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Check if MySQL is running
echo "🔍 Checking MySQL status..."
if brew services list | grep mysql | grep started > /dev/null; then
    print_status "MySQL is running"
else
    print_warning "MySQL is not running. Starting it..."
    brew services start mysql
    sleep 3
fi

# Try to connect to MySQL and create database
echo "🗄️  Setting up MySQL database..."
echo "Please enter your MySQL root password (or press Enter if no password):"
read -s MYSQL_PASSWORD

if [ -z "$MYSQL_PASSWORD" ]; then
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null && {
        print_status "MySQL database 'cms' created successfully"
    } || {
        print_error "Failed to create MySQL database. You may need to set a root password."
        echo "Run: mysql_secure_installation"
    }
else
    mysql -u root -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null && {
        print_status "MySQL database 'cms' created successfully"
    } || {
        print_error "Failed to create MySQL database. Check your password."
    }
fi

# Check if MongoDB is installed
echo "🔍 Checking MongoDB installation..."
if command -v mongod &> /dev/null; then
    print_status "MongoDB is installed"
else
    print_warning "MongoDB is not installed. Installing it..."
    brew tap mongodb/brew
    brew install mongodb-community@7.0
fi

# Start MongoDB service
echo "🔍 Checking MongoDB status..."
if brew services list | grep mongodb | grep started > /dev/null; then
    print_status "MongoDB is running"
else
    print_warning "MongoDB is not running. Starting it..."
    brew services start mongodb/brew/mongodb-community@7.0
    sleep 3
fi

# Create MongoDB database
echo "🗄️  Setting up MongoDB database..."
mongosh --eval "use prescriptions" --quiet 2>/dev/null && {
    print_status "MongoDB database 'prescriptions' is ready"
} || {
    print_warning "MongoDB connection failed. Make sure MongoDB is running."
}

echo ""
echo "🎯 Database Setup Summary:"
echo "=========================="
echo "MySQL:"
echo "  - Database: cms"
echo "  - Connection: localhost:3306"
echo ""
echo "MongoDB:"
echo "  - Database: prescriptions"
echo "  - Connection: localhost:27017"
echo ""
echo "📝 Next Steps:"
echo "1. Update your application.properties with correct credentials"
echo "2. Run: cd app && mvn spring-boot:run"
echo ""
echo "🔧 Environment Variables to set:"
echo "export DB_USERNAME=root"
echo "export DB_PASSWORD=your_mysql_password"
echo "export MONGODB_URI=mongodb://localhost:27017/prescriptions" 