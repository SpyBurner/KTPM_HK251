#!/bin/bash

# Intelligent Tutoring System - Service Stop Script
echo "=== Stopping Intelligent Tutoring System Services ==="

# Function to stop a service by PID file
stop_service() {
    local service_name=$1
    local pid_file="logs/${service_name}.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null; then
            echo "Stopping $service_name (PID: $pid)..."
            kill $pid
            # Wait for graceful shutdown
            sleep 5
            # Force kill if still running
            if ps -p $pid > /dev/null; then
                echo "Force killing $service_name..."
                kill -9 $pid
            fi
            echo "✓ $service_name stopped"
        else
            echo "⚠ $service_name was not running (stale PID file)"
        fi
        rm -f "$pid_file"
    else
        echo "⚠ No PID file found for $service_name"
    fi
}

# Function to stop service by port
stop_by_port() {
    local port=$1
    local service_name=$2
    
    local pid=$(lsof -ti:$port)
    if [ ! -z "$pid" ]; then
        echo "Stopping $service_name on port $port (PID: $pid)..."
        kill $pid
        sleep 3
        # Force kill if still running
        local still_running=$(lsof -ti:$port)
        if [ ! -z "$still_running" ]; then
            echo "Force killing $service_name..."
            kill -9 $still_running
        fi
        echo "✓ $service_name stopped"
    else
        echo "⚠ No process found on port $port for $service_name"
    fi
}

echo ""
echo "Stopping services..."

# Stop services in reverse order
stop_service "course-service"
stop_service "user-service"
stop_service "eureka-server"

echo ""
echo "Cleaning up any remaining processes on service ports..."
stop_by_port 8082 "Course Service"
stop_by_port 8081 "User Service"
stop_by_port 8761 "Eureka Server"

echo ""
echo "Cleaning up log files..."
rm -f logs/*.pid

echo ""
echo "=== Final Status Check ==="
if lsof -Pi :8761 -sTCP:LISTEN -t >/dev/null ; then
    echo "❌ Eureka Server still running on port 8761"
else
    echo "✓ Eureka Server stopped"
fi

if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null ; then
    echo "❌ User Service still running on port 8081"
else
    echo "✓ User Service stopped"
fi

if lsof -Pi :8082 -sTCP:LISTEN -t >/dev/null ; then
    echo "❌ Course Service still running on port 8082"
else
    echo "✓ Course Service stopped"
fi

echo ""
echo "🛑 All services have been stopped!"