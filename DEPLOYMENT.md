# Deployment Guide

## 🚀 Render Deployment (Production)

### Prerequisites
1. Railway MinIO is already set up ✅
2. All production configs are updated ✅
3. Dockerfiles are optimized ✅

### Option 1: Using render.yaml (Two-Step Process)
1. **First Deployment**: Push your code to GitHub and connect to Render
2. **Deploy Eureka First**: Render will deploy all services, but you need the Eureka URL
3. **Update URLs**: After Eureka deploys, update the render.yaml with actual service URLs
4. **Redeploy**: Push the updated render.yaml to complete the setup

**Important**: You need to replace `XXXX` in render.yaml with actual service IDs after first deployment

### Option 2: Manual Deployment
Deploy services in this order:

1. **Eureka Service** (Deploy first)
   - Service Type: Web Service
   - Build Command: `docker build -f "Eureka Service/Dockerfile" "Eureka Service"`
   - Start Command: `java -jar app.jar`
   - Port: 8761

2. **Auth Service**
   - Service Type: Web Service
   - Build Command: `docker build -f "Auth Service/Dockerfile" "Auth Service"`
   - Environment Variables:
     - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://[eureka-service-url]:8761/eureka`
   - Port: 8091

3. **Problem Service**
   - Service Type: Web Service
   - Build Command: `docker build -f "Problem Service/Dockerfile" "Problem Service"`
   - Environment Variables:
     - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://[eureka-service-url]:8761/eureka`
   - Port: 8082

4. **Collab Service**
   - Service Type: Web Service
   - Build Command: `docker build -f "Collab Service/Dockerfile" "Collab Service"`
   - Environment Variables:
     - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://[eureka-service-url]:8761/eureka`
   - Port: 8092

5. **API Gateway** (Deploy last)
   - Service Type: Web Service
   - Build Command: `docker build -f "ApiGateway/Dockerfile" "ApiGateway"`
   - Environment Variables:
     - `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://[eureka-service-url]:8761/eureka`
     - `AUTH_SERVICE_URL=http://[auth-service-url]:8091`
     - `PROBLEM_SERVICE_URL=http://[problem-service-url]:8082`
     - `COLLAB_SERVICE_URL=http://[collab-service-url]:8092`
   - Port: 8086

## 🔧 Local Development

### Using Docker Compose
```bash
# Start all services (excluding MinIO for production)
docker-compose up --build

# Start with MinIO for local development
docker-compose --profile dev up --build

# Stop all services
docker-compose down
```

### Using start-dev.bat (Windows)
```bash
start-dev.bat
```

## 📋 Service URLs (After Deployment)

- **API Gateway**: `https://api-gateway-[random].onrender.com` (Main entry point)
- **Eureka Dashboard**: `https://eureka-service-[random].onrender.com`
- **MinIO Console**: `https://minio-console-[random].up.railway.app` (Railway)

## 🔍 Health Checks

All services include health check endpoints:
- `/actuator/health` - Spring Boot health endpoint
- Docker health checks are configured for automatic restarts

## 🚨 Important Notes

1. **Service Discovery**: All services register with Eureka for automatic discovery
2. **MinIO**: Uses Railway cloud MinIO (not local Docker)
3. **Database**: All services use cloud databases (Neon PostgreSQL, MongoDB Atlas, Cassandra Astra)
4. **Frontend**: Deploy separately (excluded from this setup)
5. **CORS**: Update API Gateway CORS settings with your frontend URL

## 🛠️ Troubleshooting

1. **Services not connecting**: Check Eureka dashboard for registered services
2. **Database connection issues**: Verify cloud database credentials
3. **MinIO issues**: Check Railway MinIO service status
4. **Build failures**: Check Dockerfile syntax and dependencies

## 📝 Environment Variables Summary

### All Services
- `SPRING_PROFILES_ACTIVE=prod`
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-service:8761/eureka`

### API Gateway Additional
- `AUTH_SERVICE_URL=http://auth-service:8091`
- `PROBLEM_SERVICE_URL=http://problem-service:8082`
- `COLLAB_SERVICE_URL=http://collab-service:8092`