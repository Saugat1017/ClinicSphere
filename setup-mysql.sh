#!/bin/bash

echo "🗄️  MySQL Database Setup"
echo "========================"

echo "Please enter your MySQL root password (or press Enter if no password):"
read -s MYSQL_PASSWORD

if [ -z "$MYSQL_PASSWORD" ]; then
    echo "Attempting to connect without password..."
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null && {
        echo "✅ MySQL database 'cms' created successfully"
        echo "✅ You can connect without password"
    } || {
        echo "❌ Failed to create database. You may need to set a password."
        echo "Run: mysql_secure_installation"
    }
else
    echo "Attempting to connect with password..."
    mysql -u root -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS cms;" 2>/dev/null && {
        echo "✅ MySQL database 'cms' created successfully"
        echo "✅ Password authentication working"
        echo ""
        echo "🔧 Set this environment variable:"
        echo "export DB_PASSWORD=$MYSQL_PASSWORD"
    } || {
        echo "❌ Failed to create database. Check your password."
    }
fi

echo ""
echo "📝 Next steps:"
echo "1. Set environment variables:"
echo "   export DB_USERNAME=root"
echo "   export DB_PASSWORD=your_password"
echo ""
echo "2. Run the application:"
echo "   cd app && mvn spring-boot:run" 