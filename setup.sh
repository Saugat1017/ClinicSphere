#!/bin/bash

echo "🚀 Setting up Smart Clinic Management System"
echo "=============================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check if MySQL is running
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed. Please install MySQL 8.0 or higher."
    exit 1
fi

echo "✅ Prerequisites check passed!"

# Set default environment variables if not set
export DB_USERNAME=${DB_USERNAME:-root}
export DB_PASSWORD=${DB_PASSWORD:-password}
export JWT_SECRET=${JWT_SECRET:-your-super-secret-jwt-key-change-this-in-production}
export MONGODB_URI=${MONGODB_URI:-mongodb://localhost:27017/prescriptions}

echo "📝 Environment variables:"
echo "   DB_USERNAME: $DB_USERNAME"
echo "   DB_PASSWORD: $DB_PASSWORD"
echo "   JWT_SECRET: $JWT_SECRET"
echo "   MONGODB_URI: $MONGODB_URI"

# Create database if it doesn't exist
echo "🗄️  Setting up database..."
mysql -u"$DB_USERNAME" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null || {
    echo "⚠️  Could not create database. Please make sure MySQL is running and credentials are correct."
    echo "   You can create the database manually: CREATE DATABASE cms;"
}

echo "🔧 Building the application..."
cd app
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🎉 Setup complete! You can now run the application:"
    echo "   cd app"
    echo "   mvn spring-boot:run"
    echo ""
    echo "🌐 Access the application at:"
    echo "   Frontend: http://localhost:8085"
    echo "   Health Check: http://localhost:8085/health"
else
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi 