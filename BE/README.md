# Intelligent Tutoring System - Service-Based Architecture

This project implements a microservices-based intelligent tutoring system using Spring Boot and Spring Cloud.

## Architecture Overview

The system consists of the following services:

### Core Services
1. **Eureka Server** (Port 8761) - Service discovery and registration
2. **User Service** (Port 8081) - User management and authentication
3. **Course Service** (Port 8082) - Course management and content
4. **Common Library** - Shared utilities, DTOs, and configurations

### Technology Stack
- **Spring Boot 3.2.0** - Core framework
- **Spring Cloud 2023.0.0** - Microservices infrastructure
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer
- **PostgreSQL** - Database
- **Eureka** - Service discovery
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate code

## Project Structure

```
BE/
├── pom.xml                 # Root POM with shared configuration
├── eureka-server/          # Service discovery server
├── common-lib/             # Shared library
├── user-service/           # User management microservice
└── course-service/         # Course management microservice
```

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA or Eclipse)

### Database Setup

Create databases for each service:

```sql
CREATE DATABASE its_user_db;
CREATE DATABASE its_course_db;
CREATE USER its_user WITH PASSWORD 'its_password';
GRANT ALL PRIVILEGES ON DATABASE its_user_db TO its_user;
GRANT ALL PRIVILEGES ON DATABASE its_course_db TO its_user;
```

### Running the Services

1. **Build the project:**
   ```bash
   cd BE
   mvn clean install
   ```

2. **Start services in order:**

   a) Start Eureka Server:
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Access at: http://localhost:8761

   b) Start User Service:
   ```bash
   cd user-service
   mvn spring-boot:run
   ```
   
   c) Start Course Service:
   ```bash
   cd course-service
   mvn spring-boot:run
   ```

### Service Endpoints

#### User Service (localhost:8081)
- GET `/api/users/health` - Health check (no auth required)
- GET `/api/users` - Get all users (requires auth)
- GET `/api/users/{id}` - Get user by ID (requires auth)
- POST `/api/users` - Create new user (requires auth)

#### Course Service (localhost:8082)
- GET `/api/courses/health` - Health check (no auth required)
- GET `/api/courses` - Get all courses (requires auth)
- GET `/api/courses/{id}` - Get course by ID (requires auth)
- POST `/api/courses` - Create new course (requires auth)

#### Authentication
All protected endpoints use Basic Authentication:
- Username: `admin`
- Password: `admin123`

### Monitoring

Each service exposes actuator endpoints for monitoring:
- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`

## Key Features

### Service Discovery
- Services automatically register with Eureka Server
- Load balancing and failover capabilities
- Service-to-service communication via service names

### Shared Components
- Common DTOs and response wrappers
- Global exception handling
- Standard validation patterns
- Consistent logging configuration

### Security
- Stateless authentication using Spring Security
- Per-service security configuration
- Public health check endpoints

### Data Management
- Separate databases per service
- JPA with Hibernate for ORM
- Database connection pooling

## Development Guidelines

### Adding New Services
1. Create new module in root POM
2. Extend from parent POM configuration
3. Add common-lib dependency
4. Configure unique port and database
5. Enable service discovery with `@EnableDiscoveryClient`

### API Design
- Use `ApiResponse<T>` wrapper for all endpoints
- Follow RESTful conventions
- Implement proper HTTP status codes
- Add comprehensive logging

### Exception Handling
- Use `BusinessException` for business logic errors
- Global exception handler captures all errors
- Consistent error response format

## Testing

Run tests for all modules:
```bash
mvn test
```

Run tests for specific service:
```bash
cd user-service
mvn test
```

## Future Enhancements

1. **API Gateway** - Add Spring Cloud Gateway for request routing
2. **Configuration Server** - Centralized configuration management
3. **Distributed Tracing** - Add Sleuth/Zipkin for request tracing
4. **Circuit Breaker** - Add Hystrix/Resilience4j for fault tolerance
5. **Message Queue** - Add RabbitMQ/Kafka for async communication
6. **Containerization** - Docker containers and Kubernetes deployment
7. **Authentication Service** - JWT-based authentication with OAuth2

## Troubleshooting

### Common Issues

1. **Service not registering with Eureka:**
   - Check Eureka Server is running on port 8761
   - Verify eureka.client.service-url.defaultZone configuration

2. **Database connection errors:**
   - Ensure PostgreSQL is running
   - Verify database credentials and URLs
   - Check if databases exist

3. **Port conflicts:**
   - Verify no other applications using ports 8761, 8081, 8082
   - Check firewall settings

### Logs
Check application logs for detailed error information:
- Service logs show in console
- Adjust logging levels in application.properties