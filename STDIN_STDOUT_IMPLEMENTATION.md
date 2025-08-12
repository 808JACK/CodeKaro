# Stdin/Stdout Implementation Plan

## Overview
Switch from function parameter detection to stdin/stdout approach for better user experience and flexibility.

## Changes Required

### 1. MinIO Test Case Format Update

**Old Format:**
```
head_arr = [4,5,1,9], node_val = 5 → output: [4,1,9]
nums = [2,7,11,15], target = 9 → output: [0,1]
```

**New Format:**
```
4 5 1 9
5
→ output: 4 1 9

2 7 11 15
9
→ output: 0 1
```

### 2. User Code Style

**Users write complete solutions:**
```python
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def deleteNode(node):
    node.val = node.next.val
    node.next = node.next.next

def create_linked_list(arr):
    head = ListNode(arr[0])
    curr = head
    for val in arr[1:]:
        curr.next = ListNode(val)
        curr = curr.next
    return head

def linked_list_to_array(head):
    result = []
    current = head
    while current:
        result.append(current.val)
        current = current.next
    return result

# Main solution
arr = list(map(int, input().split()))
node_val = int(input())

head = create_linked_list(arr)
curr = head
while curr and curr.val != node_val:
    curr = curr.next

if curr and curr.next:
    deleteNode(curr)

result = linked_list_to_array(head)
print(' '.join(map(str, result)))
```

### 3. Backend Implementation

#### A. Update MinIOService.java

Add new parsing method for stdin/stdout format:

```java
private List<TestCase> parseStdinStdoutFormat(String content) {
    List<TestCase> testCases = new ArrayList<>();
    try {
        // Split by → output: to separate test cases
        String[] testBlocks = content.split("→\\s*output:");
        
        for (int i = 0; i < testBlocks.length - 1; i++) {
            String inputBlock = testBlocks[i].trim();
            String outputBlock = testBlocks[i + 1].trim();
            
            // Extract input lines (everything before next test case or end)
            String[] lines = inputBlock.split("\n");
            StringBuilder inputBuilder = new StringBuilder();
            
            // Take lines that are not empty and not from previous output
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.matches("\\d+\\s+\\d+.*")) {
                    if (inputBuilder.length() > 0) inputBuilder.append("\n");
                    inputBuilder.append(line);
                }
            }
            
            // Extract expected output (first line after →)
            String expectedOutput = outputBlock.split("\n")[0].trim();
            
            testCases.add(new TestCase(inputBuilder.toString(), expectedOutput, i < 2));
        }
        
        log.info("Successfully parsed {} test cases from stdin/stdout format", testCases.size());
    } catch (Exception e) {
        log.error("Error parsing stdin/stdout format: {}", e.getMessage(), e);
    }
    return testCases;
}
```

#### B. Update CodeExecutionService.java

Add new execution method:

```java
private List<TestResult> executeTestCasesStdinStdout(String containerId, String code, List<TestCase> testCases, long timeLimitMs, boolean isSubmit) {
    List<TestResult> results = new ArrayList<>();
    
    for (int i = 0; i < testCases.size(); i++) {
        TestCase tc = testCases.get(i);
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Create input file for this test case
            String inputData = tc.getInput();
            
            // Execute user code with stdin input
            ProcessBuilder pb = new ProcessBuilder("docker", "exec", "-i", containerId, "python3", "-c", code);
            Process process = pb.start();
            
            // Provide input via stdin
            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                writer.write(inputData);
                writer.flush();
            }
            
            boolean finished = process.waitFor(timeLimitMs, TimeUnit.MILLISECONDS);
            long execTime = System.currentTimeMillis() - startTime;
            
            if (!finished) {
                process.destroyForcibly();
                results.add(new TestResult(i + 1, "TLE", execTime, "", "Time limit exceeded"));
                continue;
            }
            
            String output = readStream(process.getInputStream()).trim();
            String error = readStream(process.getErrorStream()).trim();
            
            if (process.exitValue() != 0) {
                String errorType = determineErrorType(error);
                String cleanError = cleanErrorMessage(error);
                results.add(new TestResult(i + 1, errorType, execTime, output, cleanError));
                continue;
            }
            
            // Compare output
            String expectedOutput = tc.getExpectedOutput().trim();
            String verdict = output.equals(expectedOutput) ? "AC" : "WA";
            
            results.add(new TestResult(i + 1, verdict, execTime, output, ""));
            
        } catch (Exception e) {
            results.add(new TestResult(i + 1, "RE", 0L, "", e.getMessage()));
        }
    }
    
    return results;
}
```

### 4. Migration Strategy

1. **Update MinIO files** to new format
2. **Add format detection** in MinIOService to support both formats during transition
3. **Update CodeExecutionService** to use new execution method
4. **Update documentation** with new user code examples

### 5. Benefits

✅ **No function name requirements**
✅ **No parameter signature matching**
✅ **More intuitive for users**
✅ **Matches competitive programming style**
✅ **Easier to test locally**
✅ **Works with any problem type**

### 6. Test Case Examples

#### Two Sum Problem
```
2 7 11 15
9
→ output: 0 1

3 2 4
6
→ output: 1 2
```

#### Delete Node in Linked List
```
4 5 1 9
5
→ output: 4 1 9

4 5 1 9
1
→ output: 4 5 9
```

#### Add Two Numbers
```
2 4 3
5 6 4
→ output: 7 0 8

0
0
→ output: 0
```

This approach eliminates all the complexity around function detection and parameter parsing!