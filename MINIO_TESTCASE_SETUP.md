# MinIO Test Case File Setup Guide

## File Naming Convention

For each problem, create a test case file in MinIO with one of these naming patterns:

### Option 1: Direct file naming using problem title (Current setup)
```
{problemTitle}.txt
{problemTitle}
```
Examples:
- `Two Sum.txt` (for Two Sum problem)
- `Two Sum` (direct file without extension)
- `Add Two Numbers.txt`

### Option 2: Folder structure using problem title
```
{problemTitle}/testcases.txt
{problemTitle}/test.txt
```
Examples:
- `Two Sum/testcases.txt`
- `Add Two Numbers/testcases.txt`

### Option 3: Problem ID based (Alternative)
```
{problemId}.txt
```
Examples:
- `1.txt` (for problem ID 1)
- `2.txt` (for problem ID 2)

## File Content Format

Use the arrow format with parameter assignments:

```
nums = [2,7,11,15], target = 9 → output: [0,1]
nums = [3,2,4], target = 6 → output: [1,2]
nums = [3,3], target = 6 → output: [0,1]
```

### For different problem types:

**Array problems:**
```
head_arr = [4,5,1,9], node_val = 5 → output: [4,1,9]
arr = [1,2,3,4], k = 2 → output: [3,4,1,2]
```

**String problems:**
```
s = "hello", pattern = "ll" → output: 2
text = "abcdef", word = "cd" → output: true
```

**Multiple parameters:**
```
matrix = [[1,2],[3,4]], target = 3 → output: true
grid = [[0,1],[1,0]], start = [0,0], end = [1,1] → output: 2
```

## MinIO Bucket Structure

```
testcases/
├── Two Sum.txt           # Test cases for Two Sum problem
├── Two Sum               # Alternative: file without extension
├── Add Two Numbers.txt   # Test cases for Add Two Numbers problem
└── ...
```

## Example Test Case Files

### Two Sum.txt (Two Sum problem)
```
nums = [2,7,11,15], target = 9 → output: [0,1]
nums = [3,2,4], target = 6 → output: [1,2]
nums = [3,3], target = 6 → output: [0,1]
```

### Add Two Numbers.txt (Add Two Numbers problem)
```
l1 = [2,4,3], l2 = [5,6,4] → output: [7,0,8]
l1 = [0], l2 = [0] → output: [0]
l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9] → output: [8,9,9,9,0,0,0,1]
```

## Important Notes

1. **Variable names must match**: The variable names in test cases should match what your function expects
2. **Arrow format required**: Use ` → output: ` (with spaces) to separate input from expected output
3. **Python format**: Values should be in Python format (lists, strings, etc.)
4. **First 2 test cases are visible**: Only the first 2 test cases are shown to users during "Run", all are used during "Submit"

## Backend Processing

The MinIO service will:
1. Look for `{problemTitle}.txt` or `{problemTitle}` first
2. Parse the arrow format: `param1 = value1, param2 = value2 → output: expected`
3. Extract variable assignments and expected outputs
4. Generate Python test runner code
5. Execute in Docker container
6. Return results with verdicts (AC, WA, TLE, MLE, CE, RE)

## Testing Your Setup

1. Upload test case file to MinIO bucket `testcases`
2. Name it with the problem ID (e.g., `1.txt`)
3. Use the contest room to run code
4. Check backend logs for MinIO file loading
5. Verify test cases are parsed correctly