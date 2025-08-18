# Frontend Integration Guide for Contest Submissions

## Required Frontend Data for Contest Submissions

### 1. Contest Code Submission Endpoint
```
POST /api/contests/{contestCode}/code/submit
```

### 2. Required Request Body (CodeExecutionRequest)
```json
{
  "code": "public class Solution { ... }",
  "language": "java",
  "problemId": 1,
  "problemName": "Two Sum",
  "timeLimitMs": 5000,
  "memoryLimitMb": 256,
  
  // CRITICAL: Contest context fields for MongoDB saving
  "contestId": null,  // Optional - backend will resolve from contestCode
  "userId": 123,      // REQUIRED - current user ID
  "userName": "john_doe", // REQUIRED - current username
  "timeFromStartMs": 1800000  // REQUIRED - time since contest started (30 minutes = 1800000ms)
}
```

### 3. Frontend Implementation Checklist

#### ✅ Required Fields the Frontend MUST Send:
- `code` - The user's solution code
- `language` - Programming language (java, python, cpp, etc.)
- `problemId` - ID of the problem being solved
- `problemName` - Name/title of the problem
- `userId` - Current logged-in user ID
- `userName` - Current logged-in username
- `timeFromStartMs` - Time elapsed since contest started

#### ✅ Optional Fields:
- `contestId` - Backend will resolve from URL path contestCode
- `timeLimitMs` - Defaults to 5000ms
- `memoryLimitMb` - Defaults to 256MB

### 4. Frontend Code Example

```javascript
// Calculate time from contest start
const contestStartTime = new Date(contest.startTime);
const timeFromStartMs = Date.now() - contestStartTime.getTime();

// Prepare submission data
const submissionData = {
  code: editorCode,
  language: selectedLanguage,
  problemId: currentProblem.id,
  problemName: currentProblem.title,
  userId: currentUser.id,
  userName: currentUser.username,
  timeFromStartMs: timeFromStartMs,
  timeLimitMs: 5000,
  memoryLimitMb: 256
};

// Submit to contest
const response = await fetch(`/api/contests/${contestCode}/code/submit`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-User-Id': currentUser.id
  },
  body: JSON.stringify(submissionData)
});
```

### 5. Backend Processing Flow

1. **Extract contestCode** from URL path parameter
2. **Resolve real contestId** from PostgreSQL using contestCode
3. **Save submission** to MongoDB with real contestId
4. **Execute code** and get results
5. **Update submission** with execution results
6. **Return response** to frontend

### 6. Critical Frontend Requirements

#### ❌ Common Issues to Avoid:
- Missing `userId` or `userName` fields
- Not calculating `timeFromStartMs` properly
- Sending `contestId` instead of using URL path
- Missing contest context when submitting

#### ✅ Best Practices:
- Always use the contest room URL format: `/contests/{contestCode}/problem/{problemId}`
- Calculate `timeFromStartMs` from contest start time
- Include user context in every submission
- Handle submission responses properly

### 7. Response Handling

```javascript
const result = await response.json();

// Response structure (CodeExecutionResponse)
{
  "overallVerdict": "AC",  // AC, WA, TLE, CE, etc.
  "testResults": [
    {
      "testNumber": 1,
      "verdict": "AC",
      "timeMs": 150,
      "output": "expected output",
      "error": null
    }
  ],
  "error": null
}
```

### 8. Debugging Endpoints

For testing frontend integration:

```bash
# Test MongoDB connection
GET /api/contests/test-mongo

# Test contest mapping
GET /api/contests/debug/contest-mapping

# Test specific contest code flow
POST /api/contests/test-room-code-flow?roomCode={contestCode}
```

## Summary

The frontend must send:
1. **Contest context** via URL path (`/{contestCode}/code/submit`)
2. **User context** (`userId`, `userName`)
3. **Time context** (`timeFromStartMs`)
4. **Problem context** (`problemId`, `problemName`)
5. **Code context** (`code`, `language`)

This ensures proper MongoDB submission saving with correct contest ID resolution.