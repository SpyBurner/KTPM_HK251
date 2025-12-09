#!/bin/bash

# Intelligent Tutoring System - Service Startup Script
echo "=== Starting Intelligent Tutoring System Services ==="

# Function to check if a service is running on a specific port
check_service() {
    local port=$1
    local service_name=$2
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo "✓ $service_name is already running on port $port"
        return 0
    else
        echo "✗ $service_name is not running on port $port"
        return 1
    fi
}

# Function to start a service
start_service() {
    local service_dir=$1
    local service_name=$2
    local port=$3
    
    echo "Starting $service_name..."
    cd "$service_dir"
    mvn spring-boot:run > "../logs/${service_name}.log" 2>&1 &
    local pid=$!
    echo $pid > "../logs/${service_name}.pid"
    echo "✓ $service_name started with PID $pid (port $port)"
    echo "  Log file: logs/${service_name}.log"
    cd ..
}

# Create logs directory
mkdir -p logs

echo ""
echo "Checking current service status..."
check_service 8761 "Eureka Server"
check_service 8081 "User Service"
check_service 8082 "Course Service"

echo ""
echo "Building all services..."
mvn clean install -DskipTests -q
if [ $? -ne 0 ]; then
    echo "❌ Build failed! Please check the error messages above."
    exit 1
fi
echo "✓ Build completed successfully"

echo ""
echo "Starting services in proper order..."

# Start Eureka Server first
if ! check_service 8761 "Eureka Server" >/dev/null; then
    start_service "eureka-server" "eureka-server" "8761"
    echo "⏳ Waiting 30 seconds for Eureka Server to fully start..."
    sleep 30
fi

# Start User Service
if ! check_service 8081 "User Service" >/dev/null; then
    start_service "user-service" "user-service" "8081"
    echo "⏳ Waiting 15 seconds for User Service to start..."
    sleep 15
fi

# Start Course Service
if ! check_service 8082 "Course Service" >/dev/null; then
    start_service "course-service" "course-service" "8082"
    echo "⏳ Waiting 15 seconds for Course Service to start..."
    sleep 15
fi

echo ""
echo "=== Service Status ==="
echo "Eureka Server: http://localhost:8761"
echo "User Service: http://localhost:8081/api/users/health"
echo "Course Service: http://localhost:8082/api/courses/health"

echo ""
echo "=== Quick Health Check ==="
if check_service 8761 "Eureka Server" >/dev/null; then
    echo "✓ Eureka Server: Running"
else
    echo "❌ Eureka Server: Not responding"
fi

if check_service 8081 "User Service" >/dev/null; then
    echo "✓ User Service: Running"
else
    echo "❌ User Service: Not responding"
fi

if check_service 8082 "Course Service" >/dev/null; then
    echo "✓ Course Service: Running"
else
    echo "❌ Course Service: Not responding"
fi

echo ""
echo "=== Usage Examples ==="
echo "# Check Eureka Dashboard:"
echo "open http://localhost:8761"
echo ""
echo "# Test User Service (no auth required):"
echo "curl http://localhost:8081/api/users/health"
echo ""
echo "# Test Course Service (no auth required):"
echo "curl http://localhost:8082/api/courses/health"
echo ""
echo "# Get users (requires auth):"
echo "curl -u admin:admin123 http://localhost:8081/api/users"
echo ""
echo "# Get courses (requires auth):"
echo "curl -u admin:admin123 http://localhost:8082/api/courses"

echo ""
echo "=== Service Management ==="
echo "# View logs:"
echo "tail -f logs/eureka-server.log"
echo "tail -f logs/user-service.log"
echo "tail -f logs/course-service.log"
echo ""
echo "# Stop services:"
echo "./stop-services.sh"
echo ""
echo "🎉 All services should be running! Check the endpoints above to verify."