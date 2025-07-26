#!/bin/bash

echo "🚀 Starting Smart Clinic Management System Frontend"
echo "=================================================="

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
    echo "   Download from: https://nodejs.org/"
    exit 1
fi

# Check Node.js version
NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 16 ]; then
    echo "⚠️  Warning: Node.js version is $NODE_VERSION. Node.js 16 or higher is recommended."
fi

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is not installed. Please install npm."
    exit 1
fi

echo "✅ Prerequisites check passed!"

# Navigate to frontend directory
if [ ! -d "frontend" ]; then
    echo "❌ Frontend directory not found. Please make sure you're in the project root."
    exit 1
fi

cd frontend

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
    
    if [ $? -ne 0 ]; then
        echo "❌ Failed to install dependencies."
        exit 1
    fi
fi

echo "🌐 Starting development server..."
echo "   Frontend will be available at: http://localhost:3000"
echo "   Make sure your Spring Boot backend is running on: http://localhost:8085"
echo ""
echo "📝 Demo Credentials:"
echo "   Patient: patient@example.com / password123"
echo "   Doctor: doctor@clinic.com / password123"
echo "   Admin: admin / admin123"
echo ""

# Start the development server
npm start 