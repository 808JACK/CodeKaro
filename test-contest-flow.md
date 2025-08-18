# Contest ID Synchronization Test

## Current Flow
1. **Create Contest** → PostgreSQL generates real ID
2. **Submit Code** → Fetch real contest ID using contest code
3. **Save to MongoDB** → Use the real contest ID

## Test Steps

### 1. Create a Contest
```bash
POST /api/contests/create
Headers: X-User-Id: 1
Body: {
  "name": "Test Contest",
  "description": "Testing ID sync",
  "durationMinutes": 60,
  "problemIds": [1, 2, 3]
}
```
**Expected**: Returns `{"inviteCode": "ABC123", "roomId": 5}`

### 2. Submit Code Using Contest Code
```bash
POST /api/contests/ABC123/code/submit
Body: {
  "problemId": 1,
  "problemName": "Two Sum",
  "code": "public class Solution { ... }",
  "language": "java",
  "userId": 1,
  "userName": "testuser"
}
```

**Expected Flow**:
- Extract contestCode = "ABC123" from URL
- Call `contestService.getContestByCode("ABC123")`
- Get contest with ID = 5
- Save to MongoDB with contestId = 5

### 3. Verify Data Consistency
```bash
GET /api/contests/debug/contest-mapping
```

**Expected**:
```json
{
  "postgresContests": {
    "ABC123": 5
  },
  "mongoSubmissionsByContestId": {
    "5": 1
  }
}
```

## Key Code Logic

```java
// Priority 2: Get from contest code in URL path
else if (contestCode != null && !contestCode.isEmpty()) {
    try {
        Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
        if (contestOpt.isPresent()) {
            contestId = contestOpt.get().getId(); // This gets the REAL ID!
            log.info("Found REAL contestId {} for contestCode {}", contestId, contestCode);
        }
    }
}
```

This ensures MongoDB submissions use the exact same ID as PostgreSQL contests!