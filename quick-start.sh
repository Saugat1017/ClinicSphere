#!/bin/bash

echo "🚀 Quick Start for Smart Clinic Management System"
echo "================================================"

# Check if Java 21 is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" != "21" ]; then
    echo "⚠️  Warning: Java version is $JAVA_VERSION. Java 21 is recommended."
fi

# Set environment variables
export DB_USERNAME=${DB_USERNAME:-root}
export DB_PASSWORD=${DB_PASSWORD:-password}
export JWT_SECRET=${JWT_SECRET:-your-super-secret-jwt-key-change-this-in-production}

echo "📝 Environment variables:"
echo "   DB_USERNAME: $DB_USERNAME"
echo "   DB_PASSWORD: $DB_PASSWORD"
echo "   JWT_SECRET: $JWT_SECRET"

# Create database if it doesn't exist
echo "🗄️  Setting up database..."
mysql -u"$DB_USERNAME" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null || {
    echo "⚠️  Could not create database. Please make sure MySQL is running."
    echo "   You can create the database manually: CREATE DATABASE cms;"
}

echo ""
echo "🎯 Next Steps:"
echo "1. Make sure MySQL is running on localhost:3306"
echo "2. Create the database: CREATE DATABASE cms;"
echo "3. Run the application: cd app && mvn spring-boot:run"
echo ""
echo "🌐 Once running, access:"
echo "   Frontend: http://localhost:8085"
echo "   Health Check: http://localhost:8085/health"
echo ""
echo "📚 For API documentation, see: API_DOCUMENTATION.md"
echo "📖 For setup instructions, see: README.md" 