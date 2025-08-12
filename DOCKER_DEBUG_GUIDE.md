# Docker Container Debugging Guide

## Pre-requisites Check

### 1. Docker Desktop Running
```bash
# Check if Docker is running
docker --version
docker ps

# Should show Docker version and running containers
```

### 2. Python Image Available
```bash
# Check if Python image exists
docker images | grep python

# If not available, pull it
docker pull python:3.11-alpine
```

### 3. Collab Service Logs
Check the Collab Service startup logs for:
```
INFO - Docker CLI is available - real code execution enabled
INFO - Python image python:3.11-alpine is available
INFO - Pre-created reusable container: <container-id>
```

## Common Issues & Solutions

### Issue 1: "Docker not available"
**Symptoms:**
- Frontend shows "Docker is not running"
- Backend logs: "Docker CLI not available - using mock execution"

**Solution:**
1. Start Docker Desktop
2. Wait for Docker to fully start
3. Restart Collab Service
4. Check logs for "Docker CLI is available"

### Issue 2: "Failed to get Docker container"
**Symptoms:**
- Frontend shows "Docker container creation failed"
- Backend logs: "Failed to pre-create container"

**Solution:**
1. Check Docker memory/CPU limits
2. Ensure Python image is available:
   ```bash
   docker pull python:3.11-alpine
   ```
3. Check Docker daemon is responsive:
   ```bash
   docker run --rm python:3.11-alpine echo "test"
   ```

### Issue 3: Container Creation Fails
**Backend logs to look for:**
```
ERROR - Failed to pre-create container: <error-message>
WARN - Error cleaning up container <id>: <error>
```

**Debug steps:**
1. Manual container creation:
   ```bash
   docker run -d --memory=256m --cpus=1 python:3.11-alpine sleep 3600
   ```
2. Check container status:
   ```bash
   docker ps -a
   ```
3. Check container logs:
   ```bash
   docker logs <container-id>
   ```

### Issue 4: Code Execution Timeout
**Symptoms:**
- Test cases show "TLE" (Time Limit Exceeded)
- Long delays before results

**Debug:**
1. Check container is responsive:
   ```bash
   docker exec <container-id> python3 -c "print('Hello')"
   ```
2. Monitor container resources:
   ```bash
   docker stats <container-id>
   ```

## Backend Debug Logs to Monitor

### Startup Logs
```
INFO - Docker CLI is available - real code execution enabled
INFO - Python image python:3.11-alpine is available  
INFO - Pre-created reusable container: abc123def456
```

### Execution Logs
```
INFO - Executing python code for problem 1
INFO - Using container abc123def456 for problem 1
INFO - Loaded 4 test cases from arrow format
DEBUG - Test case 1: input='nums = [2,7,11,15], target = 9', expected='[0,1]'
INFO - Code execution completed for problem 1. Verdict: AC
```

### Error Logs to Watch For
```
ERROR - Error executing code for problem 1: <error>
WARN - Failed to pre-create container: <error>
ERROR - Error executing test cases: <error>
WARN - Container abc123def456 cleaned up
```

## Frontend Debug Console

### Successful Execution
```
=== CODE EXECUTION DEBUG ===
Problem ID: 1
Problem Name being sent: 1
Expected MinIO file: 1.txt

=== CONTEST SERVICE RUN CODE ===
API Endpoint: http://localhost:8092/api/contests/code/run
Request payload: {code: "...", language: "python", problemName: "1", ...}
Response status: 200
Response ok: true

=== RUN CODE RESPONSE ===
Overall verdict: AC
Test results count: 4
```

### Error Scenarios
```
=== CODE EXECUTION ERROR ===
Error type: TypeError
Error message: Failed to fetch
Debug hint: Check if Collab Service is running on http://localhost:8092
```

## Manual Testing Commands

### 1. Test Docker Availability
```bash
# From command line
docker run --rm python:3.11-alpine python3 -c "print('Docker works!')"
```

### 2. Test Container Creation
```bash
# Create container like the service does
docker run -d --memory=256m --cpus=1 python:3.11-alpine sleep 3600
```

### 3. Test Code Execution
```bash
# Execute Python code in container
docker exec <container-id> python3 -c "
def two_sum(nums, target):
    for i in range(len(nums)):
        for j in range(i+1, len(nums)):
            if nums[i] + nums[j] == target:
                return [i, j]
    return []

nums = [2,7,11,15]
target = 9
result = two_sum(nums, target)
print(result)
"
```

### 4. Test MinIO File Access
```bash
# Check if test case files exist
curl -X GET "http://localhost:9000/testcases/1.txt"
```

## Troubleshooting Checklist

- [ ] Docker Desktop is running
- [ ] Python image `python:3.11-alpine` is available
- [ ] Collab Service shows "Docker CLI is available"
- [ ] Container pre-creation succeeds
- [ ] MinIO has test case files (`1.txt`, `2.txt`, etc.)
- [ ] API endpoints are accessible (`http://localhost:8092/api/contests/code/run`)
- [ ] Frontend can reach backend (no CORS issues)
- [ ] Browser console shows detailed debug logs
- [ ] Backend logs show successful execution flow

## Success Indicators

✅ **Everything working when you see:**
- Container pre-created successfully in logs
- API calls return 200 status
- Test cases parsed from MinIO files
- Code executed in Docker container
- Results returned with proper verdicts
- Frontend shows ✓/✗ indicators
- No error messages in console