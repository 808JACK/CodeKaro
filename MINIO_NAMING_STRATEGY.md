# MinIO File Naming Strategy

## Problem Data Structure
When problems are loaded, you get:
```json
{
  "id": 1,
  "title": "Two Sum",
  "difficulty": "EASY",
  "topicIds": [1]
}
```

## Naming Options

### Option 1: Use Problem ID (Current Default)
**Code:** `problemName = currentProblem.id.toString()`
**MinIO Files:**
- `1.txt`
- `2.txt`
- `3.txt`

**Pros:**
- Simple numeric naming
- No spaces or special characters
- Easy to manage
- Consistent with database IDs

**Cons:**
- Not human-readable
- Need to map ID to problem name

### Option 2: Use Problem Title (Exact)
**Code:** `problemName = currentProblem.title`
**MinIO Files:**
- `Two Sum.txt`
- `Add Two Numbers.txt`
- `Longest Substring Without Repeating Characters.txt`

**Pros:**
- Human-readable
- Self-documenting

**Cons:**
- Spaces in filenames
- Potential special characters
- Case sensitivity issues
- Longer file names

### Option 3: Use Normalized Title
**Code:** `problemName = currentProblem.title.toLowerCase().replace(/\s+/g, '-')`
**MinIO Files:**
- `two-sum.txt`
- `add-two-numbers.txt`
- `longest-substring-without-repeating-characters.txt`

**Pros:**
- Human-readable
- No spaces or special characters
- URL-friendly
- Consistent formatting

**Cons:**
- Need to normalize consistently
- Longer file names

## Current Implementation

The code is currently set to use **Option 1 (Problem ID)**:
```typescript
const problemName = currentProblem.id.toString(); // Using ID (e.g., "1")
```

## How to Change

### To use Problem Title (Option 2):
```typescript
const problemName = currentProblem.title; // Using title (e.g., "Two Sum")
```

### To use Normalized Title (Option 3):
```typescript
const problemName = currentProblem.title.toLowerCase().replace(/\s+/g, '-'); // Using normalized title (e.g., "two-sum")
```

## Recommendation

**Use Option 1 (Problem ID)** because:
1. Your MinIO files are likely already named `1.txt`, `2.txt`, etc.
2. It's the simplest and most reliable approach
3. No issues with special characters or spaces
4. Consistent with database structure

## Verification

Check your MinIO bucket to see how your files are currently named:
1. Go to MinIO console: `http://localhost:9000`
2. Open `testcases` bucket
3. Check file names:
   - If you see `1.txt`, `2.txt` → Use Option 1 (current default)
   - If you see `Two Sum.txt` → Use Option 2
   - If you see `two-sum.txt` → Use Option 3

## Debug Output

The console will show:
```
Problem ID: 1
Problem Title: Two Sum
Problem Name being sent: 1
Expected MinIO file: 1.txt
```

This helps you verify which naming strategy is being used and what file the system expects to find.