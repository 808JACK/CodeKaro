# Contest Code Execution Testing Guide

## Setup Steps

### 1. MinIO Test Case Files
Your MinIO should already have test case files in the `testcases` bucket with the format:
- `1.txt` (for problem ID 1)
- `2.txt` (for problem ID 2)
- etc.

### 2. Start Services
Make sure these services are running:
- **Collab Service** on `http://localhost:8092`
- **Problem Service** on `http://localhost:8086`
- **MinIO** on `http://localhost:9000`
- **Docker** (for code execution)

### 3. Frontend Setup
- **Frontend** on `http://localhost:5173` (or your dev server port)

## Testing Flow

### Step 1: Create/Join Contest
1. Create a contest with problem ID `1` (Two Sum)
2. Or join an existing contest

### Step 2: Write Code
In the contest room, write a solution from scratch (no template provided - users implement complete solution)

### Step 3: Run Tests
1. Click "Run" button
2. Check browser console for debug logs:
   - Problem ID being sent
   - MinIO file name expected
   - Request/response details

### Step 4: Verify Results
Expected behavior:
- ✅ Test cases show with visual indicators
- ✅ Execution time displayed
- ✅ Verdict badges (AC, WA, etc.)
- ✅ Actual vs expected output comparison
- ✅ Toast notifications for results

## Debug Checklist

### Frontend Debug (Browser Console)
Look for these logs:
```
=== CODE EXECUTION DEBUG ===
Problem ID: 1
Problem Name being sent: 1
Expected MinIO file: 1.txt
```

### Backend Debug (Collab Service Logs)
Look for these logs:
```
INFO - Executing python code for problem 1
INFO - Loaded X test cases from arrow format
INFO - Code execution completed for problem 1. Verdict: AC
```

### MinIO Debug
Check if files exist:
1. Go to MinIO console: `http://localhost:9000`
2. Check `testcases` bucket
3. Verify `1.txt` exists with correct content

## Common Issues & Solutions

### Issue: "No Test Cases" verdict
**Cause:** MinIO file not found or incorrectly named
**Solution:** 
- Check file exists as `{problemId}.txt` in `testcases` bucket
- Verify problem ID matches file name

### Issue: "CE" (Compilation Error) verdict
**Cause:** Python syntax errors
**Solution:**
- Check code syntax
- Ensure proper indentation
- Verify function name matches expected

### Issue: "RE" (Runtime Error) verdict
**Cause:** Code throws exception during execution
**Solution:**
- Check for division by zero
- Verify array bounds
- Handle edge cases

### Issue: "WA" (Wrong Answer) verdict
**Cause:** Code logic is incorrect
**Solution:**
- Compare actual vs expected output
- Debug algorithm logic
- Test with sample inputs manually

### Issue: "TLE" (Time Limit Exceeded) verdict
**Cause:** Code takes too long (>2 seconds per test)
**Solution:**
- Optimize algorithm complexity
- Remove infinite loops
- Use more efficient data structures

## Test Cases Format Verification

Your MinIO file should look exactly like this:
```
nums = [2,7,11,15], target = 9 → output: [0,1]
nums = [3,2,4], target = 6 → output: [1,2]
```

**Important:**
- Use ` → output: ` (with spaces around arrow)
- Variable names must match function parameters
- Values in Python format (lists, strings, etc.)
- One test case per line

## Expected API Flow

1. **Frontend** → `POST /api/contests/code/run`
2. **Collab Service** → MinIO to get test cases from `{problemId}.txt`
3. **Collab Service** → Docker container for execution
4. **Docker** → Returns results with verdicts
5. **Frontend** → Updates UI with visual indicators

## Success Indicators

✅ **All working correctly when you see:**
- Console logs showing correct problem ID
- MinIO file found and parsed
- Test cases executed in Docker
- Results displayed with ✓/✗ indicators
- Toast notifications showing pass/fail counts
- Execution times displayed
- Error messages shown for failed cases